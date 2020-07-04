package com.utad.david.planfit.Fragments.MainMenu.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import java.util.List;

public interface NutritionView {
    void configurationRecycleView(List<DefaultNutrition> data);
    void onClickItemRecycleView(DefaultNutrition defaultNutrition, int mode);
    void deviceOfflineMessage();
}
