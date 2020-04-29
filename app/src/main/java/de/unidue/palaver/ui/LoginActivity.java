package de.unidue.palaver.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.UIController;
import de.unidue.palaver.engine.Communicator;
import de.unidue.palaver.engine.PalaverEngine;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.UserData;
import de.unidue.palaver.service.ServiceFetchFriend;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG= LoginActivity.class.getSimpleName();
    private static boolean visibility;

    private EditText userNameEditText, passwordEditText;
    private Palaver palaver=Palaver.getInstance();
    private PalaverEngine palaverEngine = palaver.getPalaverEngine();
    private UIController uiController = palaver.getUiController();
    private Communicator communicator = palaverEngine.getCommunicator();

    private
    BroadcastReceiver authentificationMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(communicator.checkConnectivity(getApplicationContext())){
                intent = new Intent(getApplicationContext(), ServiceFetchFriend.class);
                startService(intent);
                palaver.getChatManager().openChatListActivity(LoginActivity.this);
            }
        }
    };

    public static void startIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        LocalBroadcastManager.getInstance(this).registerReceiver(authentificationMessageReceiver,
                new IntentFilter("authentificated_broadcast"));

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_textView);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                if(validateUserInput()){
                    User user = new User(new UserData(userNameEditText.getText().toString(),
                            passwordEditText.getText().toString()));
                    palaverEngine.handleLoginRequest(LoginActivity.this, user);
                }
            }
        });

        toRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startIntent(LoginActivity.this);
            }
        });
    }

    private boolean validateUserInput() {
        if (userNameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            uiController.showErrorDialog(LoginActivity.this, "The Username and Password cannot be blank");
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        visibility =true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(authentificationMessageReceiver);
    }
}
