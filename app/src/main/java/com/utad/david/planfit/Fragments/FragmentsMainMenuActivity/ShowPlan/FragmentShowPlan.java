package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import com.utad.david.planfit.R;

public class FragmentShowPlan extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
