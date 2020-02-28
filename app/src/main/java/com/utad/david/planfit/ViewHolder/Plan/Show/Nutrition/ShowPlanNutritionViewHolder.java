package com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition;

import android.view.View;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShowPlanNutritionViewHolder extends BaseViewHolder {

    private TextView timeStart;

    public ShowPlanNutritionViewHolder(View v) {
        super(v);
        timeStart = v.findViewById(R.id.timeStart);
    }

    public void setData(ArrayList<PlanNutrition> planNutrition){
        for(int i=0;i<planNutrition.size();i++){
            this.setTypes(planNutrition.get(i));
        }
    }

    private void setTypes(PlanNutrition planNutrition) {
        switch (planNutrition.getType()){
            case Constants.TypesPlanNutrition.MODE_DESAYUNO:
                timeStart.setText(Constants.TypesPlanNutrition.DESAYUNO);
                break;
            case Constants.TypesPlanNutrition.MODE_COMIDA:
                timeStart.setText(Constants.TypesPlanNutrition.COMIDA);
                break;
            case Constants.TypesPlanNutrition.MODE_MERIENDA:
                timeStart.setText(Constants.TypesPlanNutrition.MERIENDA);
                break;
            case Constants.TypesPlanNutrition.MODE_CENA:
                timeStart.setText(Constants.TypesPlanNutrition.CENA);
                break;
        }
    }
}
