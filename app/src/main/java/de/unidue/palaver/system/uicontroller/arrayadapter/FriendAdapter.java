package de.unidue.palaver.system.uicontroller.arrayadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Friend;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, List<Friend> friends) {
        inflater = LayoutInflater.from(context);
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friend_list_recycle_view_item_layout, parent, false);
        FriendViewHolder friendViewHolder = new FriendViewHolder(view);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend current = friends.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private int position;
        private Friend friend;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friend_list_item_textview);
        }

        public void setData(Friend current, int position) {
              this.name.setText(current.getUsername());
              this.position = position;
              this.friend = current;
        }
    }
}
