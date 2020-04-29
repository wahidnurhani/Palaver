package de.unidue.palaver.model;

public class Preference {

    private boolean autoLogin;

    public Preference() { }

    public void setDefault() {
        autoLogin = true;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public boolean getAutoLogin(){
        return autoLogin;
    }
}
