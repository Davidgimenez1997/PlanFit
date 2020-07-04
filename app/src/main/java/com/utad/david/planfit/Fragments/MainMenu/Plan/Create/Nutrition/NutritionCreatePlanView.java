package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;

import java.util.List;

public interface NutritionCreatePlanView {
    void deviceOfflineMessage();
    void getNutritionFavoriteList(List<DefaultNutrition> list);
    void getEmptyNutritionFavoriteList();
    void clickItem(DefaultNutrition item);
}
