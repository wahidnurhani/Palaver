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

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.resource.MessageType;

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint("ResourceAsColor")
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

        if (message.getMessageType()== MessageType.OUT){
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            containerLinearLayout.setLayoutParams(params);

            chatItemTextView.setTextColor(Color.BLACK);
            chatItemTextView.setBackgroundResource(R.drawable.shape_round);

        }else{
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            containerLinearLayout.setLayoutParams(params);

            chatItemTextView.setTextColor(Color.WHITE);
            chatItemTextView.setBackgroundResource(R.drawable.shape_round_message_in);
        }

        //set Text in TextView
        chatItemTextView.setText(message.getMessage() + message.getDate());
        return convertView;
    }
}
