package com.utad.david.planfit.Adapter.Plan.Show.Nutrition.Details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition.Details.ShowDetailsPlanNutritionViewHolder;

import java.util.ArrayList;

public class ShowDetailsNutritionPlanAdapter extends RecyclerView.Adapter<ShowDetailsPlanNutritionViewHolder> {

    private ArrayList<PlanNutrition> planNutritions;
    private Callback listener;

    public interface Callback {
        void onItemClick(PlanNutrition item);
    }

    public ShowDetailsNutritionPlanAdapter(ArrayList<PlanNutrition> planNutritions, Callback listener) {
        this.planNutritions = planNutritions;
        this.listener = listener;
    }

    @Override
    public ShowDetailsPlanNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_plan, parent, false);
        return new ShowDetailsPlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowDetailsPlanNutritionViewHolder holder, final int position) {
        final PlanNutrition current = this.planNutritions.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.planNutritions.size();
    }
}
