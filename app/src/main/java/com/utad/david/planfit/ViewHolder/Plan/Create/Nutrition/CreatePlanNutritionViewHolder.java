package com.utad.david.planfit.ViewHolder.Plan.Create.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class CreatePlanNutritionViewHolder extends BaseViewHolder {

    public TextView namePlanNutrition;
    private ImageView photoPlanNutrition;

    public CreatePlanNutritionViewHolder(View v) {
        super(v);
        this.namePlanNutrition = v.findViewById(R.id.nameItemRecycleview);
        this.photoPlanNutrition = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultNutrition defaultNutrition){
        this.namePlanNutrition.setText(defaultNutrition.getName());
        Utils.loadImage(defaultNutrition.getPhoto(), this.photoPlanNutrition, Utils.PLACEHOLDER_GALLERY);
    }
}
