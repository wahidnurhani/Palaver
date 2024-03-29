package de.unidue.palaver.serviceandworker;
import android.app.Notification;
import android.app.NotificationChannel;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.httpclient.IHttpClient;
import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.httpclient.RetrofitHttpClient;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StackApiResponseList;

import de.unidue.palaver.model.User;
import de.unidue.palaver.notificationsmanager.NotificationManager;
import de.unidue.palaver.repository.MessageRepository;
import de.unidue.palaver.sessionmanager.PreferenceManager;
import de.unidue.palaver.sessionmanager.SessionManager;
import retrofit2.Response;

import static java.lang.Thread.sleep;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG= FirebaseMessagingService.class.getSimpleName();
    private PreferenceManager preferenceManager;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG, "Refreshed token: " + s);
        JSONBuilder.PushToken body=null;
        try{
            User user = SessionManager.getSessionManagerInstance(getApplication()).getUser();
            body = new JSONBuilder.PushToken(user, s);
        }catch (Exception e){
            body = null;
        } finally {
            if(body!=null){
                JSONBuilder.PushToken finalBody = body;
                Thread thread = new Thread(() -> {
                    IHttpClient retrofitHttpClient = new RetrofitHttpClient();

                    try {
                        Response<StackApiResponseList<String>> response = retrofitHttpClient.pushToken(finalBody);
                        Log.i(TAG, response.message()+" : "+response.body().getInfo());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        }
    }

    public void createNotificationChannel() {
        Log.i(TAG, "notification channel created");

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(NotificationManager.CHANNEL_ID,
                    NotificationManager.CHANNEL_NAME, android.app.NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription(NotificationManager.CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        User user;
        try{
            user = SessionManager.getSessionManagerInstance(getApplication()).getUser();
        }catch (Exception e){
            user = null;
        }

        createNotificationChannel();
        Log.i(TAG, "message received ");

        Map data = remoteMessage.getData();
        final String sender = (String) data.get("sender");
        final String preview = (String) data.get("preview");

        if(user!=null){
            MessageRepository messageRepository = new MessageRepository(getApplicationContext(), new Friend(sender));
            messageRepository.fetchMessageOffset(getApplicationContext(), user, new Friend(sender));
            notifyClient(sender, preview);
        }
    }

    public void notifyClient(String sender, String preview) {
        if(ChatRoomActivity.getFriend()!=null){
            if(!ChatRoomActivity.isVisible() || !ChatRoomActivity.getFriend().getUsername().equals(sender)){
                preferenceManager = SessionManager
                        .getSessionManagerInstance(getApplication()).getPreferenceManager();
                Thread thread = new Thread(() -> {
                    try{
                        sleep(0);
                        if(preferenceManager.getAllowNotificationPreference()){
                            NotificationManager.getInstance(getApplicationContext()).displayNotification(sender, preview);
                        }
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } else {
            preferenceManager = SessionManager
                    .getSessionManagerInstance(getApplication()).getPreferenceManager();
            Thread thread = new Thread(() -> {
                try{
                    sleep(0);
                    if(preferenceManager.getAllowNotificationPreference()){
                        NotificationManager.getInstance(getApplicationContext()).displayNotification(sender, preview);
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            });
            thread.start();


        }
    }
}
