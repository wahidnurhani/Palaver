package de.unidue.palaver.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import de.unidue.palaver.sessionmanager.SessionManager;

public class SettingsViewModel extends AndroidViewModel {
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences;

    public SettingsViewModel(Application application) {
        super(application);
        this.sessionManager = SessionManager.getSessionManagerInstance(getApplication());
        setupSharedPreferences();
    }

    private void setupSharedPreferences(){
        this.sharedPreferences = sessionManager.getPref();
        sharedPreferences.registerOnSharedPreferenceChangeListener(getApplication());
    }


}
