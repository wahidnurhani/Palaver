package de.unidue.palaver.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import de.unidue.palaver.R;
import de.unidue.palaver.system.engine.SessionManager;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.system.service.ServiceAddFriend;

public class AddFriendDialog {

    private AlertDialog alertDialog;
    private Context applicationContext;
    private Activity activity;
    private SessionManager sessionManager;
    private EditText userNameEditText;
    private static AddFriendDialog addFriendDialogInstance;

    private AddFriendDialog(Context applicationContext, Activity activity) {
        this.applicationContext = applicationContext;
        this.activity = activity;
        this.sessionManager = SessionManager.getSessionManagerInstance(applicationContext);
    }

    public static void startDialog(Context applicationContext, Activity activity) {
        if(addFriendDialogInstance == null){
            addFriendDialogInstance = new AddFriendDialog(applicationContext, activity);
        }
        addFriendDialogInstance.startDialog();
    }

    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (activity).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_add_friend, null);
        builder.setView(view);
        builder.setCancelable(false);

        userNameEditText = view.findViewById(R.id.addFriend_editText);
        userNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        userNameEditText.setSingleLine(true);
        userNameEditText.setMaxLines(1);

        Button addButton = view.findViewById(R.id.addFriend_addButton);
        Button closeButton = view.findViewById(R.id.addFriend_closeButton);

        addButton.setOnClickListener(v -> {
            if(inputValid()){
                String username = userNameEditText.getText().toString().trim();
                ServiceAddFriend.startIntent(applicationContext, activity, username);
                dismiss();
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean inputValid() {
        String username = userNameEditText.getText().toString().trim();
        if(username.equals("") || userNameEditText.getText()==null){
            CustomToast.makeText(applicationContext, StringValue.ErrorMessage.USERNAME_BLANK);
            return false;
        } else if (username.equals(sessionManager.getUser().getUserName())){
            CustomToast.makeText(applicationContext, StringValue.ErrorMessage.ADD_OWN_ACCOUNT);
            return false;
        }
        return true;
    }

    private void dismiss(){
        alertDialog.dismiss();
    }


}
