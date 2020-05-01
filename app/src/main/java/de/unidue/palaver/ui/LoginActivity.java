package de.unidue.palaver.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.StringValue;
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

            }
        }
    };

    public static void startIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        LocalBroadcastManager.getInstance(this).registerReceiver(authentificationMessageReceiver,
                new IntentFilter(StringValue.IntentAction.BROADCAST_AUTHENTIFICATED));

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_textView);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(v -> {
            if(validateUserInput()){
                User user = new User(new UserData(userNameEditText.getText().toString(),
                        passwordEditText.getText().toString()));
                palaverEngine.handleLoginRequest(LoginActivity.this, user);
            }
        });

        toRegisterTextView.setOnClickListener(v -> {
            RegisterActivity.startIntent(LoginActivity.this);
            overridePendingTransition(0,0);
        });
    }

    private boolean validateUserInput() {
        if (userNameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            uiController.showErrorDialog(LoginActivity.this, StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
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
