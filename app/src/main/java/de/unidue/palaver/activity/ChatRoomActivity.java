package de.unidue.palaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.viewmodel.MessageViewModel;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.adapter.MessageAdapter;

public class ChatRoomActivity extends AppCompatActivity {
    public static String TAG = ChatRoomActivity.class.getSimpleName();

    private MessageViewModel messageViewModel;
    private User user;
    private Friend friend;

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
        setContentView(R.layout.activity_chat_room);
        user = SessionManager.getSessionManagerInstance(getApplicationContext()).getUser();

        friend = (Friend) Objects.requireNonNull(getIntent().
                getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND);

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        messageViewModel.setFriend(friend);

        Objects.requireNonNull(getSupportActionBar()).setTitle(messageViewModel.getFriend().getUsername());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView messageRecycleview = findViewById(R.id.chatRoom_recycleView);
        MessageAdapter messageAdapter = new MessageAdapter(this,
                messageViewModel.getMessages().getValue());
        messageRecycleview.setAdapter(messageAdapter);
        messageViewModel.getMessages().observe(this, messages -> {
            messageAdapter.setMessages(messages);
            messageRecycleview.scrollToPosition(messageAdapter.getItemCount()-1);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecycleview.setLayoutManager(linearLayoutManager);
        messageRecycleview.setItemAnimator(new DefaultItemAnimator());
        messageRecycleview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if(bottom < oldBottom){
                messageRecycleview.post(() ->{
                    if(messageAdapter.getItemCount()!=0){
                        messageRecycleview.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                    }
                });
            }
        });

        EditText messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){

                String messageText = messageEditText.getText().toString().trim();
                Message message = new Message(friend.getUsername(),
                        user.getUserName(), friend.getUsername(),
                        messageText, new Date());

                messageAdapter.addMessage(message);
                messageViewModel.sendMessage(this, message);
                messageEditText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        ChatManagerActivity.startActivity(ChatRoomActivity.this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
    }
}
