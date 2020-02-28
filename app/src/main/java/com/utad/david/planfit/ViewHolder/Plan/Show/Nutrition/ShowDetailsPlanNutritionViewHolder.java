package com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

public class ShowDetailsPlanNutritionViewHolder extends BaseViewHolder {

    private TextView nameNutrition;
    private TextView timeStart;
    private TextView timeEnd;
    private ImageView photoNutrition;
    private ImageView imageViewCheck;

    public ShowDetailsPlanNutritionViewHolder(View v) {
        super(v);
        nameNutrition = v.findViewById(R.id.titleSport);
        photoNutrition = v.findViewById(R.id.imageViewShowSport);
        timeEnd = v.findViewById(R.id.timeEnd);
        timeStart = v.findViewById(R.id.timeStart);
        imageViewCheck = v.findViewById(R.id.imageViewCheck);
    }

    public void setData(PlanNutrition planNutrition) {

        nameNutrition.setText(planNutrition.getName());
        Utils.loadImage(planNutrition.getPhoto(),photoNutrition,Utils.PLACEHOLDER_GALLERY);
        this.setType(planNutrition);
        this.setImageView(planNutrition);

    }

    private void setType(PlanNutrition planNutrition) {
        switch (planNutrition.getType()) {
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


    private void setImageView(PlanNutrition planNutrition) {
        timeEnd.setVisibility(View.INVISIBLE);
        if (planNutrition.getIsOk().equals(Constants.ModePlan.YES)) {
            imageViewCheck.setVisibility(View.VISIBLE);
        } else if (planNutrition.getIsOk().equals(Constants.ModePlan.NO)) {
            imageViewCheck.setVisibility(View.INVISIBLE);
        }
    }
}
