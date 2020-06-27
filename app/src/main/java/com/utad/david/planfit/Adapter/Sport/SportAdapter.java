package com.utad.david.planfit.Adapter.Sport;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Sport.SportViewHolder;

import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportViewHolder> {

    private List<DefaultSport> sportList;
    private Callback callback;

    public interface Callback {
        void onItemClick(DefaultSport item);
    }

    public SportAdapter(List<DefaultSport> defaultSports, Callback callback) {
        this.sportList = defaultSports;
        this.callback = callback;
    }

    @Override
    public SportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SportViewHolder sportViewHolder, int i) {
        final DefaultSport current = this.sportList.get(i);
        sportViewHolder.setData(current);
        sportViewHolder.itemView.setOnClickListener(v -> {
            if(this.callback != null)
                this.callback.onItemClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return this.sportList.size();
    }
}
