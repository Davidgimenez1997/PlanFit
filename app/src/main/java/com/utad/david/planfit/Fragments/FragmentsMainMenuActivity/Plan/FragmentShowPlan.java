package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan;

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
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class FragmentShowPlan extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(), new Crashlytics());
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
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
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonShowNutrition.setOnClickListener(v -> {
                if(mListener!=null){
                    mListener.onClickButtonShowPlanNutrition();
                }
            });
        }else{
            buttonShowNutrition.setEnabled(false);
        }
    }

    private void findById(View view) {
        buttonShowClose = view.findViewById(R.id.three_button);
        buttonShowNutrition = view.findViewById(R.id.second_button);
        buttonShowSport = view.findViewById(R.id.first_button);
    }

    private void onClickClose(){
        buttonShowClose.setOnClickListener(v -> {
            if(mListener!=null){
                getActivity().getSupportFragmentManager().popBackStack();
                mListener.onClickButtonShowPlanClose();
            }
        });
    }

    private void onClickSport(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonShowSport.setOnClickListener(v -> {
                if(mListener!=null){
                    mListener.onClickButtonShowPlanSport();
                }
            });
        }else{
            buttonShowSport.setEnabled(false);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
