package com.utad.david.planfit.DialogFragment.Plan.Nutrition;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.utad.david.planfit.Data.Plan.Nutrition.GetNutritionPlan;
import com.utad.david.planfit.Data.Plan.Nutrition.NutritionPlanRepository;
import com.utad.david.planfit.Data.Plan.SessionPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class CreateNutritionPlanDetailsDialogPresenter implements GetNutritionPlan {

    private CreateNutritionPlanDetailsDialogView view;
    private DefaultNutrition nutritionFavorite;
    private ArrayAdapter spinnerArrayAdapter;

    public CreateNutritionPlanDetailsDialogPresenter(CreateNutritionPlanDetailsDialogView view) {
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

    public void setNutritionFavorite(DefaultNutrition nutritionFavorite) {
        this.nutritionFavorite = nutritionFavorite;
    }

    public ArrayAdapter getSpinnerArrayAdapter(Context context) {
        this.spinnerArrayAdapter = ArrayAdapter.createFromResource(context, R.array.nutrition, R.layout.spinner_item);
        this.spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        return this.spinnerArrayAdapter;
    }

    public int setType(String selectedItem) {
        int type = 0;
        switch (selectedItem){
            case Constants.TypesPlanNutrition.DESAYUNO:
                type = 1;
                break;
            case Constants.TypesPlanNutrition.COMIDA:
                type = 2;
                break;
            case Constants.TypesPlanNutrition.MERIENDA:
                type = 3;
                break;
            case Constants.TypesPlanNutrition.CENA:
                type = 4;
                break;
        }
        return type;
    }

    public void createNutritionPlan(int type) {
        SessionPlan.getInstance().setPlanNutrition(new PlanNutrition(this.nutritionFavorite.getName(), this.nutritionFavorite.getPhoto(), type, Constants.ModePlan.NO));
        NutritionPlanRepository.getInstance().addNutritionPlan();
    }

    public void deletePlan() {
        NutritionPlanRepository.getInstance().deleteNutritionPlan(this.nutritionFavorite.getName());
    }

    @Override
    public void addNutritionPlan(boolean status) {
        if (status) {
            this.view.addNutritionPlan();
        }
    }

    @Override
    public void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions) {
        if (status) {
            this.view.getNutritionPlan(planNutritions);
        }
    }

    @Override
    public void deleteNutritionPlan(boolean status) {
        if (status) {
            this.view.deletetNutritionPlan();
        }
    }

    @Override
    public void emptyNutritionPlan(boolean status) {}
    @Override
    public void updateNutritionPlan(boolean status, List<PlanNutrition> updateList) {}
}
