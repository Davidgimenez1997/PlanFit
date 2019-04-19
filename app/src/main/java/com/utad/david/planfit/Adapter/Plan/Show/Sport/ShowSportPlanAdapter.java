package com.utad.david.planfit.Adapter.Plan.Show.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ShowSportPlanAdapter extends RecyclerView.Adapter<ShowSportPlanAdapter.ShowPlanSportViewHolder> {

    private ArrayList<ArrayList<PlanSport>> planSports;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ArrayList<PlanSport> defaultSports);
    }

    public ShowSportPlanAdapter(ArrayList<ArrayList<PlanSport>> planSports, OnItemClickListener listener) {
        this.planSports = planSports;
        this.listener = listener;
    }

    @Override
    public ShowPlanSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_show, parent, false);
        return new ShowPlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowPlanSportViewHolder holder, final int position) {
        final ArrayList<PlanSport> current = planSports.get(position);
        holder.setData(current);

        holder.itemView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onItemClick(current);
            }
        });

    }

    @Override
    public int getItemCount() {
        return planSports.size();
    }

    public static class ShowPlanSportViewHolder extends RecyclerView.ViewHolder {

        private TextView timeStart;

        public ShowPlanSportViewHolder(View v) {
            super(v);
            timeStart = v.findViewById(R.id.timeStart);
        }

        public void setData(ArrayList<PlanSport> planSport){

            for(int i=0;i<planSport.size();i++){
                String str_timeStart = String.valueOf(planSport.get(i).getTimeStart());
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
            }

        }

    }
}


