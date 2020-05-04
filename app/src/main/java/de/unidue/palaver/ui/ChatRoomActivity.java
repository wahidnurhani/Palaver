package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.Objects;

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.uicontroller.UIController;
import de.unidue.palaver.system.ChatRoomManager;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.resource.StringValue;

public class ChatRoomActivity extends AppCompatActivity {
    private static boolean visibility;

    private UIController uiController;
    private PalaverEngine palaverEngine;
    private ChatRoomManager chatRoomManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
           uiController.openChatManagerActivity(ChatRoomActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_manager);
        uiController = Palaver.getInstance().getUiController();
        palaverEngine = Palaver.getInstance().getPalaverEngine();
        chatRoomManager = (ChatRoomManager) Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND));

        Objects.requireNonNull(getSupportActionBar()).setTitle(chatRoomManager.getFriend().getUsername());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView listView = findViewById(R.id.chatRoom_listView);
        chatRoomManager.initArrayAdapter(ChatRoomActivity.this, R.layout.message_layout);
        listView.setAdapter(chatRoomManager.getMessageAdapter());

        EditText messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){
                String messageText = messageEditText.getText().toString();
                palaverEngine.handleSendMessage(getApplicationContext(), ChatRoomActivity.this, chatRoomManager, messageText);
                messageEditText.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatRoomManager.updateChat();
        visibility = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    public void onBackPressed() {
        uiController.openChatManagerActivity(ChatRoomActivity.this);
    }
}
