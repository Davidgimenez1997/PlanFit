package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import com.utad.david.planfit.Adapter.Favorite.SportFavoriteAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Favorite.NutritionFavoriteDetailsDialogFragment;
import com.utad.david.planfit.DialogFragment.Favorite.SportFavoriteDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

import java.util.List;

public class SportFavorite extends Fragment implements FirebaseAdmin.FirebaseAdminFavoriteSportAndNutrition, SportFavoriteDetailsDialogFragment.CallbackFavoriteSport{

    public SportFavorite() {
        // Required empty public constructor
    }

    private SportFavorite fragment;

    public SportFavorite newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminFavoriteSportAndNutrition(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllSportFavorite();
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private SportFavoriteDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_favorite, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void downloandCollectionSportFavorite(boolean end) {
        if(end==true){
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            List<DefaultSport> allSportFavorite = SessionUser.getInstance().firebaseAdmin.allSportFavorite;
            mAdapter = new SportFavoriteAdapter(allSportFavorite, new SportFavoriteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DefaultSport item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = SportFavoriteDetailsDialogFragment.newInstance(item);
                    newFragment.setListener(fragment);
                    newFragment.show(transaction, "dialog");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void emptyCollectionSportFavorite(boolean end) {
        if(end==true){
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickClose() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void inserSportFavoriteFirebase(boolean end) {

    }

    @Override
    public void inserNutritionFavoriteFirebase(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionFavorite(boolean end) {

    }

    @Override
    public void emptyCollectionNutritionFavorite(boolean end) {

    }

    @Override
    public void deleteFavoriteSport(boolean end) {

    }

    @Override
    public void deleteFavoriteNutrition(boolean end) {

    }
}
