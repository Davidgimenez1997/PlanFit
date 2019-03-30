package com.utad.david.planfit.Adapter.Plan.Show.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;

import java.util.ArrayList;

public class ShowNutritionPlanAdapter extends RecyclerView.Adapter<ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder> {

    private static String DESAYUNO="Desayuno";
    private static String COMIDA="Comida";
    private static String MERIENDA="Merienda";
    private static String CENA="Cena";

    private ArrayList<ArrayList<PlanNutrition>> planNutritions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ArrayList<PlanNutrition> item);
    }

    public ShowNutritionPlanAdapter(ArrayList<ArrayList<PlanNutrition>> planNutritions, OnItemClickListener listener) {
        this.planNutritions = planNutritions;
        this.listener = listener;
    }

    @Override
    public ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_show, parent, false);
        return new ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder holder, final int position) {
        ArrayList<PlanNutrition> current = planNutritions.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(current);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return planNutritions.size();
    }

    public static class ShowPlanNutritionViewHolder extends RecyclerView.ViewHolder {

        private TextView timeStart;

        public ShowPlanNutritionViewHolder(View v) {
            super(v);
            timeStart = v.findViewById(R.id.timeStart);
        }

        public void setData(ArrayList<PlanNutrition> planNutrition){

            for(int i=0;i<planNutrition.size();i++){

                switch (planNutrition.get(i).getType()){
                    case 1:
                        timeStart.setText(DESAYUNO);
                        break;
                    case 2:
                        timeStart.setText(COMIDA);
                        break;
                    case 3:
                        timeStart.setText(MERIENDA);
                        break;
                    case 4:
                        timeStart.setText(CENA);
                        break;

                }
            }


        }
    }
}