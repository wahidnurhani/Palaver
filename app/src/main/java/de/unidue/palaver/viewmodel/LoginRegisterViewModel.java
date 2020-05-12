package de.unidue.palaver.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import de.unidue.palaver.model.User;
import de.unidue.palaver.sessionmanager.SessionManager;

public class LoginRegisterViewModel extends AndroidViewModel {

    private SessionManager sessionManager;
    private LiveData<Boolean> loginStatus;
    private LiveData<Boolean> registerStatus;

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        this.sessionManager = SessionManager.getSessionManagerInstance(application);
        this.loginStatus = sessionManager.getLoginStatus();
        this.registerStatus = sessionManager.getRegisterStatus();
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public LiveData<Boolean> getRegisterStatus() {
        return registerStatus;
    }

    public void login(User user){
        sessionManager.login(user);
    }

    public void register(User user){
        sessionManager.register(user);
    }

    public void reset() {
        sessionManager.resetAll();
    }
}
