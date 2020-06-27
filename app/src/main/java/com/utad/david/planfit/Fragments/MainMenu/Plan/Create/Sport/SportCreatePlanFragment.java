package com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Sport;

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
import com.utad.david.planfit.Adapter.Plan.Create.Sport.CreateSportPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.DialogFragment.Plan.Sport.CreateSportPlanDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;
import java.util.List;

public class SportCreatePlanFragment extends BaseFragment
        implements SportCreatePlanView,
        CreateSportPlanDetailsDialogFragment.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private SportCreatePlanFragment fragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private CreateSportPlanDetailsDialogFragment newFragment;
    private Runnable toolbarRunnable;
    private SportCreatePlanPresenter sportCreatePlanPresenter;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public SportCreatePlanFragment newInstanceSlimming() {
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
        this.sportCreatePlanPresenter = new SportCreatePlanPresenter(this);
        if (this.sportCreatePlanPresenter.checkInternetInDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sport_create_plan, container, false);

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

    /******************************** CreateSportPlanDetailsDialogFragment.Callback *************************************+/
     *
     */

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(), getString(R.string.info_network_device), Toast.LENGTH_LONG).show();
    }

    @Override
    public void getSportFavoriteList(List<DefaultSport> list) {
        hideLoading();
        linearLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter = new CreateSportPlanAdapter(list, item -> {
            this.sportCreatePlanPresenter.onClickItem(item);
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void clickItem(DefaultSport item) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
        newFragment = CreateSportPlanDetailsDialogFragment.newInstance(item);
        newFragment.setListener(fragment);
        newFragment.show(transaction, Constants.TagDialogFragment.TAG);
    }

    @Override
    public void getEmptySportFavoriteList() {
        hideLoading();
        linearLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
