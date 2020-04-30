package de.unidue.palaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.unidue.palaver.model.Friend;

public class FriendArrayAdapter extends ArrayAdapter<Friend> {

    public FriendArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
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
