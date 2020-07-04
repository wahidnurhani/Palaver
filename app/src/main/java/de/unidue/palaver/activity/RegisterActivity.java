package de.unidue.palaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.dialogandtoast.ErrorDialog;
import de.unidue.palaver.dialogandtoast.ProgressDialog;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.model.User;
import de.unidue.palaver.viewmodel.LoginRegisterViewModel;
import de.unidue.palaver.viewmodel.ViewModelProviderFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG= RegisterActivity.class.getSimpleName();
    private ViewModelProviderFactory viewModelProviderFactory;
    private LoginRegisterViewModel registerViewModel;
    private ProgressDialog progressDialog;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);

        viewModelProviderFactory = new ViewModelProviderFactory(getApplication());
        registerViewModel = new ViewModelProvider(this, viewModelProviderFactory)
                .get(LoginRegisterViewModel.class);
        registerViewModel.getRegisterStatus().observe(this, aBoolean -> {
            progressDialog.dismissDialog();
            if(aBoolean.equals(true)){
                Log.i(TAG, "Register success");
                goToLogin();
            }
        });

        Button registerButton = findViewById(R.id.register_register_button);
        userNameEditText = findViewById(R.id.register_userName_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        rePasswordEditText = findViewById(R.id.register_repassword_editText);
        TextView backToLoginTextView = findViewById(R.id.register_backToLogin);

        registerButton.setOnClickListener(view -> {
            if(validateUserInput()){
                User user = new User(userNameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                registerViewModel.register(user);
                progressDialog.startDialog();

            }
        });

        backToLoginTextView.setOnClickListener(v -> {
            LoginActivity.startActivity(RegisterActivity.this);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
        });
    }

    private boolean validateUserInput() {
        String username = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();
        if (username.equals("") || password.equals("") || rePassword.equals("")){
            ErrorDialog.show(getApplication(), StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        } else if(!validString(username)){
            ErrorDialog.show(getApplication(), StringValue.ErrorMessage.PLEASE_INPUT_VALID_USERNAME_FORMAT);
            return false;
        }else if (!password.equals(rePassword)) {
            ErrorDialog.show(getApplication(), StringValue.ErrorMessage.PASSWORD_DON_T_MATCH_EACH_OTHER);
            return false;
        }
        return true;
    }

    private boolean validString(String username) {
        char[] c = username.toCharArray();
        return !Character.isDigit(c[0]);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        goToLogin();
    }

    public void goToLogin(){
        LoginActivity.startActivity(RegisterActivity.this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
    }

}
