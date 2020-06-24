package de.unidue.palaver.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import de.unidue.palaver.service.FirebaseCloudMessaging.MessagingService;


public class PushTokenWorker extends Worker {
    private static String TAG = PushTokenWorker.class.getSimpleName();

    public PushTokenWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Token will be generated and pushed");

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            Log.i(TAG, "Token generated: "+task.getResult().getToken());
            MessagingService messagingService = new MessagingService();
            messagingService.onNewToken(Objects.requireNonNull(task.getResult()).getToken());
        });

        return Result.success();
    }
}
