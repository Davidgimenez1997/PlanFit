package com.utad.david.planfit.Data.Nutrition;

import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;

import java.util.List;

public interface GetNutrition {
    void getSlimmingNutritions(boolean status, List<NutritionSlimming> data);
    void getToningNutritions (boolean status, List<NutritionToning> data);
    void getGainVolumeNutritions(boolean status, List <NutritionGainVolume> data);
}
