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

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.service.ServiceLogin;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG= LoginActivity.class.getSimpleName();
    private static boolean visibility;

    private EditText userNameEditText, passwordEditText;

    private BroadcastReceiver loginResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SplashScreenActivity.startActivity(LoginActivity.this);
        }
    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        LocalBroadcastManager.getInstance(this).registerReceiver(loginResultReceiver,
                new IntentFilter(StringValue.IntentAction.BROADCAST_LOGINRESULT));

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_button);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(v -> {
            if(validateUserInput()){
                String username = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                ServiceLogin.startIntent(getApplicationContext(),
                        this, username, password);
            }
        });

        toRegisterTextView.setOnClickListener(v -> {
            RegisterActivity.startActivity(LoginActivity.this);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private boolean validateUserInput() {
        if (userNameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            ErrorDialog.show(LoginActivity.this,
                    StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        }
        return true;
    }

    public static boolean isVisibility() {
        return visibility;
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
        LocalBroadcastManager.getInstance(this).
                unregisterReceiver(loginResultReceiver);
    }
}
