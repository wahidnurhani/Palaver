package de.unidue.palaver.sessionmanager;
import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.unidue.palaver.dialogandtoast.CustomToast;
import de.unidue.palaver.httpclient.IHttpClient;
import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.httpclient.RetrofitHttpClient;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.User;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.roomdatabase.PalaverDao;
import de.unidue.palaver.serviceandworker.ServicePopulateDB;
import de.unidue.palaver.serviceandworker.WorkerPushToken;
import retrofit2.Response;

public class SessionManager implements ISessionManager{
    private static String TAG = SessionManager.class.getSimpleName();

    private PreferenceManager preferenceManager;
    private Application application;
    private MutableLiveData<Boolean> loginStatus;
    private MutableLiveData<Boolean> registerStatus;
    private MutableLiveData<Boolean> passwordChanged;
    private PalaverDao palaverDao;

    @SuppressLint("StaticFieldLeak")
    private static SessionManager sessionManagerInstance;

    public static SessionManager getSessionManagerInstance(Application application){
        if (sessionManagerInstance==null){
            sessionManagerInstance= new SessionManager(application);
        }
        return sessionManagerInstance;
    }

    @SuppressLint("CommitPrefEdits")
    private SessionManager(Application application) {
        this.application = application;
        this.preferenceManager = new PreferenceManager(application);
        this.loginStatus = new MutableLiveData<>();
        this.registerStatus = new MutableLiveData<>();
        this.passwordChanged = new MutableLiveData<>();
        this.palaverDao = PalaverDB.getDatabase(application).palaverDao();
        this.loginStatus.setValue(preferenceManager.getIsLogin());
        this.passwordChanged.setValue(preferenceManager.getPasswordChanged());
        this.registerStatus.setValue(false);
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
        return new User(preferenceManager.getUserName(), preferenceManager.getPassword());
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    @Override
    public void login(User user) {
        new LoginProcessor().execute(user);
    }

    @Override
    public void startSession(String userName, String password){
        Log.i(TAG, "session started");
        preferenceManager.handleStartSession(userName, password);
        populateDB();
        initPushTokenWorkManager();
    }

    private void initPushTokenWorkManager() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .build();
        PeriodicWorkRequest refreshTokenWorkRequest = new PeriodicWorkRequest
                .Builder(WorkerPushToken.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints).build();
        WorkManager.getInstance(application).enqueue(refreshTokenWorkRequest);
    }

    @Override
    public void populateDB(){
        ServicePopulateDB.startIntent(application, getUser());
    }

    @Override
    public void register(User user) {
        new RegisterProcessor().execute(user);
    }

    @Override
    public void changePassword(String newPassword) {
        new ChangePasswordProcessor(getUser(), newPassword).execute();
    }

    @Override
    public void endSession(){
        this.preferenceManager.handleEndSession();
        this.loginStatus.setValue(false);
        this.registerStatus.setValue(false);
        this.passwordChanged.setValue(false);
        cleanData();
    }

    @Override
    public void cleanData() {
        new CleanDatabase(palaverDao).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginProcessor extends AsyncTask<User, Void, Response<StackApiResponseList<String>>> {

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(User... users) {

            IHttpClient retrofitHttpClient = new RetrofitHttpClient();
            Response<StackApiResponseList<String>> responseAuthenticate = null;
            try {
                responseAuthenticate = retrofitHttpClient.authenticate(users[0]);
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

            IHttpClient retrofitHttpClient = new RetrofitHttpClient();

            Response<StackApiResponseList<String>> responseAuthenticate = null;
            try {
                responseAuthenticate = retrofitHttpClient.register(users[0]);
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

    @SuppressLint("StaticFieldLeak")
    private class ChangePasswordProcessor extends AsyncTask<Void, Void, Response<StackApiResponseList<String>>> {
        private User user;
        private String newPassword;

        public ChangePasswordProcessor(User user, String newPassword) {
            this.user = user;
            this.newPassword= newPassword;
        }

        @Override
        protected Response<StackApiResponseList<String>> doInBackground(Void... voids) {

            IHttpClient retrofitHttpClient = new RetrofitHttpClient();

            Response<StackApiResponseList<String>> responseChangePassword = null;
            try {
                responseChangePassword = retrofitHttpClient.changePassword(new JSONBuilder.ChangePassWord(user, newPassword));
                assert responseChangePassword.body() != null;
                if(responseChangePassword.body().getMessageType()==1){
                    preferenceManager.setNewPassword(newPassword);
                    Log.i(TAG, "change password success");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseChangePassword;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> stackApiResponseListResponse) {
            assert stackApiResponseListResponse.body() != null;
            if(stackApiResponseListResponse.body().getMessageType()==1){
                passwordChanged.setValue(true);
                CustomToast.makeText(application,stackApiResponseListResponse.body().getInfo());
            } else {
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
