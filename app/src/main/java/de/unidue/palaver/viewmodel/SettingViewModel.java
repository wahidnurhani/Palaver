package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.unidue.palaver.dialogandtoast.ChangePasswordDialog;
import de.unidue.palaver.sessionmanager.PreferenceManager;
import de.unidue.palaver.sessionmanager.SessionManager;

public class SettingViewModel extends AndroidViewModel {
    SessionManager sessionManager;
    PreferenceManager preferenceManager;
    LiveData<Boolean> passwordChanged;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        sessionManager = SessionManager.getSessionManagerInstance(application);
        preferenceManager = sessionManager.getPreferenceManager();
        passwordChanged = sessionManager.getPasswordChanged();
    }

    public LiveData<Boolean> getPasswordChanged() {
        return passwordChanged;
    }

    public void handlePasswordChanged() {
        sessionManager.endSession();
    }

    public String getUserName() {
        return sessionManager.getUser().getUserName();
    }

    public void handleChangePasswordRequest(FragmentActivity activity) {
        ChangePasswordDialog.startDialog(getApplication(), activity);
    }

    public boolean getAutoLoginPreference() {
        return preferenceManager.getAutoLoginPreference();
    }

    public void setAutoLoginPreference(boolean checked) {
        preferenceManager.setAutoLoginPreference(checked);
    }

    public boolean getAllowNotificationPreference() {
        return preferenceManager.getAllowNotificationPreference();
    }

    public void setAllowNotification(boolean checked) {
        preferenceManager.setAllowNotificationPreference(checked);
    }

    public boolean getAllowVibrationPreference() {
        return preferenceManager.getAllowVibrationPreference();
    }

    public void setAllowVibrationPreference(boolean checked) {
        preferenceManager.setAllowVibrationPreference(checked);
    }
}
