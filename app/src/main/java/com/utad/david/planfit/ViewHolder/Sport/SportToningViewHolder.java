package com.utad.david.planfit.ViewHolder.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class SportToningViewHolder extends BaseViewHolder {

    private TextView nameSlimming;
    private ImageView photoSlimming;

    public SportToningViewHolder(View v) {
        super(v);
        nameSlimming = v.findViewById(R.id.nameSlimming);
        photoSlimming = v.findViewById(R.id.imageSlimming);
    }

    public void setData(SportToning sportToning){
        nameSlimming.setText(sportToning.getName());
        Utils.loadImage(sportToning.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);

    }
}
