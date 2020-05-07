package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import de.unidue.palaver.system.viewmodel.FriendViewModel;
import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.viewmodel.ListLiveData;
import de.unidue.palaver.system.resource.StringValue;


public class FriendManagerActivity extends AppCompatActivity {
    private static boolean visibility;

    private FriendViewModel friendViewModel;
    private FriendAdapter friendAdapter;

    private BroadcastReceiver friendAddeddMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //friendViewModel.fetchFriends();
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
                List<Friend> searchedList= friendViewModel.search(newText);
                friendAdapter.overrade(searchedList);
                friendAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            friendViewModel.OpenChatManagerActivity(FriendManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);

        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        final ListLiveData<Friend> friendsListLiveData = friendViewModel.getFriendsLiveData();


        LocalBroadcastManager.getInstance(this).registerReceiver(friendAddeddMessageReceiver,
                new IntentFilter(StringValue.IntentAction.BROADCAST_FRIENDADDED));

        Objects.requireNonNull(getSupportActionBar()).setTitle(StringValue.TextAndLabel.SELECT_FRIEND);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton floatingActionButton = findViewById(R.id.friendManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v ->
                friendViewModel.openAddFriendDialog(getApplicationContext(),
                        FriendManagerActivity.this));

        RecyclerView friendsRecycleView = findViewById(R.id.friendManager_recycleView);

        friendAdapter = new FriendAdapter(this,
                friendsListLiveData.getValue());
        friendsRecycleView.setAdapter(friendAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsRecycleView.setLayoutManager(linearLayoutManager);

        friendsRecycleView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendsRecycleView.getContext(),
                linearLayoutManager.getOrientation());
        friendsRecycleView.addItemDecoration(dividerItemDecoration);

        friendsListLiveData.observe(this, friends -> friendAdapter.notifyDataSetChanged());
    }

    public static boolean isVisibility() {
        return visibility;
    }

    @Override
    protected void onResume() {
        super.onResume();
        visibility=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(friendAddeddMessageReceiver);
        visibility = false;
    }
}