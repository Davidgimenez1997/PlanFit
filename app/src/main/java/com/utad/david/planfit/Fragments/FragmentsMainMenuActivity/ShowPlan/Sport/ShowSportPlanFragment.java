package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.Sport;


import android.app.ProgressDialog;
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
import com.utad.david.planfit.Adapter.Plan.show.ShowSportPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowSportPlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminCreateAndShowPlan{

    public ShowSportPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateAndShowPlan(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllSportPlanFavorite();
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_sport_plan, container, false);

        showLoading();
        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
    public void downloadSportPlanFirebase(boolean end) {
        if(end){
            hideLoading();
            List<PlanSport> planSports = SessionUser.getInstance().firebaseAdmin.allPlanSport;
            ArrayList<PlanSport> arrSport = new ArrayList<>();

            for(PlanSport item:planSports){
                arrSport.add(item);
            }
            Collections.sort(arrSport);

            mAdapter = new ShowSportPlanAdapter(arrSport, new ShowSportPlanAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PlanSport item) {

                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void insertSportPlanFirebase(boolean end) {

    }

    @Override
    public void emptySportPlanFirebase(boolean end) {

    }

    @Override
    public void deleteSportPlanFirebase(boolean end) {

    }

    @Override
    public void insertNutritionPlanFirebase(boolean end) {

    }

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {

    }

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {

    }

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {

    }
}
