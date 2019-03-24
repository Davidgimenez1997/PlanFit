package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.R;

import java.util.List;

public class NutritionGainVolumeAdapter extends RecyclerView.Adapter<NutritionGainVolumeAdapter.GainVolumeViewHolder>{

    private List<NutritionGainVolume> nutritionGainVolumeList;
    private NutritionGainVolumeAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NutritionGainVolume item);
    }

    public NutritionGainVolumeAdapter(List<NutritionGainVolume> nutritionGainVolumes, NutritionGainVolumeAdapter.OnItemClickListener listener) {
        this.nutritionGainVolumeList = nutritionGainVolumes;
        this.listener = listener;
    }

    @Override
    public NutritionGainVolumeAdapter.GainVolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionGainVolumeAdapter.GainVolumeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionGainVolumeAdapter.GainVolumeViewHolder holder, int position) {
        final NutritionGainVolume current = nutritionGainVolumeList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutritionGainVolumeList.size();
    }

    public static class GainVolumeViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public GainVolumeViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(NutritionGainVolume nutritionGainVolume){
            nameSlimming.setText(nutritionGainVolume.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(nutritionGainVolume.getPhoto()).into(photoSlimming);
        }
    }

}
