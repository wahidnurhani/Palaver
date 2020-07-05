package de.unidue.palaver.notificationsmanager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.sessionmanager.SessionManager;

public class NotificationManager extends Application implements INotificationManager{

    private static final String TAG= NotificationManager.class.getSimpleName();
    public static final String APP_NAME = "Palaver";

    public static final String CHANNEL_ID= "palaverChannelId";
    public static final String CHANNEL_NAME= "Palaver";
    public static final String CHANNEL_DESCRIPTION= "Messaging App";
    public static final int NotificationID=1;

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static NotificationManager notificationManagerInstance;
    private de.unidue.palaver.sessionmanager.PreferenceManager preferenceManager;

    public NotificationManager(Context context) {
        this.context = context;
        preferenceManager = SessionManager
                .getSessionManagerInstance((Application) context).getPreferenceManager();
    }

    public static NotificationManager getInstance(Context context) {
        if (notificationManagerInstance==null){
            notificationManagerInstance = new NotificationManager(context);
        }
        return notificationManagerInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void displayNotification(String sender, String preview){
        NotificationCompat.Builder notificationBuilder;

        if(preferenceManager.getAllowVibrationPreference()){
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.palaver_logo_2)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 250, 100, 250})
                    .setContentTitle(APP_NAME)
                    .setContentText(sender+" : "+preview)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.palaver_logo_2)
                    .setAutoCancel(true)
                    .setContentTitle(APP_NAME)
                    .setContentText(sender+" : "+preview)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        }


        Friend friend = new Friend(sender);
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, friend);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        boolean notificationSetting = preferenceManager.getAllowNotificationPreference();

        if(notificationManager!=null){
            if(notificationSetting){
                Log.i(TAG, "notify");
                notificationManager.notify(NotificationID, notificationBuilder.build());
            }
        }
    }
}
