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
import de.unidue.palaver.system.MessageViewModel;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.ListLiveData;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.uicontroller.arrayadapter.MessageAdapter;

public class ChatRoomActivity extends AppCompatActivity {
    private static boolean visibility;

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
        setContentView(R.layout.activity_message_manager);

        Friend friend = (Friend) Objects.requireNonNull(getIntent().
                getExtras()).getSerializable(StringValue.IntentKeyName.FRIEND);

        messageViewModel = new MessageViewModel(getApplication(), friend);
        final ListLiveData<Message> messageListLiveData = messageViewModel.getMessageList();
        Objects.requireNonNull(getSupportActionBar()).setTitle(messageViewModel.getFriend().getUsername());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        palaverEngine = Palaver.getInstance().getPalaverEngine();


        ListView listView = findViewById(R.id.chatRoom_listView);
        MessageAdapter messageAdapter = new MessageAdapter(this,
                R.layout.message_layout);
        listView.setAdapter(messageAdapter);

        messageListLiveData.observe(this, messages ->
                messageAdapter.override(messageListLiveData.getValue()));

        EditText messageEditText = findViewById(R.id.chatRoom_editText_message);
        Button sendButton = findViewById(R.id.chatRoom_button_send);

        sendButton.setOnClickListener(v -> {
            if (!messageEditText.getText().toString().equals("")){
                String messageText = messageEditText.getText().toString();
                messageViewModel.addMessage(messageText);
                palaverEngine.handleSendMessage(getApplicationContext(), ChatRoomActivity.this, messageViewModel, messageText);
                messageEditText.setText("");
            }
        });
    }

    public static boolean isVisibility() {
        return visibility;
    }

    @Override
    protected void onResume() {
        super.onResume();
        visibility = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    public void onBackPressed() {
        palaverEngine.handleOpenChatManagerActivityRequest(ChatRoomActivity.this);
    }
}
