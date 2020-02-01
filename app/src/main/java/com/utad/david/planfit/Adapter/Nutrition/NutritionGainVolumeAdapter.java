package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Nutrition.NutritionGainVolumeViewHolder;
import java.util.List;

public class NutritionGainVolumeAdapter extends RecyclerView.Adapter<NutritionGainVolumeViewHolder>{

    private List<NutritionGainVolume> nutritionGainVolumeList;
    private Callback listener;

    public interface Callback {
        void onItemClick(NutritionGainVolume item);
    }

    public NutritionGainVolumeAdapter(List<NutritionGainVolume> nutritionGainVolumes, Callback listener) {
        this.nutritionGainVolumeList = nutritionGainVolumes;
        this.listener = listener;
    }

    @Override
    public NutritionGainVolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionGainVolumeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionGainVolumeViewHolder holder, int position) {
        final NutritionGainVolume current = nutritionGainVolumeList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return nutritionGainVolumeList.size();
    }

}
