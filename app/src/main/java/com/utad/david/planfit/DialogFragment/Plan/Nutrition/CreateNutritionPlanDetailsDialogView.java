package com.utad.david.planfit.DialogFragment.Plan.Nutrition;

import com.utad.david.planfit.Model.Plan.PlanNutrition;

import java.util.List;

public interface CreateNutritionPlanDetailsDialogView {
    void deviceOfflineMessage();
    void deletetNutritionPlan();
    void addNutritionPlan();
    void getNutritionPlan(List<PlanNutrition> planNutrition);
}
