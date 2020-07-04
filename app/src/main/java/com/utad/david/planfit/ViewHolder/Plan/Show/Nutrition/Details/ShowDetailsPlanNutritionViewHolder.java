package com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition.Details;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

public class ShowDetailsPlanNutritionViewHolder extends BaseViewHolder {

    private TextView name;
    private TextView timeStart;
    private TextView timeEnd;
    private ImageView photo;
    private ImageView imageViewCheck;

    public ShowDetailsPlanNutritionViewHolder(View v) {
        super(v);
        this.name = v.findViewById(R.id.titleSport);
        this.photo = v.findViewById(R.id.imageViewShowSport);
        this.timeEnd = v.findViewById(R.id.timeEnd);
        this.timeStart = v.findViewById(R.id.timeStart);
        this.imageViewCheck = v.findViewById(R.id.imageViewCheck);
    }

    public void setData(PlanNutrition planNutrition) {
        this.name.setText(planNutrition.getName());
        Utils.loadImage(planNutrition.getPhoto(), this.photo, Utils.PLACEHOLDER_GALLERY);
        this.timeStart.setText(this.getType(planNutrition));
        this.setImageView(planNutrition);
    }

    private String getType(PlanNutrition planNutrition) {
        String aux = "";
        switch (planNutrition.getType()) {
            case Constants.TypesPlanNutrition.MODE_DESAYUNO:
                aux = Constants.TypesPlanNutrition.DESAYUNO;
                break;
            case Constants.TypesPlanNutrition.MODE_COMIDA:
                aux = Constants.TypesPlanNutrition.COMIDA;
                break;
            case Constants.TypesPlanNutrition.MODE_MERIENDA:
                aux = Constants.TypesPlanNutrition.MERIENDA;
                break;
            case Constants.TypesPlanNutrition.MODE_CENA:
                aux = Constants.TypesPlanNutrition.CENA;
                break;
        }
        return aux;
    }


    private void setImageView(PlanNutrition planNutrition) {
        this.timeEnd.setVisibility(View.INVISIBLE);
        if (planNutrition.getIsOk().equals(Constants.ModePlan.YES)) {
            this.imageViewCheck.setVisibility(View.VISIBLE);
        } else if (planNutrition.getIsOk().equals(Constants.ModePlan.NO)) {
            this.imageViewCheck.setVisibility(View.INVISIBLE);
        }
    }
}
