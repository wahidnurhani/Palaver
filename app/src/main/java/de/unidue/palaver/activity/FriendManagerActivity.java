package de.unidue.palaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import de.unidue.palaver.dialogandtoast.AddFriendDialog;
import de.unidue.palaver.viewmodel.FriendViewModel;
import de.unidue.palaver.R;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.adapter.FriendAdapter;


public class FriendManagerActivity extends AppCompatActivity {

    private FriendViewModel friendViewModel;
    private FriendAdapter friendAdapter;

    public static void startActivity(Context context) {
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
                friendAdapter.setItems(friendViewModel.search(newText));
                friendAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(FriendManagerActivity.this, ChatManagerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);

        friendViewModel = ViewModelProviders.of(this).get(FriendViewModel.class);
        friendViewModel.getFriends().observe(this, friends -> friendAdapter.setItems(friends));

        Objects.requireNonNull(getSupportActionBar()).setTitle(StringValue.TextAndLabel.SELECT_FRIEND);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton floatingActionButton = findViewById(R.id.friendManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v ->
                AddFriendDialog.startDialog(getApplicationContext(), this, friendViewModel));

        RecyclerView friendsRecycleView = findViewById(R.id.friendManager_recycleView);

        friendAdapter = new FriendAdapter(this,
                friendViewModel.getFriends().getValue());
        friendsRecycleView.setAdapter(friendAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsRecycleView.setLayoutManager(linearLayoutManager);

        friendsRecycleView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendsRecycleView.getContext(),
                linearLayoutManager.getOrientation());
        friendsRecycleView.addItemDecoration(dividerItemDecoration);


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
    protected void onDestroy() {
        super.onDestroy();
    }
}