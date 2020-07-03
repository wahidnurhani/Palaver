package de.unidue.palaver.sessionmanager;

import de.unidue.palaver.R;

public class PreferenceContract {
    public static final String PREF_NAME = String.valueOf(R.string.palaver_sharedPreferences);
    public static int PRIVATE_MODE=0;

    public static final String KEY_IS_LOGIN = String.valueOf(R.string.is_log_in);
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = String.valueOf(R.string.password);
    public static final String KEY_PASSWORD_CHANGED = String.valueOf(R.string.passwordChanged);

    public static final CharSequence KEY_CHANGE_PASSWORD = "password";
    public static final String KEY_AUTO_LOGIN= "auto_login_preference";
    public static final String KEY_ALLOW_NOTIFICATION = "notification_allow_preference";
    public static final String KEY_ALLOW_VIBRATION ="notification_vibration_preference";
}
