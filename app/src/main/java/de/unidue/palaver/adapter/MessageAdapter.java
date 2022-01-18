package de.unidue.palaver.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.unidue.palaver.R;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.serviceandworker.locationservice.LocationParser;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private Context applicationContext;
    private Activity activity;
    private List<Message> messages;
    private LayoutInflater inflater;
    private User user;

    public MessageAdapter(Context applicationContext, Activity activity, User user, List<Message> messages) {
        this.applicationContext = applicationContext;
        this.inflater = LayoutInflater.from(activity);
        this.user = user;
        this.messages = messages;
        this.activity = activity;
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

        if (current.getSender().equals(user.getUserName())){
            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            containerLinearLayout.setLayoutParams(params);
            if(current.getMessage().contains("https://maps.google.com/?q=") || isBase64(current.getMessage())){
                chatItemTextView.setTextColor(Color.YELLOW);
            } else {
                chatItemTextView.setTextColor(Color.WHITE);
            }

            containerLinearLayout.setBackgroundResource(R.drawable.shape_round_message_out);

        }else{
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            containerLinearLayout.setLayoutParams(params);

            if(current.getMessage().contains("https://maps.google.com/?q=") || isBase64(current.getMessage())){
                chatItemTextView.setTextColor(Color.BLUE);
            } else {
                chatItemTextView.setTextColor(Color.BLACK);
            }

            containerLinearLayout.setBackgroundResource(R.drawable.shape_round_message_in);
        }
        try {
            holder.setData(applicationContext, current);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.getTextView().setOnClickListener(v -> {
            String text = holder.getLocationUrl();
            if(text!=null && text.contains("https://maps.google.com/?q=") ){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                activity.startActivity(intent);
            } else if(holder.getFile() != null){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(holder.getFile()));
                activity.startActivity(intent);
            }
        });
    }

    public static boolean isBase64(String message) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        try {
            return pattern.matches(message);
        } catch(IllegalArgumentException iae) {
            return false;
        }
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
        private String locationUrl;
        private LinearLayout linearLayout;
        private TextView textView;
        private File file;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.message_container);
            textView = itemView.findViewById(R.id.message_textView);
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public TextView getTextView() {
            return textView;
        }

        public String getLocationUrl() {
            return locationUrl;
        }

        public File getFile() {
            return file;
        }

        void setData(Context applicationContext, Message current) throws IOException {
            String data = current.getMessage().trim();
            if(data.contains("https://maps.google.com/?q=")){
                this.locationUrl = data;
                Log.i(TAG, "contains location");
                Address address = new LocationParser(applicationContext, data).getAddress();
                if(address!= null){
                    this.textView.setText(formatLocation(address, data));
                } else {
                    this.textView.setText(data);
                }
            } else if (isBase64(current.getMessage())){
                this.file = formatFile(applicationContext, current.getMessage());
                this.textView.setText(file.getName());
            } else {
                this.textView.setText(data);
            }
        }

        File formatFile(final Context context, final String imageData) throws IOException {
            final byte[] imgBytesData = android.util.Base64.decode(imageData,
                    android.util.Base64.DEFAULT);

            final File file = File.createTempFile("file", null, context.getCacheDir());
            final FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    fileOutputStream);
            try {
                bufferedOutputStream.write(imgBytesData);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }

        String formatLocation(Address address, String data){
            return "Location :" + "\n" +
                    address.getAddressLine(0) + "\n" +
                    " " + "\n" +
                    address.getLatitude() + ", " + address.getLongitude() +
                    "\n" + " " + "\n" +
                    data;
        }
    }


}

