package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class FragmentCreatePlan extends Fragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Button buttonSelectSport;
    private Button buttonSelectNutrition;
    private Button buttonSaveAndExit;
    private Callback mlistener;
    private Runnable toolbarRunnable;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickSportPlan();
        void onClickNutritionPlan();
        void onClickSaveAndExit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mlistener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener = null;

    }

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            Fabric.with(getContext(), new Crashlytics());
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        findById(view);
        onClickOpenSport();
        onClickOpenNutrition();
        onClickSaveAndClose();

        return view;
    }


    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View view){
        buttonSelectSport = view.findViewById(R.id.button_select_sport);
        buttonSelectNutrition = view.findViewById(R.id.button_Select_nutrition);
        buttonSaveAndExit = view.findViewById(R.id.button_exit);

    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickSaveAndClose(){
        buttonSaveAndExit.setOnClickListener(v -> {
            if (mlistener != null) {
                getActivity().getSupportFragmentManager().popBackStack();
                mlistener.onClickSaveAndExit();
            }
        });
    }

    /******************************** ONCLICK DEPORTES *************************************+/
     *
     */

    private void onClickOpenSport() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonSelectSport.setOnClickListener(v -> {
                if(mlistener!=null){
                    mlistener.onClickSportPlan();
                }
            });
        }else{
            buttonSelectSport.setEnabled(false);
        }
    }

    /******************************** ONCLICK NUTRICION *************************************+/
     *
     */

    private void onClickOpenNutrition() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonSelectNutrition.setOnClickListener(v -> {
                if(mlistener!=null){
                    mlistener.onClickNutritionPlan();
                }
            });
        }else{
            buttonSelectNutrition.setEnabled(false);
        }
    }

}
