package de.unidue.palaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

import de.unidue.palaver.R;
import de.unidue.palaver.sessionmanager.PreferenceManager;
import de.unidue.palaver.sessionmanager.SessionManager;

public class SecondSplashActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SecondSplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash_screen);
        SplashAction splashAction = new SplashAction();
        splashAction.start();
    }

    private class SplashAction extends Thread{
        private PreferenceManager preferenceManager;

        public SplashAction() {
            this.preferenceManager = SessionManager
                    .getSessionManagerInstance(getApplicationContext()).getPreferenceManager();
        }

        public void run(){
            try{
                Log.i(TAG, "splass run");
                int SPLASH_TIME_OUT = 2;
                sleep(1000 * SPLASH_TIME_OUT);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Log.i(TAG, preferenceManager.getAutoLoginPreference()+"");
            ChatManagerActivity.startActivity(SecondSplashActivity.this);
            SecondSplashActivity.this.finish();
        }
    }
}