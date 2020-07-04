package com.utad.david.planfit.Data.Nutrition;

import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import java.util.List;

public interface GetNutrition {
    void getNutritionList(boolean status, List<DefaultNutrition> list, int mode);
}
