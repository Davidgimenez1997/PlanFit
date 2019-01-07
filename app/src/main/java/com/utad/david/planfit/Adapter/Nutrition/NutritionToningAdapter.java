package com.utad.david.planfit.Adapter.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;

import java.util.List;

public class NutritionToningAdapter extends RecyclerView.Adapter<NutritionToningAdapter.ToningViewHolder>  {

    private List<NutritionToning> nutritionTonings;
    private NutritionToningAdapter.OnItemClickListener listener;

    //Obtenemos información del item
    public interface OnItemClickListener {
        void onItemClick(NutritionToning item);
    }

    public NutritionToningAdapter(List<NutritionToning> nutritionTonings, NutritionToningAdapter.OnItemClickListener listener) {
        this.nutritionTonings = nutritionTonings;
        this.listener = listener;
    }

    @Override
    public NutritionToningAdapter.ToningViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionToningAdapter.ToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionToningAdapter.ToningViewHolder holder, int position) {
        final NutritionToning current = nutritionTonings.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutritionTonings.size();
    }

    public static class ToningViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public ToningViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(NutritionToning nutritionToning){
            nameSlimming.setText(nutritionToning.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(nutritionToning.getPhoto()).into(photoSlimming);
        }
    }

}
