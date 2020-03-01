package com.utad.david.planfit.Fragments.MainMenu.Sport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Sport.SportToningAdapter;
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
import java.util.Collections;
import java.util.List;

public class SportToningFragment extends BaseFragment
        implements GetSport,
        SportDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private SportToningFragment fragment;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SportDetailsDialogFragment newFragment;
    private boolean configureRecycleView;
    private List<SportToning> sportsCache;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public SportToningFragment newInstanceSlimming() {
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
            this.sportsCache = SharedPreferencesManager.loadSportToning(getContext(), SharedPreferencesManager.SPORT_TONING_TAG);
            if (this.sportsCache == null) {
                SportRepository.getInstance().setGetSport(this);
                SportRepository.getInstance().getToningSport();
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

    private void configureRecycleView (List<SportToning> data) {
        mAdapter = new SportToningAdapter(data, item -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);
            newFragment = SportDetailsDialogFragment.newInstanceToning(item,1,getContext());
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
    public void getToningSports(boolean status, List<SportToning> data) {
        if(status){
            Collections.sort(data);
            SharedPreferencesManager.saveSportToning(data, getContext(), SharedPreferencesManager.SPORT_TONING_TAG);
            this.configureRecycleView(data);
            hideLoading();
        }
    }


    @Override
    public void getSlimmingSports(boolean status, List<SportSlimming> data) {}
    @Override
    public void getGainVolumeSports(boolean status, List<SportGainVolume> data) {}
}
