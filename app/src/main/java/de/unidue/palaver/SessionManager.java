package de.unidue.palaver;

import de.unidue.palaver.model.User;

public class SessionManager {

    private User user;
    private String status = "inactive";
    private Preference preference;

    public SessionManager(Palaver palaver) {
        this.preference= new Preference();
        preference.setDefault();
    }

    public String getStatus(){
        return status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void startSession(){
        this.status="active";
    }

    public void endSession(){
        this.user=null;
        this.status="inactive";
    }

    public User getUser() {
        return user;
    }

    public Preference getPreference() {
        return preference;
    }
}
