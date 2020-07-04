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

public class FragmentShowPlan extends BaseFragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Button buttonShowSport;
    private Button buttonShowNutrition;
    private Button buttonShowClose;
    private Callback mListener;
    private Runnable toolbarRunnable;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback{
        void onClickButtonShowPlanSport();
        void onClickButtonShowPlanNutrition();
        void onClickButtonShowPlanClose();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            this.mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
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

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_show_plan, container, false);


        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.findById(view);
        this.onClickClose();
        this.onClickSport();
        this.onClickNutrition();

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View view) {
        this.buttonShowClose = view.findViewById(R.id.three_button);
        this.buttonShowNutrition = view.findViewById(R.id.second_button);
        this.buttonShowSport = view.findViewById(R.id.first_button);
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickClose(){
        this.buttonShowClose.setOnClickListener(v -> {
            if (this.mListener != null) {
                getActivity().getSupportFragmentManager().popBackStack();
                this.mListener.onClickButtonShowPlanClose();
            }
        });
    }

    /******************************** ONCLICK DEPORTES *************************************+/
     *
     */

    private void onClickSport(){
        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            this.buttonShowSport.setOnClickListener(v -> {
                if (this.mListener != null) {
                    this.mListener.onClickButtonShowPlanSport();
                }
            });
        } else {
            this.buttonShowSport.setEnabled(false);
        }
    }

    /******************************** ONCLICK NUTRICION *************************************+/
     *
     */

    private void onClickNutrition() {
        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            this.buttonShowNutrition.setOnClickListener(v -> {
                if (this.mListener != null) {
                    this.mListener.onClickButtonShowPlanNutrition();
                }
            });
        } else {
            this.buttonShowNutrition.setEnabled(false);
        }
    }
}
