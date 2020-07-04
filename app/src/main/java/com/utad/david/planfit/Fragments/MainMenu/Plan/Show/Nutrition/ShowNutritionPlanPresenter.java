package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition;

import android.content.Context;

import com.utad.david.planfit.Data.Plan.Nutrition.GetNutritionPlan;
import com.utad.david.planfit.Data.Plan.Nutrition.NutritionPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowNutritionPlanPresenter implements GetNutritionPlan {

    private ShowNutritionPlanView view;

    public ShowNutritionPlanPresenter(ShowNutritionPlanView view) {
        this.view = view;
        NutritionPlanRepository.getInstance().setGetNutritionPlan(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            NutritionPlanRepository.getInstance().getNutritionPlan();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void shortArray(ArrayList<PlanNutrition> list) {
        Collections.sort(list);
    }

    public void checkArraySize(ArrayList<PlanNutrition> arrNutrition, List<PlanNutrition> planNutrition) {
        if (arrNutrition.size() == 0) {
            this.updateNutritionPlan(false, null);
        } else {
            this.updateNutritionPlan(true, planNutrition);
        }
    }

    public void updatePlan(PlanNutrition planNutrition) {
        NutritionPlanRepository.getInstance().updatePlanNutrition(planNutrition);
    }

    @Override
    public void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions) {
        if (status) {
            this.view.getNutritionPlan(planNutritions);
        }
    }

    @Override
    public void emptyNutritionPlan(boolean status) {
        if (status) {
            this.view.emptyNutritionPlan();
        }
    }

    @Override
    public void updateNutritionPlan(boolean status, List<PlanNutrition> updateList) {
        if (status) {
            this.view.updateNutritionPlan(updateList);
        }
    }

    @Override
    public void addNutritionPlan(boolean status) {}
    @Override
    public void deleteNutritionPlan(boolean status) {}
}
