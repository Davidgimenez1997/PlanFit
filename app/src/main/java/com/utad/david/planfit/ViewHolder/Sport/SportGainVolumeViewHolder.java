package com.utad.david.planfit.ViewHolder.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class SportGainVolumeViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public SportGainVolumeViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameSlimming);
        photoSlimming = v.findViewById(R.id.imageSlimming);
    }

    public void setData(SportGainVolume sportGainVolume){
        nameSlimming.setText(sportGainVolume.getName());
        Utils.loadImage(sportGainVolume.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);

    }
}
