package de.unidue.palaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import de.unidue.palaver.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SplashScreenActivity.class);
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
        public void run(){
            try{
                int SPLASH_TIME_OUT = 2;
                sleep(1000 * SPLASH_TIME_OUT);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            ChatManagerActivity.startActivity(SplashScreenActivity.this);
            SplashScreenActivity.this.finish();
        }
    }
}
