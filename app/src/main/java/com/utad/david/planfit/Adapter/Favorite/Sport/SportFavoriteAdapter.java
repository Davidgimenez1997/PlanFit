package com.utad.david.planfit.Adapter.Favorite.Sport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.ViewHolder.Favorite.SportFavoriteViewHolder;
import java.util.List;

public class SportFavoriteAdapter extends RecyclerView.Adapter<SportFavoriteViewHolder> {

    private List<DefaultSport> defaultSports;
    private Callback listener;

    public interface Callback {
        void onItemClick(DefaultSport item);
    }

    public SportFavoriteAdapter(List<DefaultSport> defaultSports, Callback listener) {
        this.defaultSports = defaultSports;
        this.listener = listener;
    }

    public void dataChangedDeleteSport(List<DefaultSport> defaultSports){
        this.defaultSports.clear();
        this.defaultSports.addAll(defaultSports);
        notifyDataSetChanged();
    }

    @Override
    public SportFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportFavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportFavoriteViewHolder holder, int position) {
        final DefaultSport current = defaultSports.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return defaultSports.size();
    }

}
