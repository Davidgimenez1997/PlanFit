package com.utad.david.planfit.DialogFragment.User.InfoAboutApp;

import android.content.Context;

import com.utad.david.planfit.Data.User.Developer.DeveloperRepository;
import com.utad.david.planfit.Data.User.Developer.GetDeveloper;
import com.utad.david.planfit.Model.Developer.Developer;
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.UtilsNetwork;

public class InfoAboutAppDialogPresenter implements GetDeveloper {

    private InfoAboutAppDialogView view;
    private Context context;
    private Developer developerCache;

    public InfoAboutAppDialogPresenter(InfoAboutAppDialogView view, Context context) {
        this.view = view;
        this.context = context;
        DeveloperRepository.getInstance().setGetDeveloper(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void getData() {
        if (this.checkInternetDevice(this.context)) {
            Developer cache = SharedPreferencesManager.loadDeveloper(this.context, SharedPreferencesManager.DEVELOPER_INFO);
            if (cache == null) {
                DeveloperRepository.getInstance().getDeveloperInfo();
            } else {
                this.developerCache = cache;
                this.view.getDeveloper(this.developerCache);
            }
        }
    }

    @Override
    public void getDeveloperInfo(boolean status, Developer developer) {
        if (status) {
            SharedPreferencesManager.saveDeveloper(developer, this.context,  SharedPreferencesManager.DEVELOPER_INFO);
            this.view.getDeveloper(developer);
        }
    }
}
