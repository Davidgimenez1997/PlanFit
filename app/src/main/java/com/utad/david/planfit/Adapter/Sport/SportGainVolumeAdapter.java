package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

import java.util.List;

public class SportGainVolumeAdapter extends RecyclerView.Adapter<SportGainVolumeAdapter.GainVolumeViewHolder>  {

    private List<SportGainVolume> sportGainVolumeList;
    private Callback listener;

    public interface Callback {
        void onItemClick(SportGainVolume item);
    }

    public SportGainVolumeAdapter(List<SportGainVolume> sportGainVolumes, Callback listener) {
        this.sportGainVolumeList = sportGainVolumes;
        this.listener = listener;
    }

    @Override
    public GainVolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new GainVolumeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(GainVolumeViewHolder holder, int position) {
        final SportGainVolume current = sportGainVolumeList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return sportGainVolumeList.size();
    }

    public static class GainVolumeViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public GainVolumeViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(SportGainVolume sportGainVolume){
            nameSlimming.setText(sportGainVolume.getName());
            Utils.loadImage(sportGainVolume.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);

        }
    }

}
