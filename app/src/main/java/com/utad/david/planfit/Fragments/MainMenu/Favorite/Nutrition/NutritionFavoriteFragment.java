package com.utad.david.planfit.Fragments.MainMenu.Favorite.Nutrition;

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
import com.utad.david.planfit.Adapter.Favorite.Nutrition.NutritionFavoriteAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.DialogFragment.Favorite.Nutrition.NutritionFavoriteDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.List;

public class NutritionFavoriteFragment extends BaseFragment
        implements NutritionFavoriteDetailsDialogFragment.Callback,
        NutritionFavoriteView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Runnable toolbarRunnable;
    private RecyclerView mRecyclerView;
    private NutritionFavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private NutritionFavoriteDetailsDialogFragment nutritionFavoriteDetailsDialogFragment;
    private NutritionFavoritePresenter nutritionFavoritePresenter;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public NutritionFavoriteFragment newInstance() {
        return new NutritionFavoriteFragment();
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

        this.nutritionFavoritePresenter = new NutritionFavoritePresenter(this);

        if (this.nutritionFavoritePresenter.checkConnectionInternet(getContext())) {
            showLoading();
            this.nutritionFavoritePresenter.getFavoriteNutrition();
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nutrition_favorite, container, false);

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

    /******************************** NutritionFavoriteDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        this.nutritionFavoriteDetailsDialogFragment.dismiss();
    }

    @Override
    public void setDataChange(DefaultNutrition item) {
        if (this.mAdapter != null) {
            showLoading();
            this.mAdapter.dataChangedDeleteSport(item);
            if (this.mAdapter.getDefaultNutritions().size() == 0) {
                this.nutritionFavoritePresenter.emptyFavoriteNutritionList();
            }
            Toast.makeText(getContext(),getString(R.string.favorito_borrado_correctamente),Toast.LENGTH_LONG).show();
            hideLoading();
        }
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateEmptyUI() {
        hideLoading();
        this.linearLayout.setVisibility(View.VISIBLE);
        this.mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void getFavoriteList(List<DefaultNutrition> data) {
        hideLoading();
        this.linearLayout.setVisibility(View.GONE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
        Collections.sort(data);
        this.configurateRecycleView(data);
    }

    private void configurateRecycleView(List<DefaultNutrition> list) {
        this.mAdapter = new NutritionFavoriteAdapter(list, item -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);
            this.nutritionFavoriteDetailsDialogFragment = NutritionFavoriteDetailsDialogFragment.newInstance(item);
            this.nutritionFavoriteDetailsDialogFragment.setListener(this);
            this.nutritionFavoriteDetailsDialogFragment.show(transaction, Constants.TagDialogFragment.TAG);
        });
        this.mRecyclerView.setAdapter(this.mAdapter);
    }
}
