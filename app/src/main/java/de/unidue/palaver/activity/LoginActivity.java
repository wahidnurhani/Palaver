package de.unidue.palaver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.dialogandtoast.ErrorDialog;
import de.unidue.palaver.dialogandtoast.ProgressDialog;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.viewmodel.LoginRegisterViewModel;
import de.unidue.palaver.viewmodel.ViewModelProviderFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private EditText userNameEditText, passwordEditText;
    private ViewModelProviderFactory viewModelProviderFactory;
    private LoginRegisterViewModel loginViewModel;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

        viewModelProviderFactory = new ViewModelProviderFactory(getApplication());
        loginViewModel = new ViewModelProvider(this, viewModelProviderFactory)
                .get(LoginRegisterViewModel.class);
        loginViewModel.getLoginStatus().observe(this, aBoolean -> {
            if(progressDialog!=null){
                progressDialog.dismissDialog();
            }
            if(aBoolean.equals(true)){
                Log.i(TAG, "login success");
                SecondSplashActivity.startActivity(LoginActivity.this);
            }
        });

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_button);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(v -> {
            if(validateUserInput()){
                String username = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                User user = new User(username, password);
                loginViewModel.login(user);
                progressDialog.startDialog();
            }
        });

        toRegisterTextView.setOnClickListener(v -> {
            RegisterActivity.startActivity(LoginActivity.this);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private boolean validateUserInput() {
        if (userNameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            ErrorDialog.show(getApplication(),
                    StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginViewModel.reset();
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
    }

}
