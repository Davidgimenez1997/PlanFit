package com.utad.david.planfit.Data.Chat;

import com.utad.david.planfit.Model.User.User;

public interface GetChat {
    void deleteMessage(boolean status);
    void getUserDetails(boolean status, User userDetails);
}
