package com.utad.david.planfit.Adapter.Plan.Show.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Plan.Show.Sport.ShowPlanSportViewHolder;
import java.util.ArrayList;

public class ShowSportPlanAdapter extends RecyclerView.Adapter<ShowPlanSportViewHolder> {

    private ArrayList<ArrayList<PlanSport>> planSports;
    private Callback listener;

    public interface Callback {
        void onItemClick(ArrayList<PlanSport> defaultSports);
    }

    public ShowSportPlanAdapter(ArrayList<ArrayList<PlanSport>> planSports, Callback listener) {
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

}


