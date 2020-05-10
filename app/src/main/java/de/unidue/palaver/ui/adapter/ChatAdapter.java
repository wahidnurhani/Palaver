package de.unidue.palaver.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.unidue.palaver.R;
import de.unidue.palaver.system.model.Chat;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.StringValue;
import de.unidue.palaver.ui.ChatRoomActivity;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Chat> chats;
    private LayoutInflater inflater;
    private Context context;

    public ChatAdapter(Context context, List<Chat> chats) {
        inflater = LayoutInflater.from(context);
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chats_view_holder_layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Chat current = chats.get(position);
        holder.setData(current, position);

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatRoomActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(StringValue.IntentKeyName.FRIEND, new Friend(current.getFk_friend()));
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(chats!=null){
            return chats.size();
        }
        return 0;
    }

    public void setItems(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView textViewName;
        private TextView textViewLastMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.chats_cardView);
            this.textViewName = itemView.findViewById(R.id.chat_list_name);
            this.textViewLastMessage = itemView.findViewById(R.id.chat_list_message);
        }

        CardView getCardView() {
            return cardView;
        }

        void setData(Chat current, int position){
            this.textViewName.setText(current.fk_friend);
            if(current.getData()!= null){
                this.textViewLastMessage.setText(current.getData().trim());
            }
        }
    }
}
