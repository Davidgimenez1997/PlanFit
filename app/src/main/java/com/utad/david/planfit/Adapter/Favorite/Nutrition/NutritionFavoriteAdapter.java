package com.utad.david.planfit.Adapter.Favorite.Nutrition;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;

import java.util.List;

public class NutritionFavoriteAdapter extends RecyclerView.Adapter<NutritionFavoriteAdapter.NutritionFavoriteViewHolder> {

    private List<DefaultNutrition> defaultNutritions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DefaultNutrition item);
    }

    public NutritionFavoriteAdapter(List<DefaultNutrition> defaultNutritions,OnItemClickListener listener) {
        this.defaultNutritions = defaultNutritions;
        this.listener = listener;
    }

    public void dataChangedDeleteSport(List<DefaultNutrition> defaultNutritions){
        this.defaultNutritions.clear();
        this.defaultNutritions.addAll(defaultNutritions);
        notifyDataSetChanged();
    }

    @Override
    public NutritionFavoriteAdapter.NutritionFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new NutritionFavoriteAdapter.NutritionFavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(NutritionFavoriteAdapter.NutritionFavoriteViewHolder holder, int position) {
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

    public static class NutritionFavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public NutritionFavoriteViewHolder(View v) {
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
