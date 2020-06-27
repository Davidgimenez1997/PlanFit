package com.utad.david.planfit.DialogFragment.Favorite.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Sport.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.Sport.SportFavoriteRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class SportDetailsFavoritePresenter implements GetSportFavorite {

    private SportDetailsFavoriteView view;
    private DefaultSport sportFavorite;

    public SportDetailsFavoritePresenter(SportDetailsFavoriteView view) {
        SportFavoriteRepository.getInstance().setGetSportFavorite(this);
        this.view = view;
    }

    public void setSportFavorite(DefaultSport sportFavorite) {
        this.sportFavorite = sportFavorite;
    }

    public boolean checkInternetInDevice(Context context){
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
           return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void onClickDeleleSport() {
        SportFavoriteRepository.getInstance().deleteFavoriteSport(this.sportFavorite);
    }

    @Override
    public void deleteSportFavorite(boolean status) {
        if (status) {
            this.view.deleteSportFavorite();
        }
    }

    @Override
    public void addSportFavorite(boolean status) {}
    @Override
    public void getSportFavoriteListByType(boolean status, List<DefaultSport> list) {}
    @Override
    public void getSportFavoriteList(boolean status, List<DefaultSport> defaultSports) {}
    @Override
    public void getEmptySportFavoriteList(boolean status) {}
}
