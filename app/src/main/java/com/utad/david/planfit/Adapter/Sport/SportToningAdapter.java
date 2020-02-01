package com.utad.david.planfit.Adapter.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Sport.SportToningViewHolder;

import java.util.List;

public class SportToningAdapter extends RecyclerView.Adapter<SportToningViewHolder>  {

    private List<SportToning> sportToningList;
    private Callback listener;

    public interface Callback {
        void onItemClick(SportToning item);
    }

    public SportToningAdapter(List<SportToning> sportToningList, Callback listener) {
        this.sportToningList = sportToningList;
        this.listener = listener;
    }

    @Override
    public SportToningViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportToningViewHolder holder, int position) {
        final SportToning current = sportToningList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener != null)
                listener.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return sportToningList.size();
    }
}
