package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.unidue.palaver.system.ChatManager;
import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.UIManager;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG= ChatManagerActivity.class.getSimpleName();
    private Palaver palaver;
    private UIManager uiManager;
    private ChatManager chatManager;
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.menu_logout){
            palaver.getPalaverEngine().handleLogoutRequest(getApplicationContext());
        } else if(item.getItemId()==R.id.menu_setting){
            //AppPrefererence.startIntent(MainActivity.this);
        } else if(item.getItemId()==R.id.menu_addFriend){
            palaver.getUiManager().openAddFriendDDialog(getApplicationContext(), ChatManagerActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        palaver = Palaver.getInstance();
        uiManager = palaver.getUiManager();
        chatManager = palaver.getChatManager();

        if(!SessionManager.getSessionManagerInstance(getApplicationContext()).chekLogin()){
            uiManager.openLoginActivity(ChatManagerActivity.this);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.chatManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {

            uiManager.openFriendManagerActivity(ChatManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        ListView listView = findViewById(R.id.chatManager_listView);
        chatManager.initArrayAdapter(ChatManagerActivity.this, R.layout.chat_list_item_layout);
        listView.setAdapter(chatManager.getChatArrayAdapter());

        listView.setOnItemClickListener((parent, view, position, id)->
                uiManager.openChat(ChatManagerActivity.this, chatManager.getChatArrayAdapter().getItem(position)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatManager.updateChatList();
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
