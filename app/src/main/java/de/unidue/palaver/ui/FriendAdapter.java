package de.unidue.palaver.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.StringValue;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private LayoutInflater inflater;
    private Context context;

    FriendAdapter(Context context, List<Friend> friends) {
        inflater = LayoutInflater.from(context);
        this.friends = friends;
        this.context = context;
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
        holder.setData(current, position);

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatRoomActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(StringValue.IntentKeyName.FRIEND, current);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(friends!=null){
            return friends.size();
        }
        return 0;
    }

    void overrade(List<Friend> searchedList) {
        this.friends= searchedList;
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder{

        private View cardView;
        private TextView name;

        FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.friend_cardHolder);
            name = itemView.findViewById(R.id.friend_textview);
    }

        void setData(Friend current, int position) {
              this.name.setText(current.getUsername());
        }

        View getCardView() {
            return cardView;
        }
    }
}
