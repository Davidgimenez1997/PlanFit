package com.utad.david.planfit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.Slimming;
import com.utad.david.planfit.R;

import java.util.List;

public class SlimmingAdapter extends RecyclerView.Adapter<SlimmingAdapter.SlimmingViewHolder>  {

    private List<Slimming> slimmingListm;

    public SlimmingAdapter(List<Slimming> slimmingListm) {
        this.slimmingListm = slimmingListm;
    }

    @Override
    public SlimmingAdapter.SlimmingViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SlimmingViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SlimmingViewHolder holder, int position) {
        Slimming current = slimmingListm.get(position);
        holder.setData(current);
    }

    @Override
    public int getItemCount() {
        return slimmingListm.size();
    }

    public static class SlimmingViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public SlimmingViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(Slimming slimming){
            nameSlimming.setText(slimming.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(slimming.getPhoto()).into(photoSlimming);
        }
    }

}
