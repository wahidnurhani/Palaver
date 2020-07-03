package de.unidue.palaver.sessionmanager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.Preference;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.unidue.palaver.R;
import de.unidue.palaver.dialogandtoast.CustomToast;
import de.unidue.palaver.httpclient.Retrofit;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.service.ServicePopulateDB;
import de.unidue.palaver.worker.PushTokenWorker;
import retrofit2.Response;

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences pref;
    private Context application;
    private SharedPreferences.Editor editor;
    private MutableLiveData<Boolean> loginStatus;
    private MutableLiveData<Boolean> registerStatus;
    private MutableLiveData<Boolean> passwordChanged;
    private PalaverDao palaverDao;




    @SuppressLint("StaticFieldLeak")
    private static SessionManager sessionManagerInstance;

    public static SessionManager getSessionManagerInstance(Context application){
        if (sessionManagerInstance==null){
            sessionManagerInstance= new SessionManager(application);
        }
        return sessionManagerInstance;
    }

    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context application) {
        this.application = application;
        this.pref = this.application.getSharedPreferences(PreferenceContract.PREF_NAME,
                PreferenceContract.PRIVATE_MODE);
        this.editor= pref.edit();
        this.loginStatus = new MutableLiveData<>();
        this.registerStatus = new MutableLiveData<>();
        this.passwordChanged = new MutableLiveData<>();
        this.palaverDao = PalaverDB.getDatabase(application).palaverDao();
        this.loginStatus.setValue(pref.getBoolean(PreferenceContract.KEY_IS_LOGIN, false));
        this.passwordChanged.setValue(pref.getBoolean(PreferenceContract.KEY_PASSWORD_CHANGED, false));
        this.registerStatus.setValue(false);
    }

    private void startSession(String userName, String password){
        Log.i(TAG, "session started");
        editor.putBoolean(PreferenceContract.KEY_IS_LOGIN, true);
        editor.putBoolean(PreferenceContract.KEY_PASSWORD_CHANGED, false);
        editor.putString(PreferenceContract.KEY_USERNAME, userName);
        editor.putString(PreferenceContract.KEY_PASSWORD, password);
        editor.commit();
        populateDB();

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .build();

        PeriodicWorkRequest refreshTokenWorkRequest = new PeriodicWorkRequest
                .Builder(PushTokenWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints).build();

        WorkManager.getInstance(application).enqueue(refreshTokenWorkRequest);
    }

    public SharedPreferences getPref() {
        return pref;
    }

    private void populateDB(){
        ServicePopulateDB.startIntent(application, getUser());
    }

    private void changeUserPassword(String password){
        editor.putString(PreferenceContract.KEY_PASSWORD, password);
        editor.commit();
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public LiveData<Boolean> getRegisterStatus() {
        return registerStatus;
    }

    public MutableLiveData<Boolean> getPasswordChanged() {
        return passwordChanged;
    }

    public User getUser() {
        String username = pref.getString(PreferenceContract.KEY_USERNAME,"");
        String password = pref.getString(PreferenceContract.KEY_PASSWORD,"");
        assert username != null;
        assert password != null;
        if(username.equals("")|| password.equals("")){
            return null;
        }
        return new User(username, password);
    }

    public void login(User user) {
        new LoginProcessor().execute(user);
    }

    public void register(User user) {
        new RegisterProcessor().execute(user);
    }

    private void resetRegisterStatus() {
        this.registerStatus.setValue(false);
    }

    private void resetLoginStatus() {
        this.loginStatus.setValue(false);
    }

    private void resetChangePasswordStatus(){ this.passwordChanged.setValue(false);}

    private void cleanData() {
        new CleanDatabase(palaverDao).execute();
    }

    public void resetAll() {
        resetLoginStatus();
        resetRegisterStatus();
        resetChangePasswordStatus();
        cleanData();
    }

    public void endSession(){
        editor.clear();
        editor.commit();
        resetAll();
    }

    public boolean getAutoLoginPreference() {
        return pref.getBoolean(PreferenceContract.KEY_AUTO_LOGIN, true);
    }

    public void setAutoLoginPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_AUTO_LOGIN, checked);
        editor.commit();
    }

    public boolean getAllowNotificationPreference() {
        return pref.getBoolean(PreferenceContract.KEY_ALLOW_NOTIFICATION, true);
    }

    public void setAllowNotificationPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_ALLOW_NOTIFICATION, checked);
        editor.commit();
    }

    public boolean getAllowVibrationPreference() {
        return pref.getBoolean(PreferenceContract.KEY_ALLOW_VIBRATION, true);
    }

    public void setAllowVibrationPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_ALLOW_VIBRATION, checked);
        editor.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginProcessor extends AsyncTask<User, Void, Response<StackApiResponseList<String>>> {

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(User... users) {

            Retrofit retrofit = new Retrofit();
            Response<StackApiResponseList<String>> responseAuthenticate = null;
            try {
                responseAuthenticate = retrofit.authenticate(users[0]);
                assert responseAuthenticate.body() != null;
                if(responseAuthenticate.body().getMessageType()==1){
                    Log.i(TAG, "login Success");
                    startSession(users[0].getUserName(), users[0].getPassword());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseAuthenticate;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> stackApiResponseListResponse) {
            assert stackApiResponseListResponse.body() != null;
            if(stackApiResponseListResponse.body().getMessageType()==1){
                loginStatus.setValue(true);
                passwordChanged.setValue(false);
            } else {
                loginStatus.setValue(false);
                CustomToast.makeText(application, stackApiResponseListResponse.body().getInfo());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterProcessor extends AsyncTask<User, Void, Response<StackApiResponseList<String>>> {

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(User... users) {

            Retrofit retrofit = new Retrofit();

            Response<StackApiResponseList<String>> responseAuthenticate = null;
            try {
                responseAuthenticate = retrofit.register(users[0]);
                assert responseAuthenticate.body() != null;
                if(responseAuthenticate.body().getMessageType()==1){
                    Log.i(TAG, "register Success");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseAuthenticate;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> stackApiResponseListResponse) {
            assert stackApiResponseListResponse.body() != null;
            if(stackApiResponseListResponse.body().getMessageType()==1){
                registerStatus.setValue(true);
            } else {
                registerStatus.setValue(false);
                CustomToast.makeText(application,stackApiResponseListResponse.body().getInfo());
            }
        }
    }

    private static class CleanDatabase extends AsyncTask<Void, Void, Void> {
        PalaverDao palaverDao;

        CleanDatabase(PalaverDao palaverDao) {
            this.palaverDao = palaverDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            palaverDao.deleteAllChat();
            palaverDao.deleteAllFriend();
            return null;
        }
    }
}
