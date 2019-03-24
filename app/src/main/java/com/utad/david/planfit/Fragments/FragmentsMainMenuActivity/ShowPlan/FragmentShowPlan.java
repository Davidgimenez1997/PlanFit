package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;

public class FragmentShowPlan extends Fragment implements FirebaseAdmin.FirebaseAdminCreateAndShowPlan {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateAndShowPlan(this);
        showLoading();
        SessionUser.getInstance().firebaseAdmin.downloadAllSportPlanFavorite();
        SessionUser.getInstance().firebaseAdmin.downloadAllNutrtionPlanFavorite();
    }

    public interface Callback{
        void onClickButtonShowPlanSport();
        void onClickButtonShowPlanNutrition();
        void onClickButtonShowPlanClose();

    }

    private Button buttonShowSport;
    private Button buttonShowNutrition;
    private Button buttonShowClose;
    private Callback mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_plan, container, false);

        findById(view);
        onClickClose();
        onClickSport();
        onClickNutrition();

        return view;
    }

    private void onClickNutrition() {
        buttonShowNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onClickButtonShowPlanNutrition();
                }
            }
        });
    }

    private void findById(View view) {
        buttonShowClose = view.findViewById(R.id.three_button);
        buttonShowNutrition = view.findViewById(R.id.second_button);
        buttonShowSport = view.findViewById(R.id.first_button);
    }

    private void onClickClose(){
        buttonShowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    getActivity().getSupportFragmentManager().popBackStack();
                    mListener.onClickButtonShowPlanClose();
                }
            }
        });
    }

    private void onClickSport(){
        buttonShowSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.onClickButtonShowPlanSport();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void insertSportPlanFirebase(boolean end) {
        if(end==true){
            buttonShowSport.setEnabled(true);
        }
    }

    @Override
    public void insertNutritionPlanFirebase(boolean end) {
        if(end==true){
            buttonShowNutrition.setEnabled(true);
        }
    }

    @Override
    public void emptySportPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
            buttonShowSport.setEnabled(false);
        }
    }

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
            buttonShowNutrition.setEnabled(false);
        }
    }

    @Override
    public void downloadSportPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
        }
    }

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
        }
    }

    @Override
    public void deleteSportPlanFirebase(boolean end) {

    }

    @Override
    public void deleteAllSportPlanFirebase(boolean end) {

    }

    @Override
    public void updateSportPlanFirebase(boolean end) {

    }

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {

    }

    @Override
    public void deleteAllNutritionPlanFirebase(boolean end) {

    }

    @Override
    public void updateNutritionPlanFirebase(boolean end) {

    }

}
