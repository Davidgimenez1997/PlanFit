package com.utad.david.planfit.ViewHolder.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class NutritionToningViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public NutritionToningViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameItemRecycleview);
        photoSlimming = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(NutritionToning nutritionToning){
        nameSlimming.setText(nutritionToning.getName());
        Utils.loadImage(nutritionToning.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
    }
}
