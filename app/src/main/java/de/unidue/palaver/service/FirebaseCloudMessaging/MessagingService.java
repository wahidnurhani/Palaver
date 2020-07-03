package de.unidue.palaver.service.FirebaseCloudMessaging;

import android.app.NotificationChannel;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.httpclient.JSONBuilder;
import de.unidue.palaver.httpclient.Retrofit;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.notificationsmanager.NotificationManager;
import de.unidue.palaver.repository.MessageRepository;
import de.unidue.palaver.sessionmanager.SessionManager;
import retrofit2.Response;

import static java.lang.Thread.sleep;


public class MessagingService extends FirebaseMessagingService {

    private static final String TAG= MessagingService.class.getSimpleName();
    private SessionManager sessionManager;

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
                    Retrofit retrofit = new Retrofit();

                    try {
                        Response<StackApiResponseList<String>> response = retrofit.pushToken(finalBody);
                        Log.i(TAG, response.message()+" : "+response.body().getInfo());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
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
        Log.i(TAG, "data sender:"+sender);

        if(user!=null){
            MessageRepository messageRepository = new MessageRepository(getApplication());
            messageRepository.fetchMessageOffset(getApplication(), user, new Friend(sender));

            notifyClient(sender, preview);
        }
    }

    private void createNotificationChannel() {
        Log.i(TAG, "notification channel created");

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(FirebaseConstant.CHANNEL_ID, FirebaseConstant.CHANNEL_NAME, android.app.NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription(FirebaseConstant.CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void notifyClient(String sender, String preview) {
        if(ChatRoomActivity.isVisible() && ChatRoomActivity.getFriend().getUsername().equals(sender)){
            Intent intent2 = new Intent(StringValue.IntentAction.BROADCAST_MESSAGE_RECEIVED);
            intent2.putExtra("INTENT_SENDER_USERNAME", sender);
            LocalBroadcastManager.getInstance(MessagingService.this).sendBroadcast(intent2);
        }else{
            Intent intent = new Intent(StringValue.IntentAction.BROADCAST_MESSAGE_RECEIVED);
            intent.putExtra("INTENT_SENDER_USERNAME", sender);
            LocalBroadcastManager.getInstance(MessagingService.this).sendBroadcast(intent);
            sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
            Thread thread = new Thread(() -> {
                try{
                    sleep(0);
                    if(sessionManager.getAllowNotificationPreference()){
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
