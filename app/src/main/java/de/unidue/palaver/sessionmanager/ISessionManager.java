package de.unidue.palaver.sessionmanager;

import de.unidue.palaver.model.User;

public interface ISessionManager {
    void register(User user);

    void login(User user);

    void startSession(String userName, String password);

    void populateDB();

    void changePassword(String newPassword);

    void endSession();

    void cleanData();
}
