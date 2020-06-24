package de.unidue.palaver.worker;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.iid.FirebaseInstanceId;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GetTokenTest {

    @Test
    public void getTokenTest(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            System.out.println(task.getResult().getToken());
        });
    }
}
