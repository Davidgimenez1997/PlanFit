package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Show.Sport;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Show.Sport.ShowSportPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.Plan.Sport.GetSportPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowSportPlanFragment extends BaseFragment
        implements GetSportPlan {

    /******************************** VARIABLES *************************************+/
     *
     */

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private FragmentActivity myContext;
    private ArrayList<ArrayList<PlanSport>> listToListPlan;
    private Runnable toolbarRunnable;

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }


    /******************************** SET Context *************************************+/
     *
     */

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_sport_plan, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SportPlanRepository.getInstance().setGetSportPlan(this);
            SportPlanRepository.getInstance().getSportPlan();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void getSportPlan(boolean status, List<PlanSport> planSports) {
        if(status){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            final ArrayList<PlanSport> arrSport = new ArrayList<>();
            for(PlanSport item:planSports){
                arrSport.add(item);
            }
            Collections.sort(arrSport);

            listToListPlan = new ArrayList<>();
            double time = arrSport.get(0).getTimeStart();
            ArrayList<PlanSport> aux = new ArrayList<>();
            int count =0;
            listToListPlan.add(aux);

            for(int i=0;i<arrSport.size();i++){
                if(arrSport.get(i).getTimeStart() != time){
                    count=count+1;
                    ArrayList<PlanSport> aux2 = new ArrayList<>();
                    listToListPlan.add(aux2);
                }
                listToListPlan.get(count).add(arrSport.get(i));
                time= arrSport.get(i).getTimeStart();
            }

            mAdapter = new ShowSportPlanAdapter(listToListPlan, planSportsDetails -> {
                DetailsSportPlanFragment detailsSportPlanFragment = DetailsSportPlanFragment.newInstance(planSportsDetails);
                detailsSportPlanFragment.setToolbarRunnable(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().setTitle(getResources().getString(R.string.plan_deporte));
                    }
                });
                FragmentManager fragManager = myContext.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, detailsSportPlanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            mRecyclerView.setAdapter(mAdapter);

            if(arrSport.size()==0){
                updateSportPlan(false, null);
            }else{
                updateSportPlan(true, planSports);
            }
        }
    }

    @Override
    public void updateSportPlan(boolean status, List<PlanSport> updateList) {
        if(status){
            hideLoading();
            boolean endOk = true;

            final ArrayList<PlanSport> arrSport = new ArrayList<>();
            for(PlanSport item:updateList){
                arrSport.add(item);
            }

            Collections.sort(arrSport);

            for(int i=0;i<arrSport.size();i++){
                if(arrSport.get(i).getIsOk().equals(Constants.ModePlan.NO)){
                    endOk = false;
                }
            }

            if(endOk){
                final CharSequence[] items = {getString(R.string.restablecer),getString(R.string.cancelar)};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.mensaje_complete_all_plan));
                builder.setItems(items, (dialog, itemDialog) -> {
                    switch (itemDialog) {
                        case 0:
                            showLoading();
                            for (PlanSport planSport:arrSport){
                                planSport.setIsOk(Constants.ModePlan.NO);
                                SportPlanRepository.getInstance().updatePlanSport(planSport);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 1:
                            dialog.dismiss();
                            break;
                    }
                });

                builder.show();
            }
        }
    }

    @Override
    public void emptySportPlan(boolean status) {
        if(status){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void addSportPlan(boolean status) {}

    @Override
    public void deleteSportPlan(boolean status) {}
}
