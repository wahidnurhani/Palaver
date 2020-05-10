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

import de.unidue.palaver.R;
import de.unidue.palaver.sessionmanager.SessionManager;
import de.unidue.palaver.viewmodel.ChatsViewModel;
import de.unidue.palaver.activity.adapter.ChatAdapter;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG = ChatManagerActivity.class.getSimpleName();

    private ChatsViewModel chatsViewModel;
    private ChatAdapter chatAdapter;

    private static boolean visibility;

    public static boolean isVisibility() {
        return visibility;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
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
                chatAdapter.setItems(chatsViewModel.search(newText));
                chatAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.menu_logout){
            chatsViewModel.handleLogoutRequest();
            LoginActivity.startActivity(ChatManagerActivity.this);
        } else if(item.getItemId()==R.id.menu_setting){
            SettingsActivity.startActivity(ChatManagerActivity.this);
        } else if(item.getItemId()==R.id.menu_addFriend){
            AddFriendDialog.startDialog(getApplicationContext(), this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);

        if(!SessionManager.getSessionManagerInstance(getApplicationContext()).checkLogin()){
            if(chatsViewModel!=null){
                chatsViewModel.handleLogoutRequest();
            }
            LoginActivity.startActivity(ChatManagerActivity.this);
        }

        chatsViewModel = ViewModelProviders.of(this).get(ChatsViewModel.class);

        FloatingActionButton floatingActionButton = findViewById(R.id.chatManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {

            FriendManagerActivity.startActivity(ChatManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        RecyclerView chatsRecyclerView = findViewById(R.id.chat_recycleView);
        chatAdapter = new ChatAdapter(this, chatsViewModel.getChats().getValue());
        chatsRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatsRecyclerView.setLayoutManager(linearLayoutManager);
        chatsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                chatsRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        chatsRecyclerView.addItemDecoration(dividerItemDecoration);

        chatsViewModel.getChats().observe(this, chats -> chatAdapter.setItems(chats));

    }

    @Override
    protected void onResume() {
        super.onResume();
        visibility=true;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
