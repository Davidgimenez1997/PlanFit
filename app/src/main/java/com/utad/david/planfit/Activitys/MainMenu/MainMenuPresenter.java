package com.utad.david.planfit.Activitys.MainMenu;

import android.content.Context;

import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.User.GetUser;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Utils.UtilsNetwork;

public class MainMenuPresenter implements GetUser {

    private MainMenuView view;

    public MainMenuPresenter(MainMenuView view) {
        this.view = view;
        UserRepository.getInstance().setGetUser(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void loadData() {
        UserRepository.getInstance().getUserData();
    }

    public void logout() {
        SessionUser.getInstance().removeUser();
        UserRepository.getInstance().logout();
        this.view.navigateLogout();
    }

    @Override
    public void getUserData(boolean status) {
        if (status) {
            this.view.getUserData();
        }  else {
            if (UserRepository.getInstance().getUser().getImgUser() != null) {
                this.view.getUserData();
            }
        }
    }

    @Override
    public void addUserData(boolean status) {}
}
