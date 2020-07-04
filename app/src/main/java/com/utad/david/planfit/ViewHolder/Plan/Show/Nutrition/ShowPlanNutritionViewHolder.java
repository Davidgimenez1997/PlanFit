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
        this.timeStart = v.findViewById(R.id.timeStart);
    }

    public void setData(ArrayList<PlanNutrition> planNutrition){
        for (int i = 0; i < planNutrition.size(); i++) {
            this.setTypes(planNutrition.get(i));
        }
    }

    private void setTypes(PlanNutrition planNutrition) {
        String text = "";
        switch (planNutrition.getType()){
            case Constants.TypesPlanNutrition.MODE_DESAYUNO:
                text = Constants.TypesPlanNutrition.DESAYUNO;
                break;
            case Constants.TypesPlanNutrition.MODE_COMIDA:
                text = Constants.TypesPlanNutrition.COMIDA;
                break;
            case Constants.TypesPlanNutrition.MODE_MERIENDA:
                text = Constants.TypesPlanNutrition.MERIENDA;
                break;
            case Constants.TypesPlanNutrition.MODE_CENA:
                text = Constants.TypesPlanNutrition.CENA;
                break;
        }
        this.timeStart.setText(text);
    }
}
