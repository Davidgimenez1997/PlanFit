package com.utad.david.planfit.Data.Favorite.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import java.util.List;

public interface GetNutritionFavorite {
    void addNutritionFavorite (boolean status);
    void deleteNutritionFavorite(boolean status);
    void getNutritionFavoriteListByType(boolean status, List<DefaultNutrition> list);
    void getNutritionFavoriteList(boolean status, List<DefaultNutrition> defaultNutritions);
    void getEmptyNutritionFavorite(boolean status);
}
