package com.utad.david.planfit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.GainVolume;
import com.utad.david.planfit.R;

import java.util.List;

public class GainVolumeAdapter extends RecyclerView.Adapter<GainVolumeAdapter.GainVolumeViewHolder>  {

    private List<GainVolume> gainVolumeList;

    public GainVolumeAdapter(List<GainVolume> gainVolumes) {
        this.gainVolumeList = gainVolumes;
    }

    @Override
    public GainVolumeAdapter.GainVolumeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new GainVolumeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GainVolumeViewHolder holder, int position) {
        GainVolume current = gainVolumeList.get(position);
        holder.setData(current);
    }

    @Override
    public int getItemCount() {
        return gainVolumeList.size();
    }

    public static class GainVolumeViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public GainVolumeViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(GainVolume gainVolume){
            nameSlimming.setText(gainVolume.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(gainVolume.getPhoto()).into(photoSlimming);
        }
    }

}
