package com.utad.david.planfit.Fragments.MainMenu.Nutrition;

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
import com.utad.david.planfit.Adapter.Nutrition.NutritionGainVolumeAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Nutrition.GetNutrition;
import com.utad.david.planfit.Data.Nutrition.NutritionRepository;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.List;

public class NutritionGainVolumeFragment extends BaseFragment
        implements GetNutrition,
        NutritionDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private NutritionGainVolumeFragment fragment;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NutritionDetailsDialogFragment newFragment;
    private boolean configureRecycleView;
    private List<NutritionGainVolume> nutritionCache;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public NutritionGainVolumeFragment newInstanceSlimming() {
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
            this.nutritionCache = SharedPreferencesManager.loadNutritionGainVolume(getContext(), SharedPreferencesManager.NUTRITION_GAIN_VOLUMEN_TAG);
            if (this.nutritionCache == null) {
                NutritionRepository.getInstance().setGetNutrition(this);
                NutritionRepository.getInstance().getGainVolumeNutrition();
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
        View view = inflater.inflate(R.layout.fragment_nutrition_recycleview, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (configureRecycleView) {
            this.configureRecycleView(this.nutritionCache);
            hideLoading();
        }

        return view;
    }

    private void configureRecycleView (List<NutritionGainVolume> data) {
        mAdapter = new NutritionGainVolumeAdapter(data, item -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);
            newFragment = NutritionDetailsDialogFragment.newInstanceGainVolume(item,2,getContext());
            newFragment.setCallbackNutrition(fragment);
            newFragment.show(transaction, Constants.TagDialogFragment.TAG);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /******************************** CALLBACK DE NutritionDetailsDialogFragment.Callback *************************************+/
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
    public void getGainVolumeNutritions(boolean status, List<NutritionGainVolume> data) {
        if(status){
            Collections.sort(data);
            SharedPreferencesManager.saveNutritionGainVolume(data, getContext(), SharedPreferencesManager.NUTRITION_GAIN_VOLUMEN_TAG);
            this.configureRecycleView(data);
            hideLoading();
        }
    }

    @Override
    public void getSlimmingNutritions(boolean status, List<NutritionSlimming> data) {}

    @Override
    public void getToningNutritions(boolean status, List<NutritionToning> data) {}
}
