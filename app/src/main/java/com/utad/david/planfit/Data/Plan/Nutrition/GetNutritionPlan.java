package com.utad.david.planfit.Data.Plan.Nutrition;

import com.utad.david.planfit.Model.Plan.PlanNutrition;

import java.util.List;

public interface GetNutritionPlan {
    void addNutritionPlan(boolean status);
    void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions);
    void emptyNutritionPlan(boolean status);
    void deleteNutritionPlan(boolean status);
    void updateNutritionPlan(boolean status, List<PlanNutrition> updateList);
}
