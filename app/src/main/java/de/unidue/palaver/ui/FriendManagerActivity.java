package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import de.unidue.palaver.ChatManager;
import de.unidue.palaver.FriendManager;
import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.UIController;
import de.unidue.palaver.engine.Communicator;
import de.unidue.palaver.engine.PalaverEngine;
import de.unidue.palaver.model.Chat;

public class FriendManagerActivity extends AppCompatActivity {
    private static boolean visibility;

    private UIController uiController;
    private Communicator communicator;
    private FriendManager friendManager;
    private ChatManager chatManager;

    private BroadcastReceiver friendAddeddMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String addFriendResult = intent.getCharSequenceExtra("INTENT_ADDFRIEND_RESULT").toString();
            if(communicator.checkConnectivity(getApplicationContext())){
                uiController.showToast(FriendManagerActivity.this, addFriendResult);
                friendManager.updateFriends();
            }
        }
    };


    public static void startIntent(Context context){
        Intent intent = new Intent(context, FriendManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);

        MenuItem menuItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                friendManager.search(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ChatManagerActivity.startIntent(FriendManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Palaver palaver = Palaver.getInstance();
        PalaverEngine palaverEngine = palaver.getPalaverEngine();
        uiController = palaver.getUiController();
        communicator = palaverEngine.getCommunicator();
        friendManager = palaver.getFriendManager();
        chatManager = palaver.getChatManager();

        LocalBroadcastManager.getInstance(this).registerReceiver(friendAddeddMessageReceiver,
                new IntentFilter("friendadded_broadcast"));

        setContentView(R.layout.activity_friend_manager);


        Objects.requireNonNull(getSupportActionBar()).setTitle("Select Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton floatingActionButton = findViewById(R.id.friendManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {
            friendManager.openAddFriendDialog(getApplicationContext(), FriendManagerActivity.this);
        });

        ListView friendsListView = findViewById(R.id.friendManager_recycleView);
        friendManager.initArrayAdapter(FriendManagerActivity.this,
                R.id.friend_list_item_textview,
                R.layout.friend_list_item_layout);

        friendsListView.setAdapter(friendManager.getFriendArrayAdapter());

        friendsListView.setOnItemClickListener((parent, view, position, id) -> {
            chatManager.addChat(new Chat(friendManager.getFriendArrayAdapter().getItem(position)));
            chatManager.openChat(FriendManagerActivity.this,friendManager.getFriendArrayAdapter().getItem(position));
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        visibility=true;
        friendManager.updateFriends();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(friendAddeddMessageReceiver);
        visibility = false;
    }
}
