package com.utad.david.planfit.Adapter.Plan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

import java.util.List;

public class CreateNutritionPlanAdapter extends RecyclerView.Adapter<CreateNutritionPlanAdapter.CreatePlanNutritionViewHolder> {

    private List<DefaultNutrition> defaultNutritions;

    private CreateNutritionPlanAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DefaultNutrition item);
    }


    public CreateNutritionPlanAdapter(List<DefaultNutrition> defaultNutritions, CreateNutritionPlanAdapter.OnItemClickListener listener) {
        this.defaultNutritions = defaultNutritions;
        this.listener = listener;
    }

    @Override
    public CreateNutritionPlanAdapter.CreatePlanNutritionViewHolder onCreateViewHolder(ViewGroup parent,
                                                                               int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new CreateNutritionPlanAdapter.CreatePlanNutritionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CreateNutritionPlanAdapter.CreatePlanNutritionViewHolder holder, int position) {
        final DefaultNutrition current = defaultNutritions.get(position);
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
        return defaultNutritions.size();
    }

    public static class CreatePlanNutritionViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public CreatePlanNutritionViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(DefaultNutrition defaultNutrition){
            nameSlimming.setText(defaultNutrition.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(defaultNutrition.getPhoto()).into(photoSlimming);
        }
    }
}


