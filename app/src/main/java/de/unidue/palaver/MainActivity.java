package de.unidue.palaver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static int SPLASH_TIME_OUT= 2;
    private Palaver palaver;

    public static void startIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        palaver = Palaver.getInstance();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        SplashAction splashAction = new SplashAction();
        splashAction.start();
    }

    private class SplashAction extends Thread{
        public void run(){
            try{
                sleep(1000 * SPLASH_TIME_OUT);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            palaver.openLoginActivity(MainActivity.this);
            MainActivity.this.finish();
        }
    }
}
