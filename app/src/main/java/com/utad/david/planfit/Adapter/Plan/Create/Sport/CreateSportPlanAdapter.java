package com.utad.david.planfit.Adapter.Plan.Create.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Plan.Create.Sport.CreatePlanSportViewHolder;
import java.util.List;

public class CreateSportPlanAdapter extends RecyclerView.Adapter<CreatePlanSportViewHolder> {

    private List<DefaultSport> defaultSports;
    private Callback listener;

    public interface Callback {
        void onItemClick(DefaultSport item);
    }

    public CreateSportPlanAdapter(List<DefaultSport> defaultSports, Callback listener) {
        this.defaultSports = defaultSports;
        this.listener = listener;
    }

    @Override
    public CreatePlanSportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new CreatePlanSportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(CreatePlanSportViewHolder holder, int position) {
        final DefaultSport current = this.defaultSports.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (this.listener != null) {
                this.listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.defaultSports.size();
    }
}

