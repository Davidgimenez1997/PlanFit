package com.utad.david.planfit.Adapter.Chat;

import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.utad.david.planfit.Activitys.Chat.ChatActivity;
import com.utad.david.planfit.Model.Chat.ChatMessage;
import com.utad.david.planfit.R;

public class ChatAdapter extends FirebaseListAdapter<ChatMessage> {

    private ChatActivity activity;
    private Callback callback;

    public interface Callback{
        void onItemClick(ChatMessage message);
    }

    public ChatAdapter(ChatActivity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref,Callback callback) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = getItem(position);
        if (chatMessage.getMessageUserId().equals(this.activity.getLoggedInUserName()))
            view = this.activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = this.activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
        this.populateView(view, chatMessage, position);
        return view;
    }

    @Override
    protected void populateView(View view, ChatMessage model, int position) {
        TextView messageText = view.findViewById(R.id.message_text);
        TextView messageUser = view.findViewById(R.id.message_user);
        TextView messageTime = view.findViewById(R.id.message_time);
        view.setOnClickListener(v1 -> {
            if (this.callback != null) {
                this.callback.onItemClick(model);
            }
        });
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
