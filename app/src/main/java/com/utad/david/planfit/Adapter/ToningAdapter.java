package com.utad.david.planfit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.utad.david.planfit.Model.Toning;
import com.utad.david.planfit.R;

import java.util.List;

public class ToningAdapter extends RecyclerView.Adapter<ToningAdapter.ToningViewHolder>  {

    private List<Toning> toningList;

    public ToningAdapter(List<Toning> toningList) {
        this.toningList = toningList;
    }

    @Override
    public ToningAdapter.ToningViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slimming, parent, false);
        return new ToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ToningViewHolder holder, int position) {
        Toning current = toningList.get(position);
        holder.setData(current);
    }

    @Override
    public int getItemCount() {
        return toningList.size();
    }

    public static class ToningViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public ToningViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(Toning toning){
            nameSlimming.setText(toning.getName());
            Glide.with(itemView).load(toning.getPhoto()).into(photoSlimming);
        }
    }

}
