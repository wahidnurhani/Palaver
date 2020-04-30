package de.unidue.palaver.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.SessionManager;
import de.unidue.palaver.engine.Communicator;
import de.unidue.palaver.service.ServiceAddFriend;

public class AddFriendDialog extends AlertDialog.Builder {
    private Palaver palaver = Palaver.getInstance();
    private Communicator communicator = palaver.getPalaverEngine().getCommunicator();
    private SessionManager sessionManager;
    private EditText userNameEditText;

    public AddFriendDialog(Context appContext, Context context) {
        super(context);
        setTitle("Add Friend");
        sessionManager = SessionManager.getSessionManagerInstance(appContext);
        initElement();
    }

    private void initElement() {
        userNameEditText = new EditText(getContext());
        userNameEditText.setHint("username");
        userNameEditText.setPadding(55,50,10,30);
        userNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        userNameEditText.setSingleLine(true);
        userNameEditText.setMaxLines(1);

        setNegativeButton("CLOSE", null);
        setPositiveButton("ADD", null);
        setView(userNameEditText);

        AlertDialog mAlertDialog = this.create();
        mAlertDialog.setOnShowListener(dialog -> {
            Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(view -> {
                if(userNameEditText.getText().toString().equals("")){
                    palaver.getUiController().showToast(getContext(), "The Username cannot be blank");
                }
                else{
                    if(communicator.checkConnectivity(getContext())) {
                        if (userNameEditText.getText().toString().equals(sessionManager.getUser().getUserData().getUserName()))
                            palaver.getUiController().showToast(getContext(), "you can't add your own account");
                        else {
                            Intent intent = new Intent(getContext(), ServiceAddFriend.class);
                            intent.putExtra("INTENT_FRIEND_USERNAME", userNameEditText.getText().toString().trim());
                            getContext().startService(intent);
                            dialog.dismiss();
                        }
                    }else{
                        palaver.getUiController().showToast(getContext(), "No Internet Connection");
                    }
                }
            });
        });
        mAlertDialog.show();
    }
}
