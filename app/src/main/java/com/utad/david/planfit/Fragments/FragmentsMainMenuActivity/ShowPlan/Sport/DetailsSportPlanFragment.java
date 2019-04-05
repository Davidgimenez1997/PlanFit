package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.Sport;


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
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Plan.Show.Sport.ShowDetailsSportPlanAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;

public class DetailsSportPlanFragment extends Fragment implements FirebaseAdmin.FirebaseAdminCreateShowPlanSport{

    private static final String ARG_PARAM1 = "param1";

    private ArrayList<PlanSport> planSports;

    public DetailsSportPlanFragment() {
        // Required empty public constructor
    }

    public static DetailsSportPlanFragment newInstance(ArrayList<PlanSport> defaultSports) {
        DetailsSportPlanFragment fragment = new DetailsSportPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM1,defaultSports);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());

        if (getArguments() != null) {
            planSports = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    private RecyclerView mRecyclerView;
    private ShowDetailsSportPlanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details_sport_plan, container, false);

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateShowPlanSport(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllSportPlanFavorite();

        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ShowDetailsSportPlanAdapter(planSports, item -> {

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
                                SessionUser.getInstance().firebaseAdmin.updatePlanSportFirebase(item);
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
                                SessionUser.getInstance().firebaseAdmin.updatePlanSportFirebase(item);
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
    public void updateSportPlanFirebase(boolean end) {}

    @Override
    public void insertSportPlanFirebase(boolean end) {}

    @Override
    public void downloadSportPlanFirebase(boolean end) {}

    @Override
    public void emptySportPlanFirebase(boolean end) {}

    @Override
    public void deleteSportPlanFirebase(boolean end) {}
}
