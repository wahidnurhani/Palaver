package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.unidue.palaver.system.ChatsManager;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.UIManager;
import de.unidue.palaver.system.engine.PalaverEngine;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG= ChatManagerActivity.class.getSimpleName();
    private PalaverEngine palaverEngine;
    private UIManager uiManager;
    private ChatsManager chatsManager;
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
                chatsManager.search(newText);
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
            //AppPrefererence.startIntent(MainActivity.this);
        } else if(item.getItemId()==R.id.menu_addFriend){
            uiManager.openAddFriendDDialog(getApplicationContext(), ChatManagerActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        palaverEngine = Palaver.getInstance().getPalaverEngine();
        uiManager = Palaver.getInstance().getUiManager();
        chatsManager = Palaver.getInstance().getChatsManager();

        if(!SessionManager.getSessionManagerInstance(getApplicationContext()).chekLogin()){
            uiManager.openLoginActivity(ChatManagerActivity.this);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.chatManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {
            uiManager.openFriendManagerActivity(ChatManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        ListView listView = findViewById(R.id.chatManager_listView);
        chatsManager.initArrayAdapter(ChatManagerActivity.this, R.layout.chat_list_item_layout);
        listView.setAdapter(chatsManager.getChatArrayAdapter());

        listView.setOnItemClickListener((parent, view, position, id)->
                uiManager.openChat(ChatManagerActivity.this, chatsManager.getChatArrayAdapter().getItem(position)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatsManager.updateChatList();
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
