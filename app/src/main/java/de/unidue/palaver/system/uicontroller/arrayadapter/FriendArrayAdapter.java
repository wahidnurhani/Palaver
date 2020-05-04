package de.unidue.palaver.system.uicontroller.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Friend;

public class FriendArrayAdapter extends ArrayAdapter<Friend> {




    public FriendArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Friend friend = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.friend_list_item_layout,parent,false);
        }

        TextView contactView = convertView.findViewById(R.id.friend_list_item_textview);
        assert friend != null;
        contactView.setText(friend.getUsername());

        return convertView;
    }
}
