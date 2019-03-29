package com.utad.david.planfit.Adapter.Favorite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

import java.util.List;

public class SportFavoriteAdapter extends RecyclerView.Adapter<SportFavoriteAdapter.SportFavoriteViewHolder> {

    private List<DefaultSport> defaultSports;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DefaultSport item);
    }

    public SportFavoriteAdapter(List<DefaultSport> defaultSports,OnItemClickListener listener) {
        this.defaultSports = defaultSports;
        this.listener = listener;
    }

    public void dataChangedDeleteSport(List<DefaultSport> defaultSports){
        this.defaultSports.clear();
        this.defaultSports.addAll(defaultSports);
        notifyDataSetChanged();
    }

    @Override
    public SportFavoriteAdapter.SportFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new SportFavoriteAdapter.SportFavoriteViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SportFavoriteAdapter.SportFavoriteViewHolder holder, int position) {
        final DefaultSport current = defaultSports.get(position);
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
        return defaultSports.size();
    }

    public static class SportFavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public SportFavoriteViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(DefaultSport defaultSport){
            nameSlimming.setText(defaultSport.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(defaultSport.getPhoto()).into(photoSlimming);
        }
    }
}
