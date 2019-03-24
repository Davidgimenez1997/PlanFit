package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.CreatePlan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.CreatePlan.Sport.SportCreatePlanFragment;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.R;

import java.util.List;

public class FragmentCreatePlan extends Fragment {

    public FragmentCreatePlan() {
        // Required empty public constructor
    }

    public interface Callback {
        void onClickSportPlan();
        void onClickNutritionPlan();
        void onClickSaveAndExit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView timeDuration;
    private Button buttonSelectSport;
    private Button buttonSelectNutrition;
    private Button buttonSaveAndExit;
    private Callback mlistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        findById(view);
        onClickOpenSport();
        onClickOpenNutrition();
        onClickSaveAndClose();

        return view;
    }

    private void findById(View view){
        timeDuration = view.findViewById(R.id.time_plan);
        buttonSelectSport = view.findViewById(R.id.button_select_sport);
        buttonSelectNutrition = view.findViewById(R.id.button_Select_nutrition);
        buttonSaveAndExit = view.findViewById(R.id.button_exit_save);

    }

    private void onClickOpenNutrition() {
        buttonSelectNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.onClickNutritionPlan();
                }
            }
        });
    }

    private void onClickOpenSport() {
        buttonSelectSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.onClickSportPlan();
                }
            }
        });
    }

    private void onClickSaveAndClose(){
        buttonSaveAndExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    mlistener.onClickSaveAndExit();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mlistener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener = null;

    }

}
