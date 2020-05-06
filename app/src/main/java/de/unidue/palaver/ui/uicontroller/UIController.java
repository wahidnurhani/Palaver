package de.unidue.palaver.ui.uicontroller;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.ui.AddFriendDialog;
import de.unidue.palaver.ui.ChatManagerActivity;
import de.unidue.palaver.ui.ChatRoomActivity;
import de.unidue.palaver.ui.FriendManagerActivity;
import de.unidue.palaver.ui.LoginActivity;
import de.unidue.palaver.ui.RegisterActivity;
import de.unidue.palaver.ui.SettingsActivity;
import de.unidue.palaver.ui.SplashScreenActivity;

public class UIController {

    public UIController() {
    }

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setNegativeButton(StringValue.TextAndLabel.CLOSE,
                (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void openAddFriendDDialog(Context applicationContext, Activity activity) {
        AddFriendDialog addFriendDialog = new AddFriendDialog(applicationContext, activity);
        addFriendDialog.startDialog();
    }

    public void openChatManagerActivity(Context context) {
        Intent intent = new Intent(context, ChatManagerActivity.class);
        context.startActivity(intent);
    }

    public void openFriendManagerActivity(Context context) {
        Intent intent = new Intent(context, FriendManagerActivity.class);
        context.startActivity(intent);
    }

    public void openLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void openRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public void openSplashScreenActivity(Context context) {
        Intent intent = new Intent(context, SplashScreenActivity.class);
        context.startActivity(intent);
    }

    public void openChatRoom(Context context, Friend friend) {
        Intent intent = new Intent(context, ChatRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StringValue.IntentKeyName.FRIEND, friend);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void openSettingActivity(Context applicationContext, Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
