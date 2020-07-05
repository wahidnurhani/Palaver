package de.unidue.palaver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.dialogandtoast.ExtrasDialog;
import de.unidue.palaver.dialogandtoast.SendLocationDialog;
import de.unidue.palaver.model.PalaverLocation;
import de.unidue.palaver.serviceandworker.locationservice.LocationServiceConstant;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.viewmodel.MessageViewModel;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.adapter.MessageAdapter;
import de.unidue.palaver.viewmodel.ViewModelProviderFactory;

public class ChatRoomActivity extends AppCompatActivity {
    private static String TAG = ChatRoomActivity.class.getSimpleName();
    private static boolean visible;

    private ViewModelProviderFactory viewModelProviderFactory;
    private MessageViewModel messageViewModel;
    private EditText messageEditText;
    private ResultReceiver addressResultReceiver;
    private ResultReceiver locationResultReceiver;
    private User user;
    private static Friend friend;
    public static boolean isVisible() {
        return visible;
    }
    public static Friend getFriend(){
        return friend;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ChatManagerActivity.startActivity(ChatRoomActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visible=false;
        setContentView(R.layout.activity_chat_room);

        addressResultReceiver = new AddressResultReceiver(new Handler());

        user = SessionManager.getSessionManagerInstance(getApplication()).getUser();

        friend = (Friend) Objects.requireNonNull(getIntent().
                getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND);

        viewModelProviderFactory = new ViewModelProviderFactory(getApplication(), this, friend);
        messageViewModel = new ViewModelProvider(this,
                viewModelProviderFactory).get(MessageViewModel.class);

        locationResultReceiver = new GetLocationResultReceiver(
                getApplication(),
                ChatRoomActivity.this,
                messageViewModel,
                new Handler());

        Objects.requireNonNull(getSupportActionBar()).setTitle(messageViewModel.getFriend().getUsername());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView messageRecyclerview = findViewById(R.id.chatRoom_recycleView);
        MessageAdapter messageAdapter = new MessageAdapter(getApplication(),
                messageViewModel.getUser(),
                messageViewModel.getMessages().getValue());
        messageRecyclerview.setAdapter(messageAdapter);
        messageViewModel.getMessages().observe(this, messages -> {
            messageAdapter.setMessages(messages);
            messageRecyclerview.scrollToPosition(messageAdapter.getItemCount()-1);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecyclerview.setLayoutManager(linearLayoutManager);
        messageRecyclerview.setItemAnimator(new DefaultItemAnimator());
        messageRecyclerview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if(bottom < oldBottom){
                messageRecyclerview.post(() ->{
                    if(messageAdapter.getItemCount()!=0){
                        messageRecyclerview.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    }
                });
            }
        });

        messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);
        ImageView sendExtras = findViewById(R.id.add_extras);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){

                String messageText = messageEditText.getText().toString().trim();
                Message message = new Message(friend.getUsername(),
                        user.getUserName(), friend.getUsername(),
                        messageText, new Date());

                messageAdapter.addMessage(message);
                messageViewModel.sendMessage(message);
                messageEditText.setText("");
            }
        });

        sendExtras.setOnClickListener(v -> ExtrasDialog.startDialog(getApplicationContext(), ChatRoomActivity.this, friend));

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastLocationRequest, new IntentFilter(StringValue.IntentAction.LOCATION_PERMITION));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "result");

        if(requestCode == ExtrasDialog.FILE_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            Uri fileUri = data.getData();
            File file = new File(fileUri.getPath());
            Log.i(TAG, "file name : "+ file.getName());

            //TODO sendFile
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        visible=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }

    @Override
    public void onBackPressed() {
        ChatManagerActivity.startActivity(ChatRoomActivity.this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
    }

    private BroadcastReceiver broadcastLocationRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            messageViewModel.fetchLocation(locationResultReceiver);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ExtrasDialog.LOCATION_REQUEST_CODE && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "location Permission granted after request");
                messageViewModel.fetchLocation(locationResultReceiver);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastLocationRequest);
    }

    private static class AddressResultReceiver extends ResultReceiver{
        String address;

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(resultCode == LocationServiceConstant.SUCCESS_RESULT){
                address = resultData.getString(LocationServiceConstant.RESULT_DATA_ADDRESS_KEY);
            }
        }
    }

    private static class GetLocationResultReceiver extends ResultReceiver{
        PalaverLocation palaverLocation;
        private Application application;
        private Activity activity;
        private MessageViewModel messageViewModel;



        public GetLocationResultReceiver(Application application, Activity activity, MessageViewModel messageViewModel, Handler handler) {
            super(handler);
            this.application = application;
            this.activity = activity;
            this.messageViewModel = messageViewModel;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            Log.i(TAG, "receiver active");

            if(resultCode == LocationServiceConstant.SUCCESS_RESULT){
                palaverLocation = (PalaverLocation) resultData.getSerializable(LocationServiceConstant.RESULT_DATA_LOCATION_KEY);
                Log.i(TAG, palaverLocation.toString());
                SendLocationDialog.startDialog(application,
                        activity, palaverLocation, messageViewModel);
            }
        }
    }
}
