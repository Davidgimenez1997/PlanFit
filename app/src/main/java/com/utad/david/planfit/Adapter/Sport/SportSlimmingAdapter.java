package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.R;

import java.util.List;

public class SportSlimmingAdapter extends RecyclerView.Adapter<SportSlimmingAdapter.SlimmingViewHolder>  {

    private List<SportSlimming> sportSlimmingListm;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SportSlimming item);
    }

    public SportSlimmingAdapter(List<SportSlimming> sportSlimmingListm, OnItemClickListener listener) {
        this.sportSlimmingListm = sportSlimmingListm;
        this.listener = listener;
    }

    @Override
    public SlimmingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SlimmingViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SlimmingViewHolder holder, int position) {
        final SportSlimming current = sportSlimmingListm.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return sportSlimmingListm.size();
    }

    public static class SlimmingViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public SlimmingViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(SportSlimming sportSlimming){
            nameSlimming.setText(sportSlimming.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(sportSlimming.getPhoto()).into(photoSlimming);
        }
    }

}
