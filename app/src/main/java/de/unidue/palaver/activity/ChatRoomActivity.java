package de.unidue.palaver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.util.Date;
import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.activity.resultreceiver.FileResultReceiver;
import de.unidue.palaver.activity.resultreceiver.LocationResultReceiver;
import de.unidue.palaver.dialogandtoast.ExtrasDialog;
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
    private ResultReceiver locationResultReceiver, fileResultReceiver;
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

        user = SessionManager.getSessionManagerInstance(getApplication()).getUser();

        friend = (Friend) Objects.requireNonNull(getIntent().
                getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND);

        viewModelProviderFactory = new ViewModelProviderFactory(getApplication(), this, friend);
        messageViewModel = new ViewModelProvider(this,
                viewModelProviderFactory).get(MessageViewModel.class);

        locationResultReceiver = new LocationResultReceiver(
                getApplication(),
                ChatRoomActivity.this,
                messageViewModel,
                new Handler());

        fileResultReceiver = new FileResultReceiver(
                getApplication(),
                ChatRoomActivity.this,
                messageViewModel,
                new Handler());

        Objects.requireNonNull(getSupportActionBar()).setTitle(messageViewModel.getFriend().getUsername());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView messageRecyclerview = findViewById(R.id.chatRoom_recycleView);
        MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(),this,
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

        sendExtras.setOnClickListener(v -> ExtrasDialog.startDialog(getApplicationContext(), ChatRoomActivity.this, messageViewModel, locationResultReceiver));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ExtrasDialog.FILE_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            final Uri fileUri = data.getData();
            messageViewModel.fetchData(fileResultReceiver, fileUri);
        }
    }

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
}
