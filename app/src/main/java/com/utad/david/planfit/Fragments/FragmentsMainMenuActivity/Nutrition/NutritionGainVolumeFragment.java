package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition;

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
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Adapter.Nutrition.NutritionGainVolumeAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.Nutrition.NutritionDetailsDialogFragment;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.Collections;
import java.util.List;

public class NutritionGainVolumeFragment extends Fragment implements FirebaseAdmin.FirebaseAdminDownloandFragmentData,NutritionDetailsDialogFragment.CallbackNutrition {

    public NutritionGainVolumeFragment() {
        // Required empty public constructor
    }

    private NutritionGainVolumeFragment fragment;

    public NutritionGainVolumeFragment newInstanceSlimming() {
        this.fragment = this;
        return this.fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminDownloandFragmentData(this);
            SessionUser.getInstance().firebaseAdmin.downloadGainVolumeNutrition();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private NutritionDetailsDialogFragment newFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutrition_recycleview, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_nutrition);
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
    public void downloandCollectionNutritionGainVolume(boolean end) {
        if(end){
            hideLoading();

            List<NutritionGainVolume> nutritionGainVolumes = SessionUser.getInstance().firebaseAdmin.nutritionGainVolumeListNutrition;

            Collections.sort(nutritionGainVolumes);

            mAdapter = new NutritionGainVolumeAdapter(nutritionGainVolumes, item -> {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);
                newFragment = NutritionDetailsDialogFragment.newInstanceGainVolume(item,2,getContext());
                newFragment.setCallbackNutrition(fragment);
                newFragment.show(transaction, "dialog");
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClickClose() {
        newFragment.dismiss();
    }

    @Override
    public void downloandCollectionSportGainVolume(boolean end) {}

    @Override
    public void downloandCollectionNutritionSlimming(boolean end) {}

    @Override
    public void downloandCollectionNutritionToning(boolean end) {}

    @Override
    public void downloandCollectionSportSlimming(boolean end) {}

    @Override
    public void downloandCollectionSportToning(boolean end) {}
}
