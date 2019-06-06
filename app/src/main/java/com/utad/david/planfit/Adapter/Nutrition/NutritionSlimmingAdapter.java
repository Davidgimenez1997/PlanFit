package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

import java.util.List;

public class NutritionSlimmingAdapter extends RecyclerView.Adapter<NutritionSlimmingAdapter.SlimmingViewHolder>  {

    private List<NutritionSlimming> nutritionSlimmingList;
    private Callback listener;

    public interface Callback {
        void onItemClick(NutritionSlimming item);
    }

    public NutritionSlimmingAdapter(List<NutritionSlimming> nutritionSlimmings, Callback listener) {
        this.nutritionSlimmingList = nutritionSlimmings;
        this.listener = listener;
    }

    @Override
    public SlimmingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SlimmingViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SlimmingViewHolder holder, int position) {
        final NutritionSlimming current = nutritionSlimmingList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return nutritionSlimmingList.size();
    }

    public static class SlimmingViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public SlimmingViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(NutritionSlimming nutritionSlimming){
            nameSlimming.setText(nutritionSlimming.getName());
            Utils.loadImage(nutritionSlimming.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
        }
    }

}

