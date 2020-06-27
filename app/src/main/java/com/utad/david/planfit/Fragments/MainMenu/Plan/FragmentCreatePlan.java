package com.utad.david.planfit.Fragments.MainMenu.Plan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class FragmentCreatePlan extends BaseFragment {

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
            this.mlistener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mlistener = null;

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

        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            Fabric.with(getContext(), new Crashlytics());
        } else {
            Toast.makeText(getContext(),getString(R.string.info_network_device), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.findById(view);
        this.onClickOpenSport();
        this.onClickOpenNutrition();
        this.onClickSaveAndClose();

        return view;
    }


    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View view){
        this.buttonSelectSport = view.findViewById(R.id.button_select_sport);
        this.buttonSelectNutrition = view.findViewById(R.id.button_Select_nutrition);
        this.buttonSaveAndExit = view.findViewById(R.id.button_exit);

    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickSaveAndClose(){
        this.buttonSaveAndExit.setOnClickListener(v -> {
            if (this.mlistener != null) {
                getActivity().getSupportFragmentManager().popBackStack();
                this.mlistener.onClickSaveAndExit();
            }
        });
    }

    /******************************** ONCLICK DEPORTES *************************************+/
     *
     */

    private void onClickOpenSport() {
        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            this.buttonSelectSport.setOnClickListener(v -> {
                if (this.mlistener != null){
                    this.mlistener.onClickSportPlan();
                }
            });
        } else {
            this.buttonSelectSport.setEnabled(false);
        }
    }

    /******************************** ONCLICK NUTRICION *************************************+/
     *
     */

    private void onClickOpenNutrition() {
        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            this.buttonSelectNutrition.setOnClickListener(v -> {
                if(this.mlistener != null){
                    this.mlistener.onClickNutritionPlan();
                }
            });
        } else {
            this.buttonSelectNutrition.setEnabled(false);
        }
    }

}
