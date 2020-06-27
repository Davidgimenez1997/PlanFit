package com.utad.david.planfit.ViewHolder.Plan.Create.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class CreatePlanSportViewHolder extends BaseViewHolder {

    private TextView nameSportPlan;
    private ImageView photoSportPlan;

    public CreatePlanSportViewHolder(View v) {
        super(v);
        this.nameSportPlan = v.findViewById(R.id.nameItemRecycleview);
        this.photoSportPlan = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultSport defaultSport){
        this.nameSportPlan.setText(defaultSport.getName());
        Utils.loadImage(defaultSport.getPhoto(), this.photoSportPlan, Utils.PLACEHOLDER_GALLERY);
    }
}
