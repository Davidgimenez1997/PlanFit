package com.utad.david.planfit.DialogFragment.Favorite.Nutrition;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Nutrition.GetNutritionFavorite;
import com.utad.david.planfit.Data.Favorite.Nutrition.NutritionFavoriteRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class NutritionFavoriteDetailsDialogPresenter implements GetNutritionFavorite {

    private NutritionFavoriteDetailsDialogView view;
    private DefaultNutrition nutritionFavorite;

    public NutritionFavoriteDetailsDialogPresenter(NutritionFavoriteDetailsDialogView view) {
        this.view = view;
        NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(this);
    }

    public void setNutritionFavorite(DefaultNutrition nutritionFavorite) {
        this.nutritionFavorite = nutritionFavorite;
    }

    public boolean checkInternetInDevice(Context context){
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void onClickDeleleNutrition() {
        NutritionFavoriteRepository.getInstance().deleteFavoriteNutrition(this.nutritionFavorite);
    }

    @Override
    public void deleteNutritionFavorite(boolean status) {
        if (status) {
            this.view.deleteNutritionFavorite();
        }
    }

    @Override
    public void addNutritionFavorite(boolean status) {}
    @Override
    public void getNutritionFavoriteListByType(boolean status, List<DefaultNutrition> list) {}
    @Override
    public void getNutritionFavoriteList(boolean status, List<DefaultNutrition> defaultNutritions) {}
    @Override
    public void getEmptyNutritionFavorite(boolean status) {}
}
