package com.utad.david.planfit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Model.Sport.Slimming;
import com.utad.david.planfit.Model.Sport.Toning;
import com.utad.david.planfit.R;

import java.util.List;

public class ToningAdapter extends RecyclerView.Adapter<ToningAdapter.ToningViewHolder>  {

    private List<Toning> toningList;
    private ToningAdapter.OnItemClickListener listener;

    //Obtenemos información del item
    public interface OnItemClickListener {
        void onItemClick(Toning item);
    }

    public ToningAdapter(List<Toning> toningList,ToningAdapter.OnItemClickListener listener) {
        this.toningList = toningList;
        this.listener = listener;
    }

    @Override
    public ToningAdapter.ToningViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview, parent, false);
        return new ToningViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ToningViewHolder holder, int position) {
        final Toning current = toningList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toningList.size();
    }

    public static class ToningViewHolder extends RecyclerView.ViewHolder {
        public TextView nameSlimming;
        private ImageView photoSlimming;

        public ToningViewHolder(View v) {
            super(v);
            nameSlimming = v.findViewById(R.id.nameSlimming);
            photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(Toning toning){
            nameSlimming.setText(toning.getName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_gallery);
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(toning.getPhoto()).into(photoSlimming);
        }
    }

}
