package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import de.unidue.palaver.R;
import de.unidue.palaver.model.Friend;

public class ChatRoomActivity extends AppCompatActivity {

    public static void startIntent(Context context, Friend friend){
        Intent intent = new Intent(context, ChatRoomActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ChatManagerActivity.startIntent(ChatRoomActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
