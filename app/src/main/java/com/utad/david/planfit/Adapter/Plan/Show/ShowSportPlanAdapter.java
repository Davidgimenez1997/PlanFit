package com.utad.david.planfit.Adapter.Plan.Show;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.math.BigDecimal;
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
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_plan_recycleview, parent, false);
        return new ShowSportPlanAdapter.ShowPlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowSportPlanAdapter.ShowPlanSportViewHolder holder, final int position) {
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
        private ImageView imageViewCheck;

        public ShowPlanSportViewHolder(View v) {
            super(v);
            nameSport = v.findViewById(R.id.titleSport);
            photoSport = v.findViewById(R.id.imageViewShowSport);
            timeEnd = v.findViewById(R.id.timeEnd);
            timeStart = v.findViewById(R.id.timeStart);
            imageViewCheck = v.findViewById(R.id.imageViewCheck);
        }

        public void setData(PlanSport planSport){
            nameSport.setText(planSport.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(planSport.getPhoto()).into(photoSport);

            String str_timeStart = String.valueOf(planSport.getTimeStart());
            BigDecimal bigDecimal_start = new BigDecimal(str_timeStart);
            long first_start = bigDecimal_start.longValue();
            BigDecimal second_start = bigDecimal_start.remainder(BigDecimal.ONE);
            StringBuilder stringBuilder_start = new StringBuilder(second_start.toString());
            stringBuilder_start.delete(0,2);
            if(stringBuilder_start.toString().length()==1){
                timeStart.setText(Long.valueOf(first_start)+":"+stringBuilder_start.toString()+"0");
            }else{
                timeStart.setText(Long.valueOf(first_start)+":"+stringBuilder_start.toString());
            }

            String str_timeEnd = String.valueOf(planSport.getTimeEnd());
            BigDecimal bigDecimal_end = new BigDecimal(str_timeEnd);
            long first_end = bigDecimal_end.longValue();
            BigDecimal second_End = bigDecimal_end.remainder(BigDecimal.ONE);
            StringBuilder stringBuilder_end = new StringBuilder(second_End.toString());
            stringBuilder_end.delete(0,2);
            if(stringBuilder_end.toString().length()==1){
                timeEnd.setText(Long.valueOf(first_end)+":"+stringBuilder_end.toString()+"0");
            }else{
                timeEnd.setText(Long.valueOf(first_end)+":"+stringBuilder_end.toString());
            }

            if(planSport.getIsOk().equals("yes")){
                imageViewCheck.setVisibility(View.VISIBLE);
            }else if(planSport.getIsOk().equals("no")){
                imageViewCheck.setVisibility(View.INVISIBLE);
            }
        }

    }
}


