package com.utad.david.planfit.ViewHolder.Sport;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class SportViewHolder extends BaseViewHolder {

    private TextView nameSport;
    private ImageView photoSport;

    public SportViewHolder(View v) {
        super(v);
        this.nameSport = v.findViewById(R.id.nameItemRecycleview);
        this.photoSport = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultSport defaultSport){
        this.nameSport.setText(defaultSport.getName());
        Utils.loadImage(defaultSport.getPhoto(),this.photoSport,Utils.PLACEHOLDER_GALLERY);

    }
}
