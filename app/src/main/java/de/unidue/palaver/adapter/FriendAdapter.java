package de.unidue.palaver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import de.unidue.palaver.R;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.StringValue;
import de.unidue.palaver.activity.ChatRoomActivity;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private LayoutInflater inflater;
    private Activity activity;

    public FriendAdapter(Activity activity, List<Friend> friends) {
        inflater = LayoutInflater.from(activity);
        this.friends = friends;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friend_view_holder_layout, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend current = friends.get(position);
        holder.setData(current);

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(activity, ChatRoomActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(StringValue.IntentKeyName.FRIEND, current);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(friends!=null){
            return friends.size();
        }
        return 0;
    }

    public void setItems(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private View cardView;
        private TextView name;

        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.friend_cardHolder);
            name = itemView.findViewById(R.id.friend_textview);

            itemView.setOnCreateContextMenuListener(this);
        }

        void setData(Friend current) {
              this.name.setText(current.getUsername());
        }

        View getCardView() {
            return cardView;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action for "+((TextView)v.findViewById(R.id.friend_textview)).getText());
            menu.add(this.getAdapterPosition(), R.id.menu_show_message, 1, "Show Message");
            menu.add(this.getAdapterPosition(), R.id.menu_remove_friend, 2, "Delete Friend");
        }
    }
}
