package com.utad.david.planfit.Activitys.Chat;

import android.content.Context;
import com.utad.david.planfit.Data.Chat.ChatRepository;
import com.utad.david.planfit.Data.Chat.GetChat;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Model.Chat.ChatMessage;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;

public class ChatPresenter implements GetChat {

    private ChatView view;
    private String userUid;

    public ChatPresenter(ChatView view) {
        this.view = view;
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOffliMessage();
            return false;
        }
    }

    public void setUi() {
        ChatRepository.getInstance().setGetChat(this);
        if (UserRepository.getInstance().getFirebaseAuth().getCurrentUser() != null) {
            this.view.setUiLoadChat();
        }
    }

    public String getUserUid() {
        this.userUid = UserRepository.getInstance().getCurrentUser().getUid();
        return this.userUid;
    }

    public void onClickMessage(ChatMessage message) {
        if (message.getMessageUserId().equals(UserRepository.getInstance().getCurrentUser().getUid())) {
            this.view.clickMessageUser(message);
        } else {
            this.view.clickMessageOtherUser(message);
        }
    }

    public void getUserDetailsByMessage(ChatMessage message) {
        ChatRepository.getInstance().getUserDetailsByMessage(message);
    }

    public void deleteMessageUser(ChatMessage message) {
        ChatRepository.getInstance().deleteMessageInChat(message);
    }

    public void onClickSend(String message) {
        if (Utils.isEmpty(message)) {
            this.view.errorMessageEmpty();
        } else {
            ChatMessage chatMessage = new ChatMessage(message, UserRepository.getInstance().getUser().getNickName(),
                    UserRepository.getInstance().getCurrentUser().getUid());
            this.view.sendMessage(chatMessage);
        }
    }

    @Override
    public void deleteMessage(boolean status) {
        if (status) {
            this.view.deleteMessage();
        }
    }

    @Override
    public void getUserDetails(boolean status, User userDetails) {
        if (status) {
            if (userDetails != null) {
                this.view.getUserDetails(userDetails);
            }
        }
    }
}
