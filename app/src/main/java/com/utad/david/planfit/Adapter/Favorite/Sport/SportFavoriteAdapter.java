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
    private Callback callback;

    public interface Callback {
        void onItemClick(DefaultSport item);
    }

    public SportFavoriteAdapter(List<DefaultSport> defaultSports, Callback callback) {
        this.defaultSports = defaultSports;
        this.callback = callback;
    }

    public void dataChangedDeleteSport(DefaultSport item){
        this.defaultSports.remove(item);
        this.notifyDataSetChanged();
    }

    public List<DefaultSport> getDefaultSports() {
        return this.defaultSports;
    }

    @Override
    public SportFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportFavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportFavoriteViewHolder holder, int position) {
        final DefaultSport current = this.defaultSports.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.defaultSports.size();
    }

}
