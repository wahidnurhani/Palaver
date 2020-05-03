package de.unidue.palaver.ui.arrayadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;

import de.unidue.palaver.R;
import de.unidue.palaver.system.engine.Parser;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.message_layout,parent,false);
        }

        LinearLayout containerLinearLayout = convertView.findViewById(R.id.message_container);
        TextView chatItemTextView = convertView.findViewById(R.id.message_textView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)containerLinearLayout.getLayoutParams();

        assert message != null;
        if (message.getMessageType()== MessageType.OUT){
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            containerLinearLayout.setLayoutParams(params);

            chatItemTextView.setTextColor(Color.WHITE);
            containerLinearLayout.setBackgroundResource(R.drawable.shape_round_message_out);

        }else{
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            containerLinearLayout.setLayoutParams(params);

            chatItemTextView.setTextColor(Color.WHITE);
            containerLinearLayout.setBackgroundResource(R.drawable.shape_round_message_in);
        }

        chatItemTextView.setText(message.getMessage().trim());
        return convertView;
    }
}
