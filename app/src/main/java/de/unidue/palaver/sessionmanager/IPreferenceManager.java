package de.unidue.palaver.sessionmanager;

import de.unidue.palaver.activity.SettingsActivity;

public interface IPreferenceManager {
    void setUserName(String userName);
    String getUserName();

    void setPassword(String password);
    String getPassword();

    Boolean getIsLogin();
    void setIsLogin(boolean isLogin);

    boolean getAutoLoginPreference();
    void setAutoLoginPreference(boolean checked);

    boolean getAllowNotificationPreference();
    void setAllowNotificationPreference(boolean checked);

    boolean getAllowVibrationPreference();
    void setAllowVibrationPreference(boolean checked);

    Boolean getPasswordChanged();
    void setPasswordChanged(boolean b);

    void setNewPassword(String newPassword);
    void registerSharedPreference(SettingsActivity settingsActivity);
}
