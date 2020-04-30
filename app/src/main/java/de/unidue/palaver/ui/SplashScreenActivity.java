package de.unidue.palaver.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.database.PalaverDBManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private Palaver palaver= Palaver.getInstance();

    public static void startIntent(Context context){
        Intent intent = new Intent(context, SplashScreenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        palaver.setContext(getApplicationContext());
        PalaverDBManager palaverDBManager = new PalaverDBManager(getApplicationContext());
        palaver.setDBManager(palaverDBManager);
        getSupportActionBar().hide();
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
            palaver.getUiController().openChatManagerActivity(SplashScreenActivity.this);
            SplashScreenActivity.this.finish();
        }
    }
}
