package com.utad.david.planfit.Data.User.User.EditUser;

import com.utad.david.planfit.Model.User.User;

public interface GetEditUser {
    void deletePhoto(boolean status);
    void updatePhoto(boolean status);
    void updateNickName(boolean status);
    void updateFullName(boolean status);
    void deleteUser(boolean status);
}
