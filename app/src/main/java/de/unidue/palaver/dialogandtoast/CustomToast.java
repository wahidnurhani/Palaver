package de.unidue.palaver.dialogandtoast;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class CustomToast{

    public static void makeText(Context context, CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
}
