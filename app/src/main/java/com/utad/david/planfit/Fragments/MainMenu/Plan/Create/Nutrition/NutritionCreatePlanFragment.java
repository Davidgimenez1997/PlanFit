package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Nutrition;

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
import com.utad.david.planfit.Adapter.Plan.Create.Nutrition.CreateNutritionPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.DialogFragment.Plan.Nutrition.CreateNutritionPlanDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;

import io.fabric.sdk.android.Fabric;

import java.util.List;

public class NutritionCreatePlanFragment extends BaseFragment
        implements NutritionCreatePlanView,
        CreateNutritionPlanDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private NutritionCreatePlanFragment fragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private CreateNutritionPlanDetailsDialogFragment newFragment;
    private Runnable toolbarRunnable;
    private NutritionCreatePlanPresenter nutritionCreatePlanPresenter;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public NutritionCreatePlanFragment newInstanceSlimming() {
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
        this.nutritionCreatePlanPresenter = new NutritionCreatePlanPresenter(this);
        if (this.nutritionCreatePlanPresenter.checkInternetInDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view =  inflater.inflate(R.layout.fragment_nutrition_create_plan, container, false);


        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        this.linearLayout = view.findViewById(R.id.linear_empty_favorites);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);

        return view;
    }

    /******************************** CreateNutritionPlanDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        this.newFragment.dismiss();
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(), getString(R.string.info_network_device), Toast.LENGTH_LONG).show();
    }

    @Override
    public void getNutritionFavoriteList(List<DefaultNutrition> list) {
        hideLoading();
        this.linearLayout.setVisibility(View.GONE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
        this.mAdapter = new CreateNutritionPlanAdapter(list, item -> {
            this.nutritionCreatePlanPresenter.onClickItem(item);
        });
        this.mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void clickItem(DefaultNutrition item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
        this.newFragment = CreateNutritionPlanDetailsDialogFragment.newInstance(item);
        this.newFragment.setListener(this.fragment);
        this.newFragment.show(transaction, Constants.TagDialogFragment.TAG);
    }

    @Override
    public void getEmptyNutritionFavoriteList() {
        hideLoading();
        this.linearLayout.setVisibility(View.VISIBLE);
        this.mRecyclerView.setVisibility(View.GONE);
    }

}
