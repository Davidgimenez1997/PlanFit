package com.utad.david.planfit.Adapter.Plan.Show.Nutrition;

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
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;

public class ShowDetailsNutritionPlanAdapter extends RecyclerView.Adapter<ShowDetailsNutritionPlanAdapter.ShowDetailsPlanNutritionViewHolder> {

    private ArrayList<PlanNutrition> planNutritions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PlanNutrition item);
    }

    public ShowDetailsNutritionPlanAdapter(ArrayList<PlanNutrition> planNutritions, OnItemClickListener listener) {
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
        final PlanNutrition current = planNutritions.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planNutritions.size();
    }

    public static class ShowDetailsPlanNutritionViewHolder extends RecyclerView.ViewHolder {

        private TextView nameNutrition;
        private TextView timeStart;
        private TextView timeEnd;
        private ImageView photoNutrition;
        private ImageView imageViewCheck;

        public ShowDetailsPlanNutritionViewHolder(View v) {
            super(v);
            nameNutrition = v.findViewById(R.id.titleSport);
            photoNutrition = v.findViewById(R.id.imageViewShowSport);
            timeEnd = v.findViewById(R.id.timeEnd);
            timeStart = v.findViewById(R.id.timeStart);
            imageViewCheck = v.findViewById(R.id.imageViewCheck);
        }

        public void setData(PlanNutrition planNutrition) {
            nameNutrition.setText(planNutrition.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(planNutrition.getPhoto()).into(photoNutrition);
            switch (planNutrition.getType()) {
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
            timeEnd.setVisibility(View.INVISIBLE);

            if (planNutrition.getIsOk().equals(Constants.ModePlan.YES)) {
                imageViewCheck.setVisibility(View.VISIBLE);
            } else if (planNutrition.getIsOk().equals(Constants.ModePlan.NO)) {
                imageViewCheck.setVisibility(View.INVISIBLE);
            }
        }
    }
}
