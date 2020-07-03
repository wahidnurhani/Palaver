package de.unidue.palaver.notificationsmanager;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.ChatRoomActivity;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.service.FirebaseCloudMessaging.FirebaseConstant;
import de.unidue.palaver.sessionmanager.SessionManager;

public class NotificationManager extends Application {

    private static final String TAG= NotificationManager.class.getSimpleName();

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static NotificationManager notificationManagerInstance;
    private de.unidue.palaver.sessionmanager.PreferenceManager preferenceManager;

    public NotificationManager(Context context) {
        this.context = context;
        preferenceManager = SessionManager
                .getSessionManagerInstance(context).getPreferenceManager();
    }

    public static NotificationManager getInstance(Context context) {
        if (notificationManagerInstance==null){
            notificationManagerInstance = new NotificationManager(context);
        }
        return notificationManagerInstance;
    }

    public void displayNotification(String sender, String preview){
        NotificationCompat.Builder notificationBuilder;

        if(preferenceManager.getAllowVibrationPreference()){
            notificationBuilder = new NotificationCompat.Builder(context, FirebaseConstant.CHANNEL_ID)
                    .setSmallIcon(R.drawable.palaver_logo)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0, 250, 100, 250})
                    .setContentTitle("PALAVER")
                    .setContentText(sender+" : "+preview)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context, FirebaseConstant.CHANNEL_ID)
                    .setSmallIcon(R.drawable.palaver_logo)
                    .setAutoCancel(true)
                    .setContentTitle("PALAVER")
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
                notificationManager.notify(1, notificationBuilder.build());
            }
        }
    }
}
