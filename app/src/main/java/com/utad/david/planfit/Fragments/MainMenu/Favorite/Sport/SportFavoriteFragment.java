package com.utad.david.planfit.Fragments.MainMenu.Favorite.Sport;

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
import com.utad.david.planfit.DialogFragment.Favorite.Sport.SportDetailsFavoriteDialogDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.List;

public class SportFavoriteFragment extends BaseFragment
        implements SportDetailsFavoriteDialogDialogFragment.Callback,
        SportFavoriteView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Runnable toolbarRunnable;
    private RecyclerView mRecyclerView;
    private SportFavoriteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private SportDetailsFavoriteDialogDialogFragment sportDetailsFavoriteDialogFragment;
    private SportFavoritePresenter sportFavoritePresenter;


    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public SportFavoriteFragment newInstance() {
        return new SportFavoriteFragment();
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

        this.sportFavoritePresenter = new SportFavoritePresenter(this);

        if (this.sportFavoritePresenter.checkConnectionInternet(getContext())) {
            showLoading();
            this.sportFavoritePresenter.getFavoriteSports();
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sport_favorite, container, false);

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        this.linearLayout = view.findViewById(R.id.linear_empty_favorites);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);

        return view;
    }

    /******************************** SportFavoriteDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        this.sportDetailsFavoriteDialogFragment.dismiss();
    }

    @Override
    public void setDataChange(DefaultSport item) {
        if (this.mAdapter != null){
            showLoading();
            this.mAdapter.dataChangedDeleteSport(item);
            if (this.mAdapter.getDefaultSports().size() == 0) {
                this.sportFavoritePresenter.emptyFavoriteSportList();
            }
            Toast.makeText(getContext(),getString(R.string.favorito_borrado),Toast.LENGTH_LONG).show();
            hideLoading();
        }
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void updateEmptyUI() {
        hideLoading();
        this.linearLayout.setVisibility(View.VISIBLE);
        this.mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void getFavoriteList(List<DefaultSport> data) {
        hideLoading();
        this.linearLayout.setVisibility(View.GONE);
        this.mRecyclerView.setVisibility(View.VISIBLE);
        Collections.sort(data);
        this.configurateRecycleView(data);
    }

    private void configurateRecycleView(List<DefaultSport> list) {
        this.mAdapter = new SportFavoriteAdapter(list, item -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
            if (prev != null) {
                transaction.remove(prev);
            }
            transaction.addToBackStack(null);
            this.sportDetailsFavoriteDialogFragment = SportDetailsFavoriteDialogDialogFragment.newInstance(item);
            this.sportDetailsFavoriteDialogFragment.setListener(this);
            this.sportDetailsFavoriteDialogFragment.show(transaction, Constants.TagDialogFragment.TAG);
        });
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }


}
