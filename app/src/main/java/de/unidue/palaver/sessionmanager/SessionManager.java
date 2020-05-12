package de.unidue.palaver.sessionmanager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.CustomToast;
import de.unidue.palaver.httpclient.Retrofit;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.roomdatabase.PalaverRoomDatabase;
import de.unidue.palaver.service.ServicePopulateDB;
import retrofit2.Response;

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    private SharedPreferences pref;
    private Context application;
    private SharedPreferences.Editor editor;
    private MutableLiveData<Boolean> loginStatus;
    private MutableLiveData<Boolean> registerStatus;
    private PalaverDao palaverDao;

    private static final String PREF_NAME = String.valueOf(R.string.palaver_sharedPreferences);
    private static final String KEY_IS_LOGIN = String.valueOf(R.string.is_log_in);
    private static final String KEY_USERNAME = String.valueOf(R.string.username);
    private static final String KEY_PASSWORD = String.valueOf(R.string.password);

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
        int PRIVATE_MODE = 0;
        this.pref = this.application.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor= pref.edit();
        this.loginStatus = new MutableLiveData<>();
        this.registerStatus = new MutableLiveData<>();
        this.palaverDao = PalaverRoomDatabase.getDatabase(application).palaverDao();
        this.loginStatus.setValue(pref.getBoolean(KEY_IS_LOGIN, false));
        this.registerStatus.setValue(false);
    }

    private void startSession(String userName, String password){
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
        populateDB();
    }

    private void populateDB(){
        ServicePopulateDB.startIntent(application, getUser());
    }

    private void changeUserPassword(String password){
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public LiveData<Boolean> getRegisterStatus() {
        return registerStatus;
    }

    public User getUser() {
        String username = pref.getString(KEY_USERNAME,"");
        String password = pref.getString(KEY_PASSWORD,"");
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

    private void cleanData() {
        new CleanDatabase(palaverDao).execute();
    }

    public void resetAll() {
        resetLoginStatus();
        resetRegisterStatus();
        cleanData();
    }

    public void endSession(){
        editor.clear();
        editor.commit();
        resetAll();
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
            } else {
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
            }
            CustomToast.makeText(application,stackApiResponseListResponse.body().getInfo());
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
