package de.unidue.palaver.serviceandworker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;


public class WorkerPushToken extends Worker {
    private static String TAG = WorkerPushToken.class.getSimpleName();

    public WorkerPushToken(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Token will be generated and pushed");

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            Log.i(TAG, "Token generated: "+task.getResult().getToken());
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.onNewToken(Objects.requireNonNull(task.getResult()).getToken());
        });

        return Result.success();
    }
}
