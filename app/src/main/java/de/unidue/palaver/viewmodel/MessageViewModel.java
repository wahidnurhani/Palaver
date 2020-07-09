package de.unidue.palaver.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.model.User;
import de.unidue.palaver.repository.MessageRepository;
import de.unidue.palaver.serviceandworker.locationservice.LocationAndFileServiceConstant;
import de.unidue.palaver.serviceandworker.locationservice.LocationProviderService;
import de.unidue.palaver.sessionmanager.SessionManager;

public class MessageViewModel extends AndroidViewModel implements Comparable<MessageViewModel>, Serializable {
    private static final String TAG = MessageViewModel.class.getSimpleName();

    private static final String RESULT_DATA_FILE_KEY = "RESULT_DATA_FILE_KEY";
    private MessageRepository messageRepository;
    private SessionManager sessionManager;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    private User user;
    private Friend friend;
    private LiveData<List<Message>> messages;

    public MessageViewModel(Application application, Activity activity, Friend friend) {
        super(application);
        this.friend = friend;
        this.activity = activity;
        this.messageRepository = new MessageRepository(getApplication(), activity, friend);
        this.sessionManager = SessionManager.getSessionManagerInstance(application);
        this.user = sessionManager.getUser();
        this.messages = messageRepository.getLiveData();
    }

    public Friend getFriend() {
        return friend;
    }

    public User getUser() {
        return user;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public void sendMessage(Message message) {
         messageRepository.add(message);
    }

    public void fetchLocation(ResultReceiver locationResultReceiver) {
        Intent intent = new Intent(activity, LocationProviderService.class);
        intent.putExtra(LocationAndFileServiceConstant.RECEIVER_KEY, locationResultReceiver);
        activity.startService(intent);
    }

    public void fetchData(ResultReceiver fileResultReceiver, Uri fileUri) {
        String filePath = fileUri.getPath();
        File file = new File(filePath);

        if (file != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(RESULT_DATA_FILE_KEY, file);
            fileResultReceiver.send(LocationAndFileServiceConstant.SUCCESS_RESULT, bundle);
        }
    }

    public static String getBinaryBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 50];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length,
                    Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    @Override
    public int compareTo(MessageViewModel o) {
        return 0;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void sendFile(File file) {
        Message message = new Message(
                friend.getUsername(),
                user.getUserName(),
                friend.getUsername(),
                getBinaryBase64FromPath(file.getPath()),
                new Date()
        );
        Log.i(TAG, "Binary : "+message.getMessage());
        //messageRepository.add(message);
    }

    public void sendLocation(PalaverLocation palaverLocation) {
        Message message = new Message(
                friend.getUsername(),
                user.getUserName(),
                friend.getUsername(),
                palaverLocation.getLocationUrlString(),
                new Date()
        );
        messageRepository.add(message);
    }
}
