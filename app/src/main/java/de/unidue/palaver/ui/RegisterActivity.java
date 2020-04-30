package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.UIController;
import de.unidue.palaver.engine.PalaverEngine;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.UserData;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG= RegisterActivity.class.getSimpleName();
    private static boolean visibility;
    Palaver palaver = Palaver.getInstance();
    PalaverEngine palaverEngine = palaver.getPalaverEngine();

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;

    public static void startIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.register_register_button);
        userNameEditText = findViewById(R.id.register_userName_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        rePasswordEditText = findViewById(R.id.register_repassword_editText);
        TextView backToLoginTextView = findViewById(R.id.register_backToLogin_textView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                if(validateUserInput()){
                    User user = new User(new UserData(userNameEditText.getText().toString(),
                            passwordEditText.getText().toString()));
                    palaverEngine.handleRegisterRequest(RegisterActivity.this, user);
                }
            }
        });

        backToLoginTextView.setOnClickListener(v -> {
            LoginActivity.startIntent(RegisterActivity.this);
            overridePendingTransition(0,0);
        });
    }

    private boolean validateUserInput() {
        String username = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();
        UIController uiController = palaver.getUiController();
        if (username.equals("") || password.equals("") || rePassword.equals("")){
            uiController.showErrorDialog(RegisterActivity.this, "The Username and Password cannot be blank");
            return false;
        } else if(!validString(username)){
            uiController.showErrorDialog(RegisterActivity.this, "Please input valid username-format");
            return false;
        }else if (!password.equals(rePassword)) {
            uiController.showErrorDialog(RegisterActivity.this, "Password don't match each other");
            return false;
        }
        return true;
    }

    private boolean validString(String username) {
        char[] c = username.toCharArray();

        if (Character.isDigit(c[0])) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        visibility = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visibility=false;
    }
}
