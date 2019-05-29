package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Show.Nutrition;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Show.Nutrition.ShowDetailsNutritionPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

public class DetailsNutritionPlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminCreateShowPlanNutrition {

    private static final String ARG_PARAM1 = "param1";
    private ArrayList<PlanNutrition> planNutritions;
    private Runnable toolbarRunnable;


    public DetailsNutritionPlanFragment() {
        // Required empty public constructor
    }

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    public static DetailsNutritionPlanFragment newInstance(ArrayList<PlanNutrition> defaultSports) {
        DetailsNutritionPlanFragment fragment = new DetailsNutritionPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM1,defaultSports);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateShowPlanNutrition(this);
            SessionUser.getInstance().firebaseAdmin.downloadAllNutrtionPlanFavorite();
            Fabric.with(getContext(), new Crashlytics());
        }

        if (getArguments() != null) {
            planNutritions = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    private RecyclerView mRecyclerView;
    private ShowDetailsNutritionPlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_nutrition_plan, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(!UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }else{
            mAdapter = new ShowDetailsNutritionPlanAdapter(planNutritions, item -> {

                if(!UtilsNetwork.checkConnectionInternetDevice(getContext())){
                    Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }else{
                    if(item.getIsOk().equals(Constants.ModePlan.YES)){

                        final CharSequence[] items = {getString(R.string.si),getString(R.string.cancelar)};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.te_has_equivocado));
                        builder.setItems(items, (dialog, itemDialog) -> {
                            switch (itemDialog) {
                                case 0:
                                    showLoading();
                                    item.setIsOk(Constants.ModePlan.NO);
                                    SessionUser.getInstance().firebaseAdmin.updatePlanNutrtionFirebase(item);
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
                                    SessionUser.getInstance().firebaseAdmin.updatePlanNutrtionFirebase(item);
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
                }
            });

            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(getContext(), R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {hideLoading();}

    @Override
    public void insertNutritionPlanFirebase(boolean end) {}

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {}

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {}

    @Override
    public void updateNutritionPlanFirebase(boolean end) {}
}
