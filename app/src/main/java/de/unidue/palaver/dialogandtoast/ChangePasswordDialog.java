package de.unidue.palaver.dialogandtoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import de.unidue.palaver.R;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.sessionmanager.SessionManager;

public class ChangePasswordDialog extends CustomDialog {

    private SessionManager sessionManager;
    private EditText newPasswordEditText;
    private EditText rePasswordEditText;
    @SuppressLint("StaticFieldLeak")
    private static ChangePasswordDialog changePasswordDialogInstance;

    public ChangePasswordDialog(Application application, Activity activity) {
        super(application, activity);
        this.sessionManager = SessionManager.getSessionManagerInstance( getApplication());
    }

    public static void startDialog(Application application, Activity activity){
        changePasswordDialogInstance = new ChangePasswordDialog(application, activity);
        changePasswordDialogInstance.initAndShowDialog();
    }

    @Override
    protected void initAndShowDialog() {
        inflateLayout(R.layout.dialog_change_password);

        newPasswordEditText = getView().findViewById(R.id.changePassword_editText);
        newPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        newPasswordEditText.setSingleLine(true);
        newPasswordEditText.setMaxLines(1);

        rePasswordEditText = getView().findViewById(R.id.re_inputPassword_editText);
        rePasswordEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        rePasswordEditText.setSingleLine(true);
        rePasswordEditText.setMaxLines(1);

        Button changePasswordButton = getView().findViewById(R.id.changePassword_changeButton);
        Button closeButton = getView().findViewById(R.id.changePassword_closeButton);

        changePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            if(validateUserInput()){
                sessionManager.changePassword(newPassword);
                dismiss();
            }
        });

        closeButton.setOnClickListener(v -> dismiss());
        show();
    }

    private boolean validateUserInput() {

        String newPassword = newPasswordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();
        if ( newPassword.equals("") || rePassword.equals("")){
            CustomToast.makeText(getApplication(), StringValue.ErrorMessage.USERNAME_PASSWORD_BLANK);
            return false;
        }else if (!newPassword.equals(rePassword)) {
            CustomToast.makeText(getApplication(), StringValue.ErrorMessage.PASSWORD_DON_T_MATCH_EACH_OTHER);
            return false;
        }
        return true;
    }


}
