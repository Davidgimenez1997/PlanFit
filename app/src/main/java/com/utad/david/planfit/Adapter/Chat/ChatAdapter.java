package com.utad.david.planfit.Adapter.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> mContent = new ArrayList<>();

    public void addData(ChatMessage data) {
        mContent.add(data);
    }

    public void clearData() {
        mContent.clear();
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incoming_message, parent, false);
        return new ChatAdapter.ChatViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position) {
        final ChatMessage current = mContent.get(position);
        holder.setData(current);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView message;

        public ChatViewHolder(View v) {
            super(v);
            message= v.findViewById(R.id.item_message);
            name = v.findViewById(R.id.item_username);
        }

        public void setData(ChatMessage chatMessage) {
           message.setText(chatMessage.getMessage());
           name.setText(chatMessage.getName());
        }
    }
}