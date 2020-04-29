package de.unidue.palaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG= ChatManagerActivity.class.getSimpleName();
    private Palaver palaver=Palaver.getInstance();

    public static void startIntent(Context context){
        Intent intent = new Intent(context, ChatManagerActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
    }
}
