package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import de.unidue.palaver.R;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.viewmodel.FriendViewModel;

public class AddFriendDialog extends CustomDialog{

    private FriendViewModel friendViewModel;
    private EditText userNameEditText;
    @SuppressLint("StaticFieldLeak")
    private static AddFriendDialog addFriendDialogInstance;

    private AddFriendDialog(Context applicationContext, Activity activity, FriendViewModel friendViewModel) {
        super(applicationContext, activity);
        this.friendViewModel = friendViewModel;
    }

    public static void startDialog(Context applicationContext, Activity activity, FriendViewModel friendViewModel) {
        addFriendDialogInstance = new AddFriendDialog(applicationContext, activity, friendViewModel);
        addFriendDialogInstance.startDialog();
    }

    private void startDialog(){
        init(R.layout.dialog_add_friend);

        userNameEditText = getView().findViewById(R.id.addFriend_editText);
        userNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        userNameEditText.setSingleLine(true);
        userNameEditText.setMaxLines(1);

        Button addButton = getView().findViewById(R.id.addFriend_addButton);
        Button closeButton = getView().findViewById(R.id.addFriend_closeButton);

        addButton.setOnClickListener(v -> {
            if(inputValid()){
                String username = userNameEditText.getText().toString().trim();
                friendViewModel.addFriend(username);
                dismiss();
            }
        });

        closeButton.setOnClickListener(v -> dismiss());
        show();
    }

    private boolean inputValid() {
        String username = userNameEditText.getText().toString().trim();
        if(username.equals("") || userNameEditText.getText()==null) {
            CustomToast.makeText(getApplicationContext(), StringValue.ErrorMessage.USERNAME_BLANK);
            return false;
        } else if (username.equals(friendViewModel.getUser().getUserName())){
            CustomToast.makeText(getApplicationContext(), StringValue.ErrorMessage.ADD_OWN_ACCOUNT);
            return false;
        }
        return true;
    }

}
