package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.system.httpclient.Retrofit;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.model.User;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG= RegisterActivity.class.getSimpleName();
    private static boolean visibility;

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

        Button registerButton = findViewById(R.id.register_register_button);
        userNameEditText = findViewById(R.id.register_userName_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        rePasswordEditText = findViewById(R.id.register_repassword_editText);
        TextView backToLoginTextView = findViewById(R.id.register_backToLogin);

        registerButton.setOnClickListener(view -> {
            if(validateUserInput()){
                User user = new User(userNameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                new RegisterAsyncTask().execute(user);
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
            ErrorDialog.show(RegisterActivity.this, StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        } else if(!validString(username)){
            ErrorDialog.show(RegisterActivity.this, StringValue.ErrorMessage.PLEASE_INPUT_VALID_USERNAME_FORMAT);
            return false;
        }else if (!password.equals(rePassword)) {
            ErrorDialog.show(RegisterActivity.this, StringValue.ErrorMessage.PASSWORD_DON_T_MATCH_EACH_OTHER);
            return false;
        }
        return true;
    }

    private boolean validString(String username) {
        char[] c = username.toCharArray();
        return !Character.isDigit(c[0]);
    }

    public static boolean isVisibility() {
        return visibility;
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

    @Override
    public void onBackPressed() {
        LoginActivity.startActivity(RegisterActivity.this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterAsyncTask extends AsyncTask<User, Void, Response<StackApiResponseList<String>>> {
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
            Response<StackApiResponseList<String>> response = null;
            try {
                response = retrofit.register(users[0]);
                assert response.body() != null;
                if (response.body().getMessageType()==1){
                    return response;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<StackApiResponseList<String>> response) {
            if (response!= null){
                assert response.body() != null;
                if (response.body().getMessageType()==1){
                    CustomToast.makeText(getApplicationContext(), response.body().getInfo());
                    LoginActivity.startActivity(RegisterActivity.this);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigt);
                } else {
                    CustomToast.makeText(getApplicationContext(), response.body().getInfo());
                }
            } else {
                CustomToast.makeText(getApplicationContext(), "connection failed");
            }
        }
    }
}
