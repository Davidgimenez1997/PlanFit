package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Show.Sport.ShowDetailsSportPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Plan.Sport.GetSportPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class DetailsSportPlanFragment extends BaseFragment
        implements GetSportPlan {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static final String ARG_PARAM1 = "param1";

    private ArrayList<PlanSport> planSports;
    private Runnable toolbarRunnable;

    private RecyclerView mRecyclerView;
    private ShowDetailsSportPlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SportPlanRepository.getInstance().setGetSportPlan(this);
            SportPlanRepository.getInstance().getSportPlan();
            Fabric.with(getContext(), new Crashlytics());
        }

        if (getArguments() != null) {
            planSports = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details_sport_plan, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            configureRecycleView();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }


        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configureRecycleView() {
        mAdapter = new ShowDetailsSportPlanAdapter(planSports, item -> {

            if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
                if(item.getIsOk().equals(Constants.ModePlan.YES)){

                    final CharSequence[] items = {getString(R.string.si),getString(R.string.cancelar)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.te_has_equivocado));
                    builder.setItems(items, (dialog, itemDialog) -> {
                        switch (itemDialog) {
                            case 0:
                                showLoading();
                                item.setIsOk(Constants.ModePlan.NO);
                                SportPlanRepository.getInstance().updatePlanSport(item);
                                mAdapter.notifyDataSetChanged();
                                hideLoading();
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    });
                    builder.show();

                }else if(item.getIsOk().equals(Constants.ModePlan.NO)){

                    final CharSequence[] items = {getString(R.string.si),getString(R.string.cancelar)};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.ya_lo_has_realizado));
                    builder.setItems(items, (dialog, itemDialog) -> {
                        switch (itemDialog) {
                            case 0:
                                showLoading();
                                item.setIsOk(Constants.ModePlan.YES);
                                SportPlanRepository.getInstance().updatePlanSport(item);
                                mAdapter.notifyDataSetChanged();
                                hideLoading();
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    });
                    builder.show();
                }
            }else{
                Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }

        });

        mRecyclerView.setAdapter(mAdapter);
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void getSportPlan(boolean status, List<PlanSport> planSports) {
        hideLoading();
    }

    @Override
    public void updateSportPlan(boolean status, List<PlanSport> updateList) {}
    @Override
    public void emptySportPlan(boolean status) {}
    @Override
    public void addSportPlan(boolean status) {}
    @Override
    public void deleteSportPlan(boolean status) {}
}
