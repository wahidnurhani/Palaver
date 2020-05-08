package de.unidue.palaver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.system.values.StringValue;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG= LoginActivity.class.getSimpleName();
    private static boolean visibility;

    private EditText userNameEditText, passwordEditText;
    private PalaverEngine palaverEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
        palaverEngine = PalaverEngine.getPalaverEngineInstance();

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_button);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(v -> {
            if(validateUserInput()){
                User user = new User(userNameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                palaverEngine.handleLoginRequest(getApplicationContext(), LoginActivity.this, user);
            }
        });

        toRegisterTextView.setOnClickListener(v -> {
            palaverEngine.handleOpenRegisterActivityRequest(LoginActivity.this);
            overridePendingTransition(0,0);
        });
    }

    private boolean validateUserInput() {
        if (userNameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            palaverEngine.handleShowErrorDialogRequest(LoginActivity.this, StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
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
    }
}
