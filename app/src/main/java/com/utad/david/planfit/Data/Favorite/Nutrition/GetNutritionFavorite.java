package com.utad.david.planfit.Data.Favorite.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;

import java.util.List;

public interface GetNutritionFavorite {
    void addNutritionFavorite (boolean status);
    void deleteNutritionFavorite(boolean status);
    void getNutritionSlimmingFavorite(boolean status, List<NutritionSlimming> nutritionSlimmings);
    void getNutritionToningFavorite(boolean status, List<NutritionToning> nutritionTonings);
    void getNutritionGainVolumeFavorite(boolean status, List<NutritionGainVolume> nutritionGainVolumes);
    void getNutritionAllFavorite(boolean status, List<DefaultNutrition> defaultNutritions);
    void emptyNutritionFavorite(boolean status);
}
