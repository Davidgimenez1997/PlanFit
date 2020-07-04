package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition.Details;

import android.content.Context;

import com.utad.david.planfit.Data.Plan.Nutrition.GetNutritionPlan;
import com.utad.david.planfit.Data.Plan.Nutrition.NutritionPlanRepository;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class DetailsNutritionPlanPresenter implements GetNutritionPlan {

    private DetailsNutritionPlanView view;

    public DetailsNutritionPlanPresenter(DetailsNutritionPlanView view) {
        this.view = view;
        NutritionPlanRepository.getInstance().setGetNutritionPlan(this);
    }

    public boolean checkInternetInDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            NutritionPlanRepository.getInstance().getNutritionPlan();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void updatePlan(PlanNutrition item) {
        NutritionPlanRepository.getInstance().updatePlanNutrition(item);
    }

    @Override
    public void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions) {
        this.view.getNutritionPlan();
    }

    @Override
    public void addNutritionPlan(boolean status) {}
    @Override
    public void emptyNutritionPlan(boolean status) {}
    @Override
    public void deleteNutritionPlan(boolean status) {}
    @Override
    public void updateNutritionPlan(boolean status, List<PlanNutrition> updateList) {}
}
