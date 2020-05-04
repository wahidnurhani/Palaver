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
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import de.unidue.palaver.system.FriendManager;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.engine.communicator.Communicator;
import de.unidue.palaver.system.engine.PalaverEngine;

public class FriendManagerActivity extends AppCompatActivity {
    private static boolean visibility;

    private PalaverEngine palaverEngine;
    private Communicator communicator;
    private FriendManager friendManager;

    private BroadcastReceiver friendAddeddMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String addFriendResult = intent.getCharSequenceExtra(StringValue.IntentKeyName.BROADCAST_FRIENDADDED_MESSAGE_RESULT).toString();
            if(communicator.checkConnectivity(getApplicationContext())){
                palaverEngine.handleShowToastRequest(getApplicationContext(), addFriendResult);
                friendManager.updateFriends();
            }
        }
    };

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
            palaverEngine.handleOpenChatManagerActivityRequest(FriendManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        palaverEngine = Palaver.getInstance().getPalaverEngine();
        communicator = palaverEngine.getCommunicator();
        palaverEngine = Palaver.getInstance().getPalaverEngine();
        friendManager = palaverEngine.getFriendManager();

        LocalBroadcastManager.getInstance(this).registerReceiver(friendAddeddMessageReceiver,
                new IntentFilter(StringValue.IntentAction.BROADCAST_FRIENDADDED));

        setContentView(R.layout.activity_friend_manager);

        Objects.requireNonNull(getSupportActionBar()).setTitle(StringValue.TextAndLabel.SELECT_FRIEND);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton floatingActionButton = findViewById(R.id.friendManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v ->
                palaverEngine.handleOpenAddFriendDialogRequest(getApplicationContext(),
                        FriendManagerActivity.this));

        ListView friendsListView = findViewById(R.id.friendManager_recycleView);
        friendManager.initArrayAdapter(FriendManagerActivity.this,
                R.layout.friend_list_item_layout);

        friendsListView.setAdapter(friendManager.getFriendArrayAdapter());

        friendsListView.setOnItemClickListener((parent, view, position, id) -> {
            Friend friend = friendManager.getFriendArrayAdapter().getItem(position);
            palaverEngine.handleClickOnFriend(FriendManagerActivity.this, friend);
        });
    }

    public static boolean isVisibility() {
        return visibility;
    }

    @Override
    protected void onResume() {
        super.onResume();
        visibility=true;
        friendManager.updateFriends();
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(friendAddeddMessageReceiver);
        visibility = false;
    }
}
