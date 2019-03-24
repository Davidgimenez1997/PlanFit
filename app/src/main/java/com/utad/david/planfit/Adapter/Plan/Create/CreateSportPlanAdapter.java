package com.utad.david.planfit.Adapter.Plan.Create;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

import java.util.List;

public class CreateSportPlanAdapter extends RecyclerView.Adapter<CreateSportPlanAdapter.CreatePlanSportViewHolder> {

    private List<DefaultSport> defaultSports;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DefaultSport item);
    }

    public CreateSportPlanAdapter(List<DefaultSport> defaultSports, OnItemClickListener listener) {
        this.defaultSports = defaultSports;
        this.listener = listener;
    }

    @Override
    public CreateSportPlanAdapter.CreatePlanSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new CreateSportPlanAdapter.CreatePlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CreateSportPlanAdapter.CreatePlanSportViewHolder holder, int position) {
        final DefaultSport current = defaultSports.get(position);
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
        return defaultSports.size();
    }

    public static class CreatePlanSportViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public CreatePlanSportViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(DefaultSport defaultSport){
            nameSlimming.setText(defaultSport.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(defaultSport.getPhoto()).into(photoSlimming);
        }
    }
}

