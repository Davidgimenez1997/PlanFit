package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.Nutrition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.utad.david.planfit.Adapter.Plan.Show.Details.ShowDetailsNutritionPlanAdapter;
import com.utad.david.planfit.Adapter.Plan.Show.ShowNutritionPlanAdapter;
import com.utad.david.planfit.Adapter.Plan.Show.ShowSportPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.Sport.DetailsSportPlanFragment;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowNutritionPlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminCreateAndShowPlan {

    public ShowNutritionPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private FragmentActivity myContext;
    private ArrayList<ArrayList<PlanNutrition>> listToListPlan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_nutrition_plan, container, false);

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateAndShowPlan(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllNutrtionPlanFavorite();
        showLoading();
        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
        linearLayout = view.findViewById(R.id.linear_empty_favorites);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        hideLoading();

        return view;
    }

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {
        if (end == true) {
            hideLoading();

            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            final List<PlanNutrition> planNutritions = SessionUser.getInstance().firebaseAdmin.allPlanNutrition;
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
                FragmentManager fragManager = myContext.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, detailsNutritionPlanFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            mRecyclerView.setAdapter(mAdapter);
            if(arrNutrition.size()==0){
                updateNutritionPlanFirebase(false);
            }else{
                updateNutritionPlanFirebase(true);
            }
        }
    }

    @Override
    public void updateNutritionPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
            boolean endOk = true;
            List<PlanNutrition> planNutritions = SessionUser.getInstance().firebaseAdmin.allPlanNutrition;
            final ArrayList<PlanNutrition> arrNutrition = new ArrayList<>();
            for(PlanNutrition item:planNutritions ){
                arrNutrition.add(item);
            }
            Collections.sort(arrNutrition);
            for(int i=0;i<arrNutrition.size();i++){
                if(arrNutrition.get(i).getIsOk().equals("no")){
                    endOk = false;
                }
            }
            if(endOk==true){
                Log.d("TodosOk","estan todos ok");
                final CharSequence[] items = {"Restablecer","Cancelar"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Felicidades has completado todos!!!");
                builder.setItems(items, (dialog, itemDialog) -> {
                    switch (itemDialog) {
                        case 0:
                            showLoading();
                            for (PlanNutrition planNutrition:arrNutrition){
                                planNutrition.setIsOk("no");
                                SessionUser.getInstance().firebaseAdmin.updatePlanNutrtionFirebase(planNutrition);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 1:
                            dialog.dismiss();
                            break;
                    }
                });
                builder.show();
                return;
            }
        }
    }

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
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
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void insertNutritionPlanFirebase(boolean end) {}

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {}

    @Override
    public void insertSportPlanFirebase(boolean end) {}

    @Override
    public void downloadSportPlanFirebase(boolean end) {}

    @Override
    public void emptySportPlanFirebase(boolean end) {}

    @Override
    public void deleteSportPlanFirebase(boolean end) {}

    @Override
    public void updateSportPlanFirebase(boolean end) {}

}
