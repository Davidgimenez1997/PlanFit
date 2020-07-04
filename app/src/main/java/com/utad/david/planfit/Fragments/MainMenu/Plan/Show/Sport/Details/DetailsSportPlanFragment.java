package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport.Details;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Show.Sport.Details.ShowDetailsSportPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

public class DetailsSportPlanFragment extends BaseFragment
        implements DetailsSportPlanView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static final String ARG_PARAM1 = "param1";

    private ArrayList<PlanSport> planSports;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private ShowDetailsSportPlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DetailsSportPlanPresenter detailsSportPlanPresenter;

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static DetailsSportPlanFragment newInstance(ArrayList<PlanSport> defaultSports) {
        DetailsSportPlanFragment fragment = new DetailsSportPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM1,defaultSports);
        fragment.setArguments(bundle);
        return fragment;
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

        this.detailsSportPlanPresenter = new DetailsSportPlanPresenter(this);
        if (this.detailsSportPlanPresenter.checkInternetInDevice(getContext())){
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
        }

        if (getArguments() != null) {
            this.planSports = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details_sport_plan, container, false);

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getContext(), 1);
        this.mRecyclerView.setLayoutManager(mLayoutManager);

        if (this.detailsSportPlanPresenter.checkInternetInDevice(getContext())) {
            this.configureRecycleView();
        }

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configureRecycleView() {
        this.mAdapter = new ShowDetailsSportPlanAdapter(planSports, item -> {
            if (this.detailsSportPlanPresenter.checkInternetInDevice(getContext())) {
                if (item.getIsOk().equals(Constants.ModePlan.YES)) {
                    this.createDialog(getString(R.string.te_has_equivocado), item);
                } else if (item.getIsOk().equals(Constants.ModePlan.NO)) {
                    this.createDialog(getString(R.string.ya_lo_has_realizado), item);
                }
            }
        });
        this.mRecyclerView.setAdapter(mAdapter);
    }

    private void createDialog(String title, PlanSport item) {
        CharSequence[] items = {getString(R.string.si),getString(R.string.cancelar)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setItems(items, (dialog, itemDialog) -> {
            switch (itemDialog) {
                case 0:
                    showLoading();
                    if (item.getIsOk().equals(Constants.ModePlan.YES)) {
                        item.setIsOk(Constants.ModePlan.NO);
                    } else if (item.getIsOk().equals(Constants.ModePlan.NO)) {
                        item.setIsOk(Constants.ModePlan.YES);
                    }
                    this.detailsSportPlanPresenter.updatePlan(item);
                    this.mAdapter.notifyDataSetChanged();
                    hideLoading();
                    break;
                case 1:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void getSportPlan() {
        hideLoading();
    }

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
