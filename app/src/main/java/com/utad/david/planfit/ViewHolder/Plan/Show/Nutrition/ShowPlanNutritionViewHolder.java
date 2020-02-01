package com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition;

import android.view.View;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;

public class ShowPlanNutritionViewHolder extends BaseViewHolder {

    private TextView timeStart;

    public ShowPlanNutritionViewHolder(View v) {
        super(v);
        timeStart = v.findViewById(R.id.timeStart);
    }

    public void setData(ArrayList<PlanNutrition> planNutrition){

        for(int i=0;i<planNutrition.size();i++){

            switch (planNutrition.get(i).getType()){

                case Constants.TiposPlanNutricion.MODE_DESAYUNO:
                    timeStart.setText(Constants.TiposPlanNutricion.DESAYUNO);
                    break;

                case Constants.TiposPlanNutricion.MODE_COMIDA:
                    timeStart.setText(Constants.TiposPlanNutricion.COMIDA);
                    break;

                case Constants.TiposPlanNutricion.MODE_MERIENDA:
                    timeStart.setText(Constants.TiposPlanNutricion.MERIENDA);
                    break;

                case Constants.TiposPlanNutricion.MODE_CENA:
                    timeStart.setText(Constants.TiposPlanNutricion.CENA);
                    break;
            }
        }
    }
}
