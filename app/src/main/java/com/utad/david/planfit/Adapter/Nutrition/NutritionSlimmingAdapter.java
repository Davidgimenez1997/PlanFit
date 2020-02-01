package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Nutrition.NutritionSlimmingViewHolder;

import java.util.List;

public class NutritionSlimmingAdapter extends RecyclerView.Adapter<NutritionSlimmingViewHolder>  {

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
    public NutritionSlimmingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionSlimmingViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionSlimmingViewHolder holder, int position) {
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

}

