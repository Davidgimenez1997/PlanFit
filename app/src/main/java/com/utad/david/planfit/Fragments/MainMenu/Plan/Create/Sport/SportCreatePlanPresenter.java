package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Sport.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.Sport.SportFavoriteRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.Collections;
import java.util.List;

public class SportCreatePlanPresenter implements GetSportFavorite {

    private SportCreatePlanView view;

    public SportCreatePlanPresenter(SportCreatePlanView view) {
        this.view = view;
        SportFavoriteRepository.getInstance().setGetSportFavorite(this);
    }

    public boolean checkInternetInDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            SportFavoriteRepository.getInstance().getSportFavoriteList();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void onClickItem(DefaultSport item) {
        this.view.clickItem(item);
    }

    @Override
    public void getSportFavoriteList(boolean status, List<DefaultSport> defaultSports) {
        if (status) {
            Collections.sort(defaultSports);
            this.view.getSportFavoriteList(defaultSports);
        }
    }

    @Override
    public void getEmptySportFavoriteList(boolean status) {
        if (status) {
            this.view.getEmptySportFavoriteList();
        }
    }

    @Override
    public void addSportFavorite(boolean status) {}
    @Override
    public void deleteSportFavorite(boolean status) {}
    @Override
    public void getSportFavoriteListByType(boolean status, List<DefaultSport> list) {}
}
