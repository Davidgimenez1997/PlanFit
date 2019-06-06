package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Utils;

import java.util.List;

public class NutritionToningAdapter extends RecyclerView.Adapter<NutritionToningAdapter.ToningViewHolder>  {

    private List<NutritionToning> nutritionTonings;
    private Callback listener;

    public interface Callback {
        void onItemClick(NutritionToning item);
    }

    public NutritionToningAdapter(List<NutritionToning> nutritionTonings, Callback listener) {
        this.nutritionTonings = nutritionTonings;
        this.listener = listener;
    }

    @Override
    public ToningViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new ToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ToningViewHolder holder, int position) {
        final NutritionToning current = nutritionTonings.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return nutritionTonings.size();
    }

    public static class ToningViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public ToningViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(NutritionToning nutritionToning){
            nameSlimming.setText(nutritionToning.getName());
            Utils.loadImage(nutritionToning.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
        }
    }

}
