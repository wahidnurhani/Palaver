package de.unidue.palaver.sessionmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import de.unidue.palaver.activity.SettingsActivity;
import de.unidue.palaver.model.User;

public class PreferenceManager implements IPreferenceManager{

    private Context application;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Context application) {
        this.application =application;
        this.pref = this.application.getSharedPreferences(PreferenceContract.PREF_NAME,
                PreferenceContract.PRIVATE_MODE);
        this.editor= pref.edit();
    }

    public void handleStartSession(String userName, String password) {
        setPasswordChanged(false);
        setIsLogin(true);
        setUserName(userName);
        setPassword(password);
        editor.commit();
    }

    public void handleEndSession() {
        editor.clear();
        editor.commit();
    }

    @Override
    public void setUserName(String userName){
        editor.putString(PreferenceContract.KEY_USERNAME, userName);
        editor.commit();
    }

    @Override
    public String getUserName(){
        return pref.getString(PreferenceContract.KEY_USERNAME,"");
    }

    @Override
    public void setPassword(String password) {
        editor.putString(PreferenceContract.KEY_PASSWORD, password);
        editor.commit();
    }

    @Override
    public String getPassword(){
        return pref.getString(PreferenceContract.KEY_PASSWORD,"");
    }

    @Override
    public Boolean getIsLogin() {
        return pref.getBoolean(PreferenceContract.KEY_IS_LOGIN, false);
    }

    @Override
    public void setIsLogin(boolean isLogin){
        editor.putBoolean(PreferenceContract.KEY_IS_LOGIN, isLogin);
        editor.commit();
    }

    @Override
    public void setAutoLoginPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_AUTO_LOGIN, checked);
        editor.commit();
    }

    @Override
    public boolean getAutoLoginPreference() {
        return pref.getBoolean(PreferenceContract.KEY_AUTO_LOGIN, true);
    }

    @Override
    public boolean getAllowNotificationPreference() {
        return pref.getBoolean(PreferenceContract.KEY_ALLOW_NOTIFICATION, true);
    }

    @Override
    public void setAllowNotificationPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_ALLOW_NOTIFICATION, checked);
        editor.commit();
    }

    @Override
    public boolean getAllowVibrationPreference() {
        return pref.getBoolean(PreferenceContract.KEY_ALLOW_VIBRATION, true);
    }

    @Override
    public void setAllowVibrationPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_ALLOW_VIBRATION, checked);
        editor.commit();
    }

    @Override
    public void setNewPassword(String newPassword) {
        editor.putString(PreferenceContract.KEY_PASSWORD, newPassword);
        editor.commit();
    }

    @Override
    public Boolean getPasswordChanged() {
        return pref.getBoolean(PreferenceContract.KEY_PASSWORD_CHANGED, false);
    }

    @Override
    public void setPasswordChanged(boolean b) {
        editor.putBoolean(PreferenceContract.KEY_PASSWORD_CHANGED, b);
    }

    @Override
    public void registerSharedPreference(SettingsActivity settingsActivity) {
        pref.registerOnSharedPreferenceChangeListener(settingsActivity);
    }


}
