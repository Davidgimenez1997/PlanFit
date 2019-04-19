package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;

import java.util.List;

public class SportToningAdapter extends RecyclerView.Adapter<SportToningAdapter.ToningViewHolder>  {

    private List<SportToning> sportToningList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SportToning item);
    }

    public SportToningAdapter(List<SportToning> sportToningList, OnItemClickListener listener) {
        this.sportToningList = sportToningList;
        this.listener = listener;
    }

    @Override
    public ToningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new ToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ToningViewHolder holder, int position) {
        final SportToning current = sportToningList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return sportToningList.size();
    }

    public static class ToningViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public ToningViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(SportToning sportToning){
            nameSlimming.setText(sportToning.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(sportToning.getPhoto()).into(photoSlimming);
        }
    }

}
