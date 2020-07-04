package com.utad.david.planfit.Fragments.MainMenu.Favorite.Nutrition;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Nutrition.GetNutritionFavorite;
import com.utad.david.planfit.Data.Favorite.Nutrition.NutritionFavoriteRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class NutritionFavoritePresenter implements GetNutritionFavorite {

    private NutritionFavoriteView view;

    public NutritionFavoritePresenter(NutritionFavoriteView view) {
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

    public void getFavoriteNutrition() {
        NutritionFavoriteRepository.getInstance().getNutritionFavoriteList();
    }

    public void emptyFavoriteNutritionList() {
        this.view.updateEmptyUI();
    }

    @Override
    public void getNutritionFavoriteList(boolean status, List<DefaultNutrition> defaultNutritions) {
        if (status) {
            this.view.getFavoriteList(defaultNutritions);
        }
    }

    @Override
    public void getEmptyNutritionFavorite(boolean status) {
        if (status) {
            this.view.updateEmptyUI();
        }
    }

    @Override
    public void addNutritionFavorite(boolean status) {}
    @Override
    public void deleteNutritionFavorite(boolean status) {}
    @Override
    public void getNutritionFavoriteListByType(boolean status, List<DefaultNutrition> list) {}
}
