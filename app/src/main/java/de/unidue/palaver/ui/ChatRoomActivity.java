package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Date;
import java.util.Objects;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.UIManager;
import de.unidue.palaver.system.MessageManager;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;
import de.unidue.palaver.system.resource.StringValue;

public class ChatRoomActivity extends AppCompatActivity {
    private static boolean visibility;

    private Palaver palaver = Palaver.getInstance();
    private UIManager uiManager;
    private MessageManager messageManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
           uiManager.openChatManagerActivity(ChatRoomActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_manager);
        uiManager = palaver.getUiManager();
        messageManager = (MessageManager) Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND));

        Objects.requireNonNull(getSupportActionBar()).setTitle(messageManager.getFriend().getUsername());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView listView = findViewById(R.id.chatRoom_listView);
        messageManager.initArrayAdapter(ChatRoomActivity.this, R.layout.message_layout);
        listView.setAdapter(messageManager.getMessageAdapter());

        EditText messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){
                String message = messageEditText.getText().toString();
                Message chatItem = new Message(SessionManager.getSessionManagerInstance(getApplicationContext()).getUser().getUserData().getUserName(),
                        messageManager.getFriend().getUsername(), MessageType.OUT, message, "true", new Date());
                messageManager.sendMessage(getApplicationContext(), chatItem);
                messageEditText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageManager.updateChat();
        visibility = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    public void onBackPressed() {
        uiManager.openChatManagerActivity(ChatRoomActivity.this);
    }
}
