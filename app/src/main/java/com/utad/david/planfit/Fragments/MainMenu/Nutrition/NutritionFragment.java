package com.utad.david.planfit.Fragments.MainMenu.Nutrition;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.utad.david.planfit.Adapter.Nutrition.NutritionAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import java.util.List;

public class NutritionFragment extends BaseFragment implements NutritionDetailsDialogFragment.Callback, NutritionView {

    private static String MODE = Constants.SportNutritionOption.EXTRA_MODE;
    private NutritionFragment fragment;
    private Runnable toolbarRunnable;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NutritionDetailsDialogFragment fragmentDetailsNutritiont;
    private NutritionPresenter nutritionPresenter;
    private int mode;

    public NutritionFragment newInstance(int mode) {
        this.fragment = this;
        Bundle args = new Bundle();
        args.putInt(MODE, mode);
        this.fragment.setArguments(args);
        return this.fragment;
    }

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mode = getArguments().getInt(MODE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_recycleview, container, false);

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.mRecyclerView.setLayoutManager(mLayoutManager);

        showLoading();
        switch (this.mode) {
            case Constants.SportNutritionOption.SLIMMING:
                this.nutritionPresenter = new NutritionPresenter(this, this.mode, getContext());
                break;
            case Constants.SportNutritionOption.TONING:
                this.nutritionPresenter = new NutritionPresenter(this, this.mode, getContext());
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                this.nutritionPresenter = new NutritionPresenter(this, this.mode, getContext());
                break;
        }
        this.nutritionPresenter.loadData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.nutritionPresenter = null;
    }

    private void configureRecycleView (List<DefaultNutrition> data) {
        this.mAdapter = new NutritionAdapter(data, item -> {
            switch (this.mode) {
                case Constants.SportNutritionOption.SLIMMING:
                    this.nutritionPresenter.onListItemClick(item, this.mode);
                    break;
                case Constants.SportNutritionOption.TONING:
                    this.nutritionPresenter.onListItemClick(item, this.mode);
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    this.nutritionPresenter.onListItemClick(item, this.mode);
                    break;
            }
        });
        this.mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClickClose() {
        this.fragmentDetailsNutritiont.dismiss();
    }

    @Override
    public void configurationRecycleView(List<DefaultNutrition> data) {
        hideLoading();
        this.configureRecycleView(data);
    }

    @Override
    public void onClickItemRecycleView(DefaultNutrition defaultNutrition, int mode) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
        switch (mode) {
            case Constants.SportNutritionOption.SLIMMING:
                this.fragmentDetailsNutritiont = NutritionDetailsDialogFragment.newInstance(defaultNutrition, this.mode);
                break;
            case Constants.SportNutritionOption.TONING:
                this.fragmentDetailsNutritiont = NutritionDetailsDialogFragment.newInstance(defaultNutrition, this.mode);
                break;
            case Constants.SportNutritionOption.GAIN_VOLUMEN:
                this.fragmentDetailsNutritiont = NutritionDetailsDialogFragment.newInstance(defaultNutrition, this.mode);
                break;
        }
        this.fragmentDetailsNutritiont.setCallbackNutrition(this.fragment);
        this.fragmentDetailsNutritiont.show(transaction, Constants.TagDialogFragment.TAG);
    }

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
