package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Favorite.Sport.SportFavoriteAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Favorite.GetSportFavorite;
import com.utad.david.planfit.Data.Favorite.SportFavoriteRepository;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Favorite.SportFavoriteDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.List;

public class SportFavoriteFragment extends BaseFragment
        implements GetSportFavorite,
        SportFavoriteDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private SportFavoriteFragment fragment;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private SportFavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private SportFavoriteDetailsDialogFragment newFragment;


    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public SportFavoriteFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SportFavoriteRepository.getInstance().setGetSportFavorite(this);
            SportFavoriteRepository.getInstance().getAllSportFavorite();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sport_favorite, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    /******************************** SportFavoriteDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void setDataChange() {
        if(mAdapter!=null){
            showLoading();
            mAdapter.dataChangedDeleteSport(SessionUser.getInstance().firebaseAdmin.allSportFavorite);
            if(SessionUser.getInstance().firebaseAdmin.allSportFavorite.size()==0){
                linearLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            Toast.makeText(getContext(),getString(R.string.favorito_borrado),Toast.LENGTH_LONG).show();
            hideLoading();
        }
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void getSportAllFavorite(boolean status, List<DefaultSport> defaultSports) {
        if(status){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Collections.sort(defaultSports);
            mAdapter = new SportFavoriteAdapter(defaultSports, item -> {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                newFragment = SportFavoriteDetailsDialogFragment.newInstance(item);
                newFragment.setListener(fragment);
                newFragment.show(transaction, Constants.TagDialogFragment.TAG);
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void emptySportFavorite(boolean status) {
        if(status){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void addSportFavorite(boolean status) {}
    @Override
    public void deleteSportFavorite(boolean status) {}
    @Override
    public void getSportSlimmingFavorite(boolean status, List<SportSlimming> sportSlimmings) {}
    @Override
    public void getSportToningFavorite(boolean status, List<SportToning> sportTonings) {}
    @Override
    public void getSportGainVolumeFavorite(boolean status, List<SportGainVolume> sportGainVolumes) {}
}
