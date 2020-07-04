package com.utad.david.planfit.ViewHolder.Nutrition;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class NutritionViewHolder extends BaseViewHolder {

    private TextView nameSport;
    private ImageView photoSport;

    public NutritionViewHolder(View v) {
        super(v);
        this.nameSport = v.findViewById(R.id.nameItemRecycleview);
        this.photoSport = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultNutrition defaultNutrition){
        this.nameSport.setText(defaultNutrition.getName());
        Utils.loadImage(defaultNutrition.getPhoto(), this.photoSport, Utils.PLACEHOLDER_GALLERY);

    }
}
