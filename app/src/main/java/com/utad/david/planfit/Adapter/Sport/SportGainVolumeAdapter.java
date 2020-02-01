package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Sport.SportGainVolumeViewHolder;

import java.util.List;

public class SportGainVolumeAdapter extends RecyclerView.Adapter<SportGainVolumeViewHolder>  {

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
    public SportGainVolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportGainVolumeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportGainVolumeViewHolder holder, int position) {
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

}
