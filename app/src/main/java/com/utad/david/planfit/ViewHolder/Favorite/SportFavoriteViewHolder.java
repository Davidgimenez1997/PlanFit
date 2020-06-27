package com.utad.david.planfit.ViewHolder.Favorite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.utad.david.planfit.Base.BaseViewHolder;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

public class SportFavoriteViewHolder extends BaseViewHolder {

    private TextView nameSportFavorite;
    private ImageView photoSportFavorite;

    public SportFavoriteViewHolder(View v) {
        super(v);
        this.nameSportFavorite = v.findViewById(R.id.nameItemRecycleview);
        this.photoSportFavorite = v.findViewById(R.id.imageItemRecycleview);
    }

    public void setData(DefaultSport defaultSport){
        this.nameSportFavorite.setText(defaultSport.getName());
        Utils.loadImage(defaultSport.getPhoto(), this.photoSportFavorite, Utils.PLACEHOLDER_GALLERY);
    }
}
