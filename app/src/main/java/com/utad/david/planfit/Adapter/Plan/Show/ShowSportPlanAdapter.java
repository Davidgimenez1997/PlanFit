package com.utad.david.planfit.Adapter.Plan.Show;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.util.ArrayList;

public class ShowSportPlanAdapter extends RecyclerView.Adapter<ShowSportPlanAdapter.ShowPlanSportViewHolder> {

    private ArrayList<PlanSport> planSports;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PlanSport item);
    }

    public ShowSportPlanAdapter(ArrayList<PlanSport> planSports, OnItemClickListener listener) {
        this.planSports = planSports;
        this.listener = listener;
    }

    @Override
    public ShowSportPlanAdapter.ShowPlanSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_sport_recycleview, parent, false);
        return new ShowSportPlanAdapter.ShowPlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowSportPlanAdapter.ShowPlanSportViewHolder holder, int position) {
        final PlanSport current = planSports.get(position);
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
        return planSports.size();
    }

    public static class ShowPlanSportViewHolder extends RecyclerView.ViewHolder {

        private TextView nameSport;
        private TextView timeStart;
        private TextView timeEnd;
        private ImageView photoSport;

        public ShowPlanSportViewHolder(View v) {
            super(v);
            nameSport = v.findViewById(R.id.titleSport);
            photoSport = v.findViewById(R.id.imageViewShowSport);
            timeEnd = v.findViewById(R.id.timeEnd);
            timeStart = v.findViewById(R.id.timeStart);
        }

        public void setData(PlanSport planSport){
            nameSport.setText(planSport.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(planSport.getPhoto()).into(photoSport);
            String str_timeStart = String.valueOf(planSport.getTimeStart());
            String str_timeEnd = String.valueOf(planSport.getTimeEnd());
            timeStart.setText(str_timeStart+":00");
            timeEnd.setText(str_timeEnd+":00");
        }
    }
}


