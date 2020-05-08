package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.system.viewmodel.MessageViewModel;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.viewmodel.ListLiveData;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.values.StringValue;

public class ChatRoomActivity extends AppCompatActivity {
    public static String TAG = ChatRoomActivity.class.getSimpleName();

    private PalaverEngine palaverEngine;
    private MessageViewModel messageViewModel;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            palaverEngine.handleOpenChatManagerActivityRequest(ChatRoomActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Friend friend = (Friend) Objects.requireNonNull(getIntent().
                getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND);

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        messageViewModel.setFriend(friend);
        final ListLiveData<Message> messageListLiveData = messageViewModel.getMessageList();

        Objects.requireNonNull(getSupportActionBar()).setTitle(messageViewModel.getFriend().getUsername());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        palaverEngine = PalaverEngine.getPalaverEngineInstance();

        RecyclerView messageRecycleview = findViewById(R.id.chatRoom_recycleView);
        MessageAdapter messageAdapter = new MessageAdapter(this,
                messageListLiveData.getValue());
        messageRecycleview.setAdapter(messageAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecycleview.setLayoutManager(linearLayoutManager);

        messageRecycleview.setItemAnimator(new DefaultItemAnimator());

        EditText messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){
                String messageText = messageEditText.getText().toString().trim();
                messageViewModel.addMessage(this, messageText);
                messageEditText.setText("");
            }
        });

        messageRecycleview.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if(bottom < oldBottom){
                messageRecycleview.post(() ->{
                        if(messageAdapter.getItemCount()!=0){
                            messageRecycleview.smoothScrollToPosition(messageAdapter.getItemCount()-1);
                        }
                });
            }
        });
        messageListLiveData.observe(this, messages -> {
            messageAdapter.setMessages(messages);
            messageRecycleview.scrollToPosition(messages.size()-1);
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
        palaverEngine.handleOpenChatManagerActivityRequest(ChatRoomActivity.this);
    }
}
