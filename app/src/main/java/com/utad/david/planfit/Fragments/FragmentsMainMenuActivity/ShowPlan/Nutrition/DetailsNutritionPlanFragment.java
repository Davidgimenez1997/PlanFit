package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.Nutrition;


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
import butterknife.ButterKnife;
import com.utad.david.planfit.Adapter.Plan.Show.Nutrition.ShowDetailsNutritionPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;

import java.util.ArrayList;

public class DetailsNutritionPlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminCreateShowPlanNutrition {

    private static final String ARG_PARAM1 = "param1";
    private ArrayList<PlanNutrition> planNutritions;


    public DetailsNutritionPlanFragment() {
        // Required empty public constructor
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

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateShowPlanNutrition(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllNutrtionPlanFavorite();

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ShowDetailsNutritionPlanAdapter(planNutritions, item -> {

            if(item.getIsOk().equals("yes")){

                final CharSequence[] items = {"Si","Cancelar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("¿Te has equivocado?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemDialog) {
                        switch (itemDialog) {
                            case 0:
                                showLoading();
                                item.setIsOk("no");
                                SessionUser.getInstance().firebaseAdmin.updatePlanNutrtionFirebase(item);
                                mAdapter.notifyDataSetChanged();
                                hideLoading();
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();

            }else if(item.getIsOk().equals("no")){

                final CharSequence[] items = {"Si","Cancelar"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("¿Ya lo has realizado?");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemDialog) {
                        switch (itemDialog) {
                            case 0:
                                showLoading();
                                item.setIsOk("yes");
                                SessionUser.getInstance().firebaseAdmin.updatePlanNutrtionFirebase(item);
                                mAdapter.notifyDataSetChanged();
                                hideLoading();
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();


            }
        });

        mRecyclerView.setAdapter(mAdapter);


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
    public void insertNutritionPlanFirebase(boolean end) {}

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {}

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {}

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {}

    @Override
    public void updateNutritionPlanFirebase(boolean end) {}

}