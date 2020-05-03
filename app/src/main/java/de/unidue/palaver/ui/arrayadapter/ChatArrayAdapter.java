package de.unidue.palaver.ui.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.unidue.palaver.R;
import de.unidue.palaver.system.MessageManager;

public class ChatArrayAdapter extends ArrayAdapter<MessageManager> {

    public ChatArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MessageManager messageManager = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.chat_list_item_layout,parent,false);
        }

        TextView friendView = convertView.findViewById(R.id.chat_list_name);
        TextView messageView = convertView.findViewById(R.id.chat_list_message);


        assert messageManager != null;
        friendView.setText(messageManager.getFriend().getUsername());
        messageView.setText(messageManager.getLatestMessage().getMessage());

        return convertView;
    }
}
