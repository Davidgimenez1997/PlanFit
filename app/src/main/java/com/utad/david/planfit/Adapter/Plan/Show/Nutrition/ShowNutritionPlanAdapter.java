package com.utad.david.planfit.Adapter.Plan.Show.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;

import static com.utad.david.planfit.Utils.Constants.TiposPlanNutricion.COMIDA;

public class ShowNutritionPlanAdapter extends RecyclerView.Adapter<ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder> {

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
    public ShowPlanNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_show, parent, false);
        return new ShowPlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowPlanNutritionViewHolder holder, final int position) {
        ArrayList<PlanNutrition> current = planNutritions.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onItemClick(current);
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
                    case Constants.TiposPlanNutricion.MODE_DESAYUNO:
                        timeStart.setText(Constants.TiposPlanNutricion.DESAYUNO);
                        break;
                    case Constants.TiposPlanNutricion.MODE_COMIDA:
                        timeStart.setText(Constants.TiposPlanNutricion.COMIDA);
                        break;
                    case Constants.TiposPlanNutricion.MODE_MERIENDA:
                        timeStart.setText(Constants.TiposPlanNutricion.MERIENDA);
                        break;
                    case Constants.TiposPlanNutricion.MODE_CENA:
                        timeStart.setText(Constants.TiposPlanNutricion.CENA);
                        break;
                }
            }
        }
    }
}