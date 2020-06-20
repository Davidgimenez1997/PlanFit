package com.utad.david.planfit.Fragments.MainMenu.Sport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.utad.david.planfit.Adapter.Sport.SportSlimmingAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Sport.GetSport;
import com.utad.david.planfit.Data.Sport.SportRepository;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SportSlimmingFragment extends BaseFragment
        implements GetSport,
        SportDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private SportSlimmingFragment fragment;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SportDetailsDialogFragment newFragment;
    private boolean configureRecycleView;
    private List<SportSlimming> sportsCache;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public SportSlimmingFragment newInstanceSlimming() {
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
            this.sportsCache = SharedPreferencesManager.loadSportSlimming(getContext(),SharedPreferencesManager.SPORT_SLIMING_TAG);
            if (this.sportsCache == null) {
                SportRepository.getInstance().setGetSport(this);
                SportRepository.getInstance().getSlimmingSport();
            } else {
                this.configureRecycleView = true;
            }
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_sport_recycleview, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (configureRecycleView) {
            this.configureRecycleView(this.sportsCache);
            hideLoading();
        }

       return view;
    }

    private void configureRecycleView (List<SportSlimming> data) {
        mAdapter = new SportSlimmingAdapter(data, item -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);
            newFragment = SportDetailsDialogFragment.newInstanceSlimming(item,0,getContext());
            newFragment.setListener(fragment);
            newFragment.show(transaction, Constants.TagDialogFragment.TAG);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /******************************** CALLBACK DE SportDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void getSlimmingSports(boolean status, List<SportSlimming> data) {
        if(status){
            Collections.sort(data);
            SharedPreferencesManager.saveSportSlimming(data, getContext(), SharedPreferencesManager.SPORT_SLIMING_TAG);
            this.configureRecycleView(data);
            hideLoading();
        }
    }

    @Override
    public void getToningSports(boolean status, List<SportToning> data) {}
    @Override
    public void getGainVolumeSports(boolean status, List<SportGainVolume> data) {}
}
