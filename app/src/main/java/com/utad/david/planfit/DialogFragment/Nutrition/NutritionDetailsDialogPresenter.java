package com.utad.david.planfit.DialogFragment.Nutrition;

import android.content.Context;
import com.utad.david.planfit.Data.Favorite.Nutrition.GetNutritionFavorite;
import com.utad.david.planfit.Data.Favorite.Nutrition.NutritionFavoriteRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import java.util.List;
import java.util.stream.Collectors;

public class NutritionDetailsDialogPresenter implements GetNutritionFavorite {

    private NutritionDetailsDialogView view;
    private DefaultNutrition item;
    private int mode;

    public NutritionDetailsDialogPresenter(DefaultNutrition item, int mode, NutritionDetailsDialogView view) {
        this.item = item;
        this.mode = mode;
        this.view = view;
        NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(this);
    }

    public boolean checkConnectionInternet(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void getNutritionListByType() {
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                NutritionFavoriteRepository.getInstance().getNutritionFavoriteListByType(Constants.ModelNutritionFavorite.ADELGAZAR);
                break;
            case Constants.SportNutritionOption.TONING:
                NutritionFavoriteRepository.getInstance().getNutritionFavoriteListByType(Constants.ModelNutritionFavorite.TONIFICAR);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                NutritionFavoriteRepository.getInstance().getNutritionFavoriteListByType(Constants.ModelNutritionFavorite.GANAR_VOLUMEN);
                break;
        }
    }

    public void clickInsertNutritionFavorite() {
        switch (this.mode){
            case Constants.SportNutritionOption.SLIMMING:
                NutritionFavoriteRepository.getInstance().addFavoriteNutritionByType(this.item, Constants.ModelSportFavorite.ADELGAZAR);
                break;
            case Constants.SportNutritionOption.TONING:
                NutritionFavoriteRepository.getInstance().addFavoriteNutritionByType(this.item, Constants.ModelSportFavorite.TONIFICAR);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                NutritionFavoriteRepository.getInstance().addFavoriteNutritionByType(this.item, Constants.ModelSportFavorite.GANAR_VOLUMEN);
                break;
        }
    }

    public void clickDeleteNutritionFavorite() {
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                NutritionFavoriteRepository.getInstance().deleteFavoriteNutrition(this.item);
                break;
            case Constants.SportNutritionOption.TONING:
                NutritionFavoriteRepository.getInstance().deleteFavoriteNutrition(this.item);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                NutritionFavoriteRepository.getInstance().deleteFavoriteNutrition(this.item);
                break;
        }
    }

    public String getTitleItem() { return this.item.getName(); }

    public String getDescriptionItem() { return this.item.getDescription(); }

    public String getPhotoItem() { return this.item.getPhoto(); }

    public String getUrlItem() { return this.item.getUrl(); }

    @Override
    public void addNutritionFavorite(boolean status) {
        if (status) {
            this.view.addFavoriteNutrition();
        }
    }

    @Override
    public void deleteNutritionFavorite(boolean status) {
        if (status) {
            this.view.deleteFavoriteNutrition();
        }
    }

    @Override
    public void getNutritionFavoriteListByType(boolean status, List<DefaultNutrition> list) {
        if (status) {
            if (this.isItemInFavorite(list)) {
                this.view.updateButtonsUi();
            }
            this.view.getFavoriteNutritonList();
        }
    }

    private boolean isItemInFavorite(List<DefaultNutrition> lists) {
        List<DefaultNutrition> results = lists.stream().filter(x -> x.getName().equals(this.item.getName()))
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
    public void getNutritionFavoriteList(boolean status, List<DefaultNutrition> defaultNutritions) {}
    @Override
    public void getEmptyNutritionFavorite(boolean status) {}
}
