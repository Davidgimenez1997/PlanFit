package com.utad.david.planfit.ViewHolder.Favorite.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class NutritionFavoriteViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public NutritionFavoriteViewHolder(View v) {
        super(v);
        this.nameSlimming = v.findViewById(R.id.nameItemRecycleview);
        this.photoSlimming = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultNutrition defaultNutrition){
        this.nameSlimming.setText(defaultNutrition.getName());
        Utils.loadImage(defaultNutrition.getPhoto(), this.photoSlimming, Utils.PLACEHOLDER_GALLERY);
    }
}
