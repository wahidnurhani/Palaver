package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.unidue.palaver.R;
import de.unidue.palaver.system.engine.SessionManager;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.viewmodel.ChatsViewModel;
import de.unidue.palaver.system.viewmodel.ListLiveData;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG = ChatManagerActivity.class.getSimpleName();

    private PalaverEngine palaverEngine;
    private ChatsViewModel chatsViewModel;
    private ChatAdapter chatAdapter;

    private static boolean visibility;

    public static boolean isVisibility() {
        return visibility;
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
                List<Chat> searchedList= chatsViewModel.search(newText);
                chatAdapter.overrade(searchedList);
                chatAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.menu_logout){
            palaverEngine.handleLogoutRequest(getApplicationContext());
        } else if(item.getItemId()==R.id.menu_setting){
            palaverEngine.handleOpenSettingRequest(getApplicationContext(), ChatManagerActivity.this);
        } else if(item.getItemId()==R.id.menu_addFriend){
            palaverEngine.handleOpenAddFriendDialogRequest(getApplicationContext(), ChatManagerActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        palaverEngine = PalaverEngine.getPalaverEngineInstance();

        if(!SessionManager.getSessionManagerInstance(getApplicationContext()).chekLogin()){
            palaverEngine.handleLogoutRequest(ChatManagerActivity.this);
        }

        chatsViewModel = ViewModelProviders.of(this).get(ChatsViewModel.class);
        final ListLiveData<Chat> chatListLiveData = chatsViewModel.getChatListLiveData();

        FloatingActionButton floatingActionButton = findViewById(R.id.chatManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {

            palaverEngine.handleOpenFriendManagerActivityRequest(ChatManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        RecyclerView chatsRecyclerView = findViewById(R.id.chat_recycleView);

        chatAdapter = new ChatAdapter(this, chatListLiveData.getValue());
        chatsRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatsRecyclerView.setLayoutManager(linearLayoutManager);
        chatsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                chatsRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        chatsRecyclerView.addItemDecoration(dividerItemDecoration);

        chatListLiveData.observe(this, chats -> chatAdapter.notifyDataSetChanged());

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
