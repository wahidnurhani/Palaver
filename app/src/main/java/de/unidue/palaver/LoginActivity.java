package de.unidue.palaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import de.unidue.palaver.engine.Communicator;
import de.unidue.palaver.engine.PalaverEngine;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.UserData;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG= LoginActivity.class.getSimpleName();
    private static boolean visibility;

    private EditText userNameEditText, passwordEditText;
    private Palaver palaver=Palaver.getInstance();
    private PalaverEngine palaverEngine = palaver.getPalaverEngine();

    public static void startIntent(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

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
            palaver.getUiController().showErrorDialog(LoginActivity.this, "The Username and Password cannot be blank");
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
}
