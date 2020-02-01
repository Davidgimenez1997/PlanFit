package com.utad.david.planfit.ViewHolder.Plan.Create.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class CreatePlanSportViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public CreatePlanSportViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameSlimming);
        photoSlimming = v.findViewById(R.id.imageSlimming);
    }

    public void setData(DefaultSport defaultSport){
        nameSlimming.setText(defaultSport.getName());
        Utils.loadImage(defaultSport.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);

    }
}
