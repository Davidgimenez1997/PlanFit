package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Nutrition;

import android.content.Context;

import com.utad.david.planfit.Data.Favorite.Nutrition.GetNutritionFavorite;
import com.utad.david.planfit.Data.Favorite.Nutrition.NutritionFavoriteRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.Collections;
import java.util.List;

public class NutritionCreatePlanPresenter implements GetNutritionFavorite {

    private NutritionCreatePlanView view;

    public NutritionCreatePlanPresenter(NutritionCreatePlanView view) {
        this.view = view;
        NutritionFavoriteRepository.getInstance().setGetNutritionFavorite(this);
    }

    public boolean checkInternetInDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            NutritionFavoriteRepository.getInstance().getNutritionFavoriteList();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void onClickItem(DefaultNutrition item) {
        this.view.clickItem(item);
    }

    @Override
    public void getNutritionFavoriteList(boolean status, List<DefaultNutrition> defaultNutritions) {
        if (status) {
            Collections.sort(defaultNutritions);
            this.view.getNutritionFavoriteList(defaultNutritions);
        }
    }

    @Override
    public void getEmptyNutritionFavorite(boolean status) {
        if (status) {
            this.view.getEmptyNutritionFavoriteList();
        }
    }

    @Override
    public void addNutritionFavorite(boolean status) {}
    @Override
    public void deleteNutritionFavorite(boolean status) {}
    @Override
    public void getNutritionFavoriteListByType(boolean status, List<DefaultNutrition> list) {}
}
