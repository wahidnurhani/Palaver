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

import de.unidue.palaver.system.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.UIManager;

public class AddFriendDialog {

    private AlertDialog alertDialog;
    private Context applicationContext;
    private Activity activity;
    private Palaver palaver;
    private UIManager uiManager;
    private SessionManager sessionManager;

    private EditText userNameEditText;

    public AddFriendDialog(Context applicationContext, Activity activity) {
        this.applicationContext =applicationContext;
        this.activity = activity;
        this.palaver = Palaver.getInstance();
        this.uiManager = palaver.getUiManager();
        this.sessionManager = SessionManager.getSessionManagerInstance(activity);
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
                palaver.getPalaverEngine().handleAddFriendRequest(applicationContext, activity, username);
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
            uiManager.showToast(applicationContext, StringValue.ErrorMessage.USERNAME_BLANK);
            return false;
        } else if (username.equals(sessionManager.getUser().getUserData().getUserName())){
            palaver.getUiManager().showToast(applicationContext, StringValue.ErrorMessage.ADD_OWN_ACCOUNT);
            return false;
        }
        return true;
    }

    private void dismiss(){
        alertDialog.dismiss();
    }


}
