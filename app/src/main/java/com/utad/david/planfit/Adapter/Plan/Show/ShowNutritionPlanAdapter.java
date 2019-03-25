package com.utad.david.planfit.Adapter.Plan.Show;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;

import java.util.ArrayList;

public class ShowNutritionPlanAdapter extends RecyclerView.Adapter<ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder> {

    private static String DESAYUNO="Desayuno";
    private static String COMIDA="Comida";
    private static String MERIENDA="Merienda";
    private static String CENA="Cena";

    private ArrayList<PlanNutrition> planNutritions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PlanNutrition item);
    }

    public ShowNutritionPlanAdapter(ArrayList<PlanNutrition> planNutritions, OnItemClickListener listener) {
        this.planNutritions = planNutritions;
        this.listener = listener;
    }

    @Override
    public ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_plan_recycleview, parent, false);
        return new ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowNutritionPlanAdapter.ShowPlanNutritionViewHolder holder, final int position) {
        final PlanNutrition current = planNutritions.get(position);
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

        private TextView nameNutrition;
        private TextView timeStart;
        private TextView timeEnd;
        private ImageView photoNutrition;
        private ImageView imageViewCheck;

        public ShowPlanNutritionViewHolder(View v) {
            super(v);
            nameNutrition = v.findViewById(R.id.titleSport);
            photoNutrition = v.findViewById(R.id.imageViewShowSport);
            timeEnd = v.findViewById(R.id.timeEnd);
            timeStart = v.findViewById(R.id.timeStart);
            imageViewCheck = v.findViewById(R.id.imageViewCheck);
        }

        public void setData(PlanNutrition planNutrition){
            nameNutrition.setText(planNutrition.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(planNutrition.getPhoto()).into(photoNutrition);
            switch (planNutrition.getType()){
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
            timeEnd.setVisibility(View.INVISIBLE);

            if(planNutrition.getIsOk().equals("yes")){
                imageViewCheck.setVisibility(View.VISIBLE);
            }else if(planNutrition.getIsOk().equals("no")){
                imageViewCheck.setVisibility(View.INVISIBLE);
            }
        }
    }
}