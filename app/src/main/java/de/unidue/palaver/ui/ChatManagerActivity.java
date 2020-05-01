package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.SessionManager;

public class ChatManagerActivity extends AppCompatActivity {
    private static final String TAG= ChatManagerActivity.class.getSimpleName();
    private Palaver palaver=Palaver.getInstance();
    private static boolean visibility;


    public static void startIntent(Context context){
        Intent intent = new Intent(context, ChatManagerActivity.class);
        context.startActivity(intent);
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
            palaver.getUiController().openAddFriendDDialog(getApplicationContext(), ChatManagerActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);

        SessionManager.getSessionManagerInstance(getApplicationContext()).chekLogin();

        FloatingActionButton floatingActionButton = findViewById(R.id.chatManager_addChatFloatingButton);
        floatingActionButton.setOnClickListener(v -> {
            palaver.getFriendManager().openFriendListActivity(ChatManagerActivity.this);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
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
