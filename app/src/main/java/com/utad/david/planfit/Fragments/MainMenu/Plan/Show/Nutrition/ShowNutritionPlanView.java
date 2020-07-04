package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition;

import com.utad.david.planfit.Model.Plan.PlanNutrition;
import java.util.List;

public interface ShowNutritionPlanView {
    void deviceOfflineMessage();
    void getNutritionPlan(List<PlanNutrition> planNutrition);
    void updateNutritionPlan(List<PlanNutrition> updateList);
    void emptyNutritionPlan();
}
