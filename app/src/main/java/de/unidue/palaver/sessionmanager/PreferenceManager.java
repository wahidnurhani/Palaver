package de.unidue.palaver.sessionmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import de.unidue.palaver.activity.SettingsActivity;
import de.unidue.palaver.model.User;

public class PreferenceManager {

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
        editor.putBoolean(PreferenceContract.KEY_IS_LOGIN, true);
        editor.putBoolean(PreferenceContract.KEY_PASSWORD_CHANGED, false);
        editor.putString(PreferenceContract.KEY_USERNAME, userName);
        editor.putString(PreferenceContract.KEY_PASSWORD, password);
        editor.commit();
    }

    public SharedPreferences getPref() {
        return pref;
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

    public void handleEndSession() {
        editor.clear();
        editor.commit();
    }

    public void setAutoLoginPreference(boolean checked) {
        editor.putBoolean(PreferenceContract.KEY_AUTO_LOGIN, checked);
        editor.commit();
    }

    public boolean getAutoLoginPreference() {
        return pref.getBoolean(PreferenceContract.KEY_AUTO_LOGIN, true);
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

    public void setNewPassword(String newPassword) {
        editor.putString(PreferenceContract.KEY_PASSWORD, newPassword);
        editor.commit();
    }

    public void registerSharedPreference(SettingsActivity settingsActivity) {
        pref.registerOnSharedPreferenceChangeListener(settingsActivity);
    }
}
