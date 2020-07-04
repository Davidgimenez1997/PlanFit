package com.utad.david.planfit.Fragments.MainMenu.Favorite.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import java.util.List;

public interface NutritionFavoriteView {
    void deviceOfflineMessage();
    void updateEmptyUI();
    void getFavoriteList(List<DefaultNutrition> data);
}
