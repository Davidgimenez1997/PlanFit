package com.utad.david.planfit.ViewHolder.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class NutritionGainVolumeViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public NutritionGainVolumeViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameItemRecycleview);
        photoSlimming = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(NutritionGainVolume nutritionGainVolume){
        nameSlimming.setText(nutritionGainVolume.getName());
        Utils.loadImage(nutritionGainVolume.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
    }
}
