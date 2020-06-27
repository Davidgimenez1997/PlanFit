package com.utad.david.planfit.Adapter.Plan.Show.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Plan.Show.Sport.ShowDetailPlanSportViewHolder;
import java.util.ArrayList;

public class ShowDetailsSportPlanAdapter extends RecyclerView.Adapter<ShowDetailPlanSportViewHolder> {

    private ArrayList<PlanSport> planSports;
    private Callback listener;

    public interface Callback {
        void onItemClick(PlanSport item);
    }

    public ShowDetailsSportPlanAdapter(ArrayList<PlanSport> planSports, Callback listener) {
        this.planSports = planSports;
        this.listener = listener;
    }

    @Override
    public ShowDetailPlanSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_plan, parent, false);
        return new ShowDetailPlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ShowDetailPlanSportViewHolder holder, final int position) {
        final PlanSport current = this.planSports.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.planSports.size();
    }
}
