package de.unidue.palaver.notificationsmanager;

public interface INotificationManager {
    void createNotificationChannel();
    void displayNotification(String sender, String preview);
}
