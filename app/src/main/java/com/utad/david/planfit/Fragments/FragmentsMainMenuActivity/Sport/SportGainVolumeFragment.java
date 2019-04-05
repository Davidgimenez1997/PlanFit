package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.utad.david.planfit.Adapter.Sport.SportGainVolumeAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.util.List;

public class SportGainVolumeFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData , SportDetailsDialogFragment.CallbackSport{

    public SportGainVolumeFragment() {
        // Required empty public constructor
    }

    private SportGainVolumeFragment fragment;

    public SportGainVolumeFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminDownloandFragmentData(this);
        SessionUser.getInstance().firebaseAdmin.downloadGainVolumeSport();
        Fabric.with(getContext(), new Crashlytics());

    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SportDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sport_recycleview, container, false);
        showLoading();
        mRecyclerView = view.findViewById(R.id.recycler_view_sport);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public void downloandCollectionSportGainVolume(boolean end) {
        if(end){
            hideLoading();
            List<SportGainVolume> sportGainVolumes = SessionUser.getInstance().firebaseAdmin.sportGainVolumeListSport;
            mAdapter = new SportGainVolumeAdapter(sportGainVolumes, new SportGainVolumeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SportGainVolume item) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        transaction.remove(prev);
                    }
                    transaction.addToBackStack(null);
                    newFragment = SportDetailsDialogFragment.newInstanceGainVolume(item,2);
                    newFragment.setListener(fragment);
                    newFragment.show(transaction, "dialog");
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void downloandCollectionSportSlimming(boolean end) {
    }

    @Override
    public void downloandCollectionSportToning(boolean end) {
    }

    @Override
    public void downloandCollectionNutritionSlimming(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionToning(boolean end) {

    }

    @Override
    public void downloandCollectionNutritionGainVolume(boolean end) {

    }
}
