package com.utad.david.planfit.Adapter.Chat;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Adapter.Users.UsersAdapters;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;


    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }


    public void addNewMessages(List<ChatMessage> newMessages) {
        int startPosition = chatMessages.size();
        chatMessages.addAll(newMessages);
        notifyItemRangeInserted(startPosition, newMessages.size() - 1);
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incoming_message, parent, false);
        return new ChatAdapter.ChatViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position) {
        final ChatMessage current = chatMessages.get(position);
        holder.setData(current);
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView date;
        //private ImageView photoSlimming;

        public ChatViewHolder(View v) {
            super(v);
            message= v.findViewById(R.id.text_incoming_message_body);
            date = v.findViewById(R.id.text_incoming_message_date);
            //photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(ChatMessage chatMessage) {
            message.setText(chatMessage.getMessageText());
            date.setText((DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    chatMessage.getMessageTime())));
            //Utils.loadImage(defaultNutrition.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
        }
    }
}