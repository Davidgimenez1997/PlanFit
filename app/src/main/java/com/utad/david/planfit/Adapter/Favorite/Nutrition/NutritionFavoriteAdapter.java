package com.utad.david.planfit.Adapter.Favorite.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Favorite.NutritionFavoriteViewHolder;
import java.util.List;

public class NutritionFavoriteAdapter extends RecyclerView.Adapter<NutritionFavoriteViewHolder> {

    private List<DefaultNutrition> defaultNutritions;
    private Callback callback;

    public interface Callback {
        void onItemClick(DefaultNutrition item);
    }

    public NutritionFavoriteAdapter(List<DefaultNutrition> defaultNutritions, Callback callback) {
        this.defaultNutritions = defaultNutritions;
        this.callback = callback;
    }

    public void dataChangedDeleteSport(List<DefaultNutrition> defaultNutritions){
        this.defaultNutritions.clear();
        this.defaultNutritions.addAll(defaultNutritions);
        this.notifyDataSetChanged();
    }

    @Override
    public NutritionFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionFavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionFavoriteViewHolder holder, int position) {
        final DefaultNutrition current = defaultNutritions.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (callback != null ){
                callback.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return defaultNutritions.size();
    }
}

