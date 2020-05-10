package de.unidue.palaver.system.sessionmanager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.User;

public class SessionManager {

    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    private static SessionManager sessionManagerInstance;

    private static final String PREF_NAME = String.valueOf(R.string.palaver_sharedPreferences);
    private static final String KEY_IS_LOGIN = String.valueOf(R.string.is_log_in);
    private static final String KEY_USERNAME = String.valueOf(R.string.username);
    private static final String KEY_PASSWORD = String.valueOf(R.string.password);

    public static SessionManager getSessionManagerInstance(Context context){
        if (sessionManagerInstance==null){
            sessionManagerInstance= new SessionManager(context);
        }
        return sessionManagerInstance;
    }

    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context context) {
        this.context=context;
        int PRIVATE_MODE = 0;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor= pref.edit();
    }

    private void createLoginSession(String username, String password){
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUser(User user) {
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_PASSWORD, user.getUserName());
        editor.commit();
    }

    public void startSession(String userName, String password){
        createLoginSession(userName,password);
    }

    public void changeUserPassword(String password){
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public void endSession(){
        editor.clear();
        editor.commit();
        sessionManagerInstance=null;
    }

    public boolean chekLogin(){
        return this.isLoggedin();
    }

    private boolean isLoggedin() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public User getUser() {
        String username = pref.getString(KEY_USERNAME,"");
        String password = pref.getString(KEY_PASSWORD,"");
        return new User(username, password);
    }

    public SharedPreferences getPreference() {
        return pref;
    }
}
