package com.utad.david.planfit.Adapter.Users;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;

import java.util.List;

public class UsersAdapters extends RecyclerView.Adapter<UsersAdapters.UsersViewHolder> {

    private List<User> userList;
    private Callback listener;

    public interface Callback {
        void onItemClick(User item);
    }

    public UsersAdapters(List<User> users, Callback listener) {
        this.userList = users;
        this.listener = listener;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UsersViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, int position) {
        final User current = userList.get(position);
        holder.setData(current);
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onItemClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView nameUser;
        //private ImageView photoSlimming;

        public UsersViewHolder(View v) {
            super(v);
            nameUser = v.findViewById(R.id.name_user);
            //photoSlimming = v.findViewById(R.id.imageSlimming);
        }

        public void setData(User user){
            nameUser.setText(user.getNickName());
            //Utils.loadImage(defaultNutrition.getPhoto(),photoSlimming,Utils.PLACEHOLDER_GALLERY);
        }
    }
}