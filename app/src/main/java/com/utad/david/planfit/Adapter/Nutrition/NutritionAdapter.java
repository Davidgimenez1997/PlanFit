package com.utad.david.planfit.Adapter.Nutrition;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Nutrition.NutritionViewHolder;

import java.util.List;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionViewHolder> {

    private List<DefaultNutrition> nutritionList;
    private Callback callback;

    public interface Callback {
        void onItemClick(DefaultNutrition item);
    }

    public NutritionAdapter(List<DefaultNutrition> defaultSports, Callback callback) {
        this.nutritionList = defaultSports;
        this.callback = callback;
    }

    @Override
    public NutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NutritionViewHolder sportViewHolder, int i) {
        final DefaultNutrition current = this.nutritionList.get(i);
        sportViewHolder.setData(current);
        sportViewHolder.itemView.setOnClickListener(v -> {
            if(this.callback != null)
                this.callback.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return this.nutritionList.size();
    }
}
