package com.utad.david.planfit.DialogFragment.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Sport.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.Sport.SportFavoriteRepository;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;
import java.util.stream.Collectors;

public class SportDetailsDialogPresenter implements GetSportFavorite {

    private DefaultSport item;
    private int mode;
    private SportDetailsDialogView view;

    public SportDetailsDialogPresenter(DefaultSport item, int mode, SportDetailsDialogView view) {
        this.item = item;
        this.mode = mode;
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

    public void getSportListByType() {
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                SportFavoriteRepository.getInstance().getSportFavoriteListByType(Constants.ModelSportFavorite.ADELGAZAR);
                break;
            case Constants.SportNutritionOption.TONING:
                SportFavoriteRepository.getInstance().getSportFavoriteListByType(Constants.ModelSportFavorite.TONIFICAR);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                SportFavoriteRepository.getInstance().getSportFavoriteListByType(Constants.ModelSportFavorite.GANAR_VOLUMEN);
                break;
        }
    }

    public String getTitleItem() { return this.item.getName(); }

    public String getDescriptionItem() { return this.item.getDescription(); }

    public String getPhotoItem() { return this.item.getPhoto(); }

    public String getVideoItem() { return this.item.getVideo(); }

    public void clickInsertSportFavorite() {
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                SportFavoriteRepository.getInstance().addFavoriteSportByType(this.item, Constants.ModelSportFavorite.ADELGAZAR);
                break;
            case Constants.SportNutritionOption.TONING:
                SportFavoriteRepository.getInstance().addFavoriteSportByType(this.item, Constants.ModelSportFavorite.TONIFICAR);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                SportFavoriteRepository.getInstance().addFavoriteSportByType(this.item, Constants.ModelSportFavorite.GANAR_VOLUMEN);
                break;
        }
    }

    public void clickDeleteSportFavorite() {
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                SportFavoriteRepository.getInstance().deleteFavoriteSport(this.item);
                break;
            case Constants.SportNutritionOption.TONING:
                SportFavoriteRepository.getInstance().deleteFavoriteSport(this.item);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                SportFavoriteRepository.getInstance().deleteFavoriteSport(this.item);
                break;
        }
    }

    @Override
    public void addSportFavorite(boolean status) {
        if (status) {
            this.view.addFavoriteSport();
        }
    }

    @Override
    public void deleteSportFavorite(boolean status) {
        if (status) {
            this.view.deleteFavoriteSport();
        }
    }

    @Override
    public void getSportFavoriteListByType(boolean status, List<DefaultSport> list) {
        if (status) {
            if (this.isItemInFavorite(list)) {
                this.view.updateButtonsUi();
            }
            this.view.getFavoriteSportList();
        }
    }

    private boolean isItemInFavorite(List<DefaultSport> lists) {
        List<DefaultSport> results = lists.stream().filter(x -> x.getName().equals(this.item.getName()))
                .collect(Collectors.toList());
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                return results.size() != 0;
            case Constants.SportNutritionOption.TONING:
                return results.size() != 0;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                return results.size() != 0;
        }
        return false;
    }

    @Override
    public void getSportFavoriteList(boolean status, List<DefaultSport> defaultSports) {}
    @Override
    public void getEmptySportFavoriteList(boolean status) {}
}
