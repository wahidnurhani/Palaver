package de.unidue.palaver.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.system.httpclient.Retrofit;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.service.ServicePopulateDB;
import de.unidue.palaver.system.sessionmanager.SessionManager;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private EditText userNameEditText, passwordEditText;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this);

        Button loginButton = findViewById(R.id.login_login_button);
        TextView toRegisterTextView = findViewById(R.id.login_register_button);

        userNameEditText = findViewById(R.id.login_userName_editText);
        passwordEditText = findViewById(R.id.login_password_editText);
        loginButton.setOnClickListener(v -> {
            if(validateUserInput()){
                String username = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                User user = new User(username, password);
                new LoginProcessor().execute(user);
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
            ErrorDialog.show(LoginActivity.this,
                    StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
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
    }


    @SuppressLint("StaticFieldLeak")
    private class LoginProcessor extends AsyncTask<User, Void, Response<StackApiResponseList<String>>> {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param users The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Response<StackApiResponseList<String>> doInBackground(User... users) {

            Retrofit retrofit = new Retrofit();

            Response<StackApiResponseList<String>> responseAuthenticate = null;
            sessionManager = SessionManager.getSessionManagerInstance(getApplicationContext());
            try {

                responseAuthenticate = retrofit.authenticate(users[0]);

                assert responseAuthenticate.body() != null;
                if(responseAuthenticate.body().getMessageType()==1){
                    sessionManager.startSession(users[0].getUserName(), users[0].getPassword());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseAuthenticate;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> dataServerResponseResponse) {
            if(dataServerResponseResponse!= null){
                assert dataServerResponseResponse.body() != null;
                if(dataServerResponseResponse.body().getMessageType()!=1){
                    progressDialog.dismissDialog();
                    CustomToast.makeText(getApplicationContext(),
                            dataServerResponseResponse.body().getInfo());
                } else {
                    User user = sessionManager.getUser();
                    progressDialog.dismissDialog();
                    ServicePopulateDB.startIntent(LoginActivity.this , user);
                    SplashScreenActivity.startActivity(LoginActivity.this);
                }
            } else {
                progressDialog.dismissDialog();
                CustomToast.makeText(getApplicationContext(),
                        "Connection failed");
            }
            finish();
        }
    }
}
