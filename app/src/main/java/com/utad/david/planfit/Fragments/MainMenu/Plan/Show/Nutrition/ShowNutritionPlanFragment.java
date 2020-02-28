package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition;

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
import com.utad.david.planfit.Adapter.Plan.Show.Nutrition.ShowNutritionPlanAdapter;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.Plan.Nutrition.GetNutritionPlan;
import com.utad.david.planfit.Data.Plan.Nutrition.NutritionPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowNutritionPlanFragment extends BaseFragment
        implements GetNutritionPlan {

    /******************************** VARIABLES *************************************+/
     *
     */

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private FragmentActivity myContext;
    private ArrayList<ArrayList<PlanNutrition>> listToListPlan;
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
        View view =  inflater.inflate(R.layout.fragment_show_nutrition_plan, container, false);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            NutritionPlanRepository.getInstance().setGetNutritionPlan(this);
            NutritionPlanRepository.getInstance().getNutrtionPlan();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
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
    public void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions) {
        if (status) {
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ArrayList<PlanNutrition> arrNutrition = new ArrayList<>();
            for (PlanNutrition item : planNutritions) {
                arrNutrition.add(item);
            }
            Collections.sort(arrNutrition);

            listToListPlan = new ArrayList<>();
            double time = arrNutrition.get(0).getType();
            ArrayList<PlanNutrition> aux = new ArrayList<>();
            int count =0;
            listToListPlan.add(aux);

            for(int i=0;i<arrNutrition.size();i++){
                if(arrNutrition.get(i).getType() != time){
                    count=count+1;
                    ArrayList<PlanNutrition> aux2 = new ArrayList<>();
                    listToListPlan.add(aux2);
                }else{
                }
                listToListPlan.get(count).add(arrNutrition.get(i));

                time= arrNutrition.get(i).getType();

            }

            mAdapter = new ShowNutritionPlanAdapter(listToListPlan, planNutritionDetails -> {
                DetailsNutritionPlanFragment detailsNutritionPlanFragment = DetailsNutritionPlanFragment.newInstance(planNutritionDetails);
                detailsNutritionPlanFragment.setToolbarRunnable(() -> getActivity().setTitle(getResources().getString(R.string.plan_nutricion)));
                FragmentManager fragManager = myContext.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, detailsNutritionPlanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            mRecyclerView.setAdapter(mAdapter);
            if(arrNutrition.size()==0){
                updateNutritionPlan(false, null);
            }else{
                updateNutritionPlan(true, planNutritions);
            }
        }
    }

    @Override
    public void emptyNutritionPlan(boolean status) {
        if(status){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateNutritionPlan(boolean status, List<PlanNutrition> updateList) {
        if(status){
            hideLoading();
            boolean endOk = true;

            final ArrayList<PlanNutrition> arrNutrition = new ArrayList<>();
            for(PlanNutrition item:updateList ){
                arrNutrition.add(item);
            }
            Collections.sort(arrNutrition);

            for(int i=0;i<arrNutrition.size();i++){
                if(arrNutrition.get(i).getIsOk().equals(Constants.ModePlan.NO)){
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
                            for (PlanNutrition planNutrition:arrNutrition){
                                planNutrition.setIsOk(Constants.ModePlan.NO);
                                NutritionPlanRepository.getInstance().updatePlanNutrtion(planNutrition);
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
    public void addNutritionPlan(boolean status) {}
    @Override
    public void deleteNutritionPlan(boolean status) {}
}
