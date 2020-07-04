package com.utad.david.planfit.Fragments.MainMenu.Favorite.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Sport.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.Sport.SportFavoriteRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class SportFavoritePresenter implements GetSportFavorite {

    private SportFavoriteView view;

    SportFavoritePresenter(SportFavoriteView view) {
        this.view = view;
        SportFavoriteRepository.getInstance().setGetSportFavorite(this);
    }

    public boolean checkConnectionInternet(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void getFavoriteSports() {
        SportFavoriteRepository.getInstance().getSportFavoriteList();
    }

    public void emptyFavoriteSportList() {
        this.view.updateEmptyUI();
    }

    @Override
    public void getSportFavoriteList(boolean status, List<DefaultSport> defaultSports) {
        if (status) {
            this.view.getFavoriteList(defaultSports);
        }
    }

    @Override
    public void getEmptySportFavoriteList(boolean status) {
        if (status) {
            this.view.updateEmptyUI();
        }
    }

    @Override
    public void addSportFavorite(boolean status) {}
    @Override
    public void deleteSportFavorite(boolean status) {}
    @Override
    public void getSportFavoriteListByType(boolean status, List<DefaultSport> list) {}
}
