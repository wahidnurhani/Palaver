package de.unidue.palaver.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.unidue.palaver.R;
import de.unidue.palaver.system.sessionmanager.SessionManager;
import de.unidue.palaver.system.model.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> messages;
    private LayoutInflater inflater;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.messages = messages;
    }
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.message_view_holder_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message current = messages.get(position);

        LinearLayout containerLinearLayout = holder.getLinearLayout();
        TextView chatItemTextView = holder.getTextView();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)containerLinearLayout.getLayoutParams();

        if (current.getSender().equals(SessionManager.getSessionManagerInstance(context).getUser().getUserName())){
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

        holder.setData(current);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(messages!=null){
            return messages.size();
        }
        return 0;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout linearLayout;
        private TextView textView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.message_container);
            textView = itemView.findViewById(R.id.message_textView);
        }

        LinearLayout getLinearLayout() {
            return linearLayout;
        }

        TextView getTextView() {
            return textView;
        }

        void setData(Message current) {
            this.textView.setText(current.getMessage().trim());
        }
    }
}

