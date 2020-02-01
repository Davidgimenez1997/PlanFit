package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Sport.SportSlimmingViewHolder;

import java.util.List;

public class SportSlimmingAdapter extends RecyclerView.Adapter<SportSlimmingViewHolder>  {

    private List<SportSlimming> sportSlimmingListm;
    private Callback listener;

    public interface Callback {
        void onItemClick(SportSlimming item);
    }

    public SportSlimmingAdapter(List<SportSlimming> sportSlimmingListm, Callback listener) {
        this.sportSlimmingListm = sportSlimmingListm;
        this.listener = listener;
    }

    @Override
    public SportSlimmingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportSlimmingViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportSlimmingViewHolder holder, int position) {
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

}
