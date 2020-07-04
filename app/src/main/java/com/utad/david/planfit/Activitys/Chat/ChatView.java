package com.utad.david.planfit.Activitys.Chat;

import com.utad.david.planfit.Model.Chat.ChatMessage;
import com.utad.david.planfit.Model.User.User;

public interface ChatView {
    void deviceOffliMessage();
    void setUiLoadChat();
    void clickMessageUser(ChatMessage message);
    void clickMessageOtherUser(ChatMessage message);
    void errorMessageEmpty();
    void sendMessage(ChatMessage chatMessage);
    void getUserDetails(User userDetails);
    void deleteMessage();
}
