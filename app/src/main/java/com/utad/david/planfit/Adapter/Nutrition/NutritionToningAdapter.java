package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Nutrition.NutritionToningViewHolder;

import java.util.List;

public class NutritionToningAdapter extends RecyclerView.Adapter<NutritionToningViewHolder>  {

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
    public NutritionToningViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionToningViewHolder holder, int position) {
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

}
