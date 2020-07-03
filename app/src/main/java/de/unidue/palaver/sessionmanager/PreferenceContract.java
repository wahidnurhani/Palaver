package de.unidue.palaver.sessionmanager;

import de.unidue.palaver.R;

public class PreferenceContract {
    public static final String PREF_NAME = String.valueOf(R.string.palaver_sharedPreferences);
    public static int PRIVATE_MODE=0;


    public static final String KEY_AUTO_LOGIN= "auto_login_preference";
    public static final String KEY_ALLOW_NOTIFICATION = "notification_allow_preference";
    public static final String KEY_ALLOW_FIBRATION ="notification_vibration_preference";
}
