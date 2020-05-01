package de.unidue.palaver.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.R;
import de.unidue.palaver.SessionManager;
import de.unidue.palaver.UIController;

public class AddFriendDialog {

    private AlertDialog alertDialog;
    private Context applicationContext;
    private Activity activity;
    private Palaver palaver;
    private UIController uiController;
    private SessionManager sessionManager;

    private EditText userNameEditText;

    public AddFriendDialog(Context applicationContext, Activity activity) {
        this.applicationContext =applicationContext;
        this.activity = activity;
        this.palaver = Palaver.getInstance();
        this.uiController = palaver.getUiController();
        this.sessionManager = SessionManager.getSessionManagerInstance(activity);
    }

    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (activity).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_friend, null);
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

        closeButton.setOnClickListener(v -> {
            dismiss();
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean inputValid() {
        String username = userNameEditText.getText().toString().trim();
        if(username.equals("") || userNameEditText.getText()==null){
            uiController.showToast(applicationContext, "The Username cannot be blank");
            return false;
        } else if (username.equals(sessionManager.getUser().getUserData().getUserName())){
            palaver.getUiController().showToast(applicationContext, "you can't add your own account");
            return false;
        }
        return true;
    }

    private void dismiss(){
        alertDialog.dismiss();
    }


}
