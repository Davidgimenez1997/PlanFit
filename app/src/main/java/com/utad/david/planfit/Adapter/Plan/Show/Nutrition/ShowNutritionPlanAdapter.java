package com.utad.david.planfit.Adapter.Plan.Show.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Plan.Show.Nutrition.ShowPlanNutritionViewHolder;

import java.util.ArrayList;

public class ShowNutritionPlanAdapter extends RecyclerView.Adapter<ShowPlanNutritionViewHolder> {

    private ArrayList<ArrayList<PlanNutrition>> planNutritions;
    private Callback listener;

    public interface Callback {
        void onItemClick(ArrayList<PlanNutrition> item);
    }

    public ShowNutritionPlanAdapter(ArrayList<ArrayList<PlanNutrition>> planNutritions, Callback listener) {
        this.planNutritions = planNutritions;
        this.listener = listener;
    }

    @Override
    public ShowPlanNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_show, parent, false);
        return new ShowPlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowPlanNutritionViewHolder holder, final int position) {
        ArrayList<PlanNutrition> current = this.planNutritions.get(position);
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