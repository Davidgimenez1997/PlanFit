package com.utad.david.planfit.ViewHolder.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class SportSlimmingViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public SportSlimmingViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameSlimming);
        photoSlimming = v.findViewById(R.id.imageSlimming);
    }

    public void setData(SportSlimming sportSlimming){
        nameSlimming.setText(sportSlimming.getName());
        Utils.loadImage(sportSlimming.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
    }
}
