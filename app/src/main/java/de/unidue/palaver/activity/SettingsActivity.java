package de.unidue.palaver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.unidue.palaver.R;
import de.unidue.palaver.roomdatabase.PalaverDB;
import de.unidue.palaver.sessionmanager.PreferenceContract;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.viewmodel.SettingViewModel;
import de.unidue.palaver.viewmodel.ViewModelProviderFactory;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static String TAG = SettingsActivity.class.getSimpleName();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ChatManagerActivity.startActivity(SettingsActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        PalaverDB.getDatabase(getApplicationContext());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager.getSessionManagerInstance(getApplicationContext())
                .getPreferenceManager()
                .registerSharedPreference(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private ViewModelProviderFactory viewModelProviderFactory;
        private SettingViewModel settingViewModel;
        private LiveData<Boolean> passwordChanged;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.fragment_setting, rootKey);

            viewModelProviderFactory = new ViewModelProviderFactory(getActivity().getApplication());
            settingViewModel = new ViewModelProvider(this, viewModelProviderFactory)
                    .get(SettingViewModel.class);
            passwordChanged = settingViewModel.getPasswordChanged();

            passwordChanged.observe(this, aBoolean -> {
                if(aBoolean){
                    settingViewModel.handlePasswordChanged();

                    SplashScreenActivity.startActivity(getActivity());
                }
            });

            Preference preferenceUsername = findPreference(PreferenceContract.KEY_USERNAME);
            preferenceUsername.setSummary(settingViewModel.getUserName());

            Preference editTextPreferenceChangePassword = findPreference(PreferenceContract.KEY_CHANGE_PASSWORD);
            editTextPreferenceChangePassword.setOnPreferenceClickListener(preference -> {
                settingViewModel.handleChangePasswordRequest(getActivity().getApplicationContext(), getActivity());
                return true;
            });

            CheckBoxPreference checkBoxPreferenceAutoLogin = findPreference(PreferenceContract.KEY_AUTO_LOGIN);
            checkBoxPreferenceAutoLogin.setChecked(settingViewModel.getAutoLoginPreference());
            checkBoxPreferenceAutoLogin.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = Boolean.parseBoolean(newValue.toString());
                settingViewModel.setAutoLoginPreference(checked);
                Log.i(TAG, "Auto Login : "+ settingViewModel.getAutoLoginPreference()+"");
                return true;
            });
            CheckBoxPreference checkBoxPreferenceAllowNotification = findPreference(PreferenceContract.KEY_ALLOW_NOTIFICATION);
            checkBoxPreferenceAllowNotification.setChecked(settingViewModel.getAllowNotificationPreference());
            checkBoxPreferenceAllowNotification.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = Boolean.parseBoolean(newValue.toString());
                settingViewModel.setAllowNotification(checked);
                Log.i(TAG, "Notification : "+settingViewModel.getAllowNotificationPreference()+"");
                return true;
            });
            CheckBoxPreference checkBoxPreferenceAllowVibration = findPreference(PreferenceContract.KEY_ALLOW_VIBRATION);
            checkBoxPreferenceAllowVibration.setChecked(settingViewModel.getAllowVibrationPreference());
            checkBoxPreferenceAllowNotification.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = Boolean.parseBoolean(newValue.toString());
                settingViewModel.setAllowVibrationPreference(checked);
                Log.i(TAG, "Vibration : "+ settingViewModel.getAllowVibrationPreference()+"");
                return true;
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}