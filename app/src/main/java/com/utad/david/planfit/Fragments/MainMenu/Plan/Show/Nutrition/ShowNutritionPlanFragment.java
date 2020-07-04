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
import com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition.Details.DetailsNutritionPlanFragment;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

public class ShowNutritionPlanFragment extends BaseFragment
        implements ShowNutritionPlanView {

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
    private ShowNutritionPlanPresenter showNutritionPlanPresenter;

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
        this.myContext = (FragmentActivity) activity;
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

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.showNutritionPlanPresenter = new ShowNutritionPlanPresenter(this);

        if (this.showNutritionPlanPresenter.checkInternetDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        this.linearLayout = view.findViewById(R.id.linear_empty_favorites);
        this.mRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getContext(), 1);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);

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
    public void getNutritionPlan(List<PlanNutrition> planNutrition) {
        hideLoading();
        this.linearLayout.setVisibility(View.GONE);
        this.mRecyclerView.setVisibility(View.VISIBLE);

        ArrayList<PlanNutrition> arrNutrition = new ArrayList<>();
        for (PlanNutrition item : planNutrition) {
            arrNutrition.add(item);
        }
        this.showNutritionPlanPresenter.shortArray(arrNutrition);

        this.listToListPlan = new ArrayList<>();
        double time = arrNutrition.get(0).getType();
        ArrayList<PlanNutrition> aux = new ArrayList<>();
        int count = 0;
        this.listToListPlan.add(aux);

        for(int i = 0; i < arrNutrition.size(); i++) {
            if (arrNutrition.get(i).getType() != time) {
                count = count + 1;
                ArrayList<PlanNutrition> aux2 = new ArrayList<>();
                this.listToListPlan.add(aux2);
            }
            this.listToListPlan.get(count).add(arrNutrition.get(i));
            time = arrNutrition.get(i).getType();
        }
        this.configurateRecycleView(this.listToListPlan);
        this.showNutritionPlanPresenter.checkArraySize(arrNutrition, planNutrition);
    }

    private void configurateRecycleView(ArrayList<ArrayList<PlanNutrition>> list) {
        this.mAdapter = new ShowNutritionPlanAdapter(list, planNutritionDetails -> {
            DetailsNutritionPlanFragment detailsNutritionPlanFragment = DetailsNutritionPlanFragment.newInstance(planNutritionDetails);
            detailsNutritionPlanFragment.setToolbarRunnable(() -> getActivity().setTitle(getResources().getString(R.string.plan_nutricion)));
            FragmentManager fragManager = this.myContext.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, detailsNutritionPlanFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void updateNutritionPlan(List<PlanNutrition> updateList) {
        hideLoading();
        boolean endOk = true;
        final ArrayList<PlanNutrition> arrNutrition = new ArrayList<>();
        for (PlanNutrition item : updateList ){
            arrNutrition.add(item);
        }
        this.showNutritionPlanPresenter.shortArray(arrNutrition);
        for (int i = 0; i < arrNutrition.size(); i++) {
            if (arrNutrition.get(i).getIsOk().equals(Constants.ModePlan.NO)) {
                endOk = false;
            }
        }
        this.createDialogs(endOk, arrNutrition);
    }

    private void createDialogs(boolean endOk, ArrayList<PlanNutrition> arrNutrition) {
        if (endOk) {
            final CharSequence[] items = {getString(R.string.restablecer),getString(R.string.cancelar)};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.mensaje_complete_all_plan));
            builder.setItems(items, (dialog, itemDialog) -> {
                switch (itemDialog) {
                    case 0:
                        showLoading();
                        for (PlanNutrition planNutrition : arrNutrition){
                            planNutrition.setIsOk(Constants.ModePlan.NO);
                            this.showNutritionPlanPresenter.updatePlan(planNutrition);
                        }
                        this.mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        dialog.dismiss();
                        break;
                }
            });

            builder.show();
        }
    }

    @Override
    public void emptyNutritionPlan() {
        hideLoading();
        this.linearLayout.setVisibility(View.VISIBLE);
        this.mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
