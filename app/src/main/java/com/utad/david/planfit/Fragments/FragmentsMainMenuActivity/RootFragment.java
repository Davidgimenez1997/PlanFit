package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity;


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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;


public class RootFragment extends Fragment{

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SELECTED = "SELECTED";
    private int selected;
    private Runnable toolbarRunnable;
    private Callback mListener;


    private TextView textViewInfo;
    private Button first_button;
    private Button second_button;
    private Button three_button;
    private boolean isDeviceNetwork;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void clickOnAdelgazarSport();
        void clickOnTonificarSport();
        void clickOnGanarVolumenSport();
        void clickOnAdelgazarNutrition();
        void clickOnTonificarNutrition();
        void clickOnGanarVolumenNutrition();
        void clickOnCreatePlan();
        void clickOnShowPlan();
        void clickSportFavorite();
        void clickNutritionFavorite();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /******************************** PROGRESS DIALOG Y METODOS *************************************+/
     *
     */

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

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static RootFragment newInstance(int selected) {
        RootFragment fragment = new RootFragment();
        Bundle args = new Bundle();
        args.putInt(SELECTED,selected);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }


    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());

        selected = getArguments().getInt(SELECTED);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.root_fragment, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        if (UtilsNetwork.checkConnectionInternetDevice(getContext())) {
            isDeviceNetwork = true;
        }else{
            isDeviceNetwork = false;
        }

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        findViewById(view);

        switch (selected){
            case 0:
                configViewSport();
                break;
            case 1:
                configViewNutrition();
                break;
            case 2:
                configFavorite();
                break;
            case 3:
                configViewPlan();
                break;
        }

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findViewById(View view){
        textViewInfo = view.findViewById(R.id.textViewInfo);
        first_button = view.findViewById(R.id.first_button);
        second_button = view.findViewById(R.id.second_button);
        three_button = view.findViewById(R.id.three_button);
    }

    /******************************** CONFIGURA FAVORITOS *************************************+/
     *
     */

    private void configFavorite() {
        textViewInfo.setText(R.string.itemfavoritos);
        first_button.setText(R.string.first_nav_name);
        second_button.setText(R.string.two_nav_name);
        three_button.setVisibility(View.INVISIBLE);

        if(isDeviceNetwork){
            onClickFavoriteSport();
            onClickFavoriteNutrition();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            first_button.setEnabled(false);
            second_button.setEnabled(false);
            three_button.setEnabled(false);
        }
    }

    private void onClickFavoriteNutrition() {
        second_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickNutritionFavorite();
            }
        });
    }

    private void onClickFavoriteSport() {
        first_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickSportFavorite();
            }
        });
    }

    /******************************** CONFIGURA DEPORTES *************************************+/
     *
     */

    private void configViewSport(){
        textViewInfo.setText(getString(R.string.first_nav_name));
        first_button.setText(getString(R.string.adelgazar));
        second_button.setText(getString(R.string.tonificar));
        three_button.setText(getString(R.string.ganar_volumen));

        if(isDeviceNetwork){
            onClickAdelgazarSport();
            onClickTonificarSport();
            onClickGainVolumeSport();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            first_button.setEnabled(false);
            second_button.setEnabled(false);
            three_button.setEnabled(false);
        }

    }

    private void onClickAdelgazarSport(){
        first_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnAdelgazarSport();
            }
        });
    }

    private void onClickTonificarSport(){
        second_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnTonificarSport();
            }
        });
    }

    private void onClickGainVolumeSport(){
        three_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnGanarVolumenSport();
            }
        });
    }

    /******************************** CONFIGURA NUTRICION *************************************+/
     *
     */

    private void configViewNutrition(){
        textViewInfo.setText(getString(R.string.two_nav_name));
        first_button.setText(getString(R.string.adelgazar));
        second_button.setText(getString(R.string.tonificar));
        three_button.setText(getString(R.string.ganar_volumen));

        if(isDeviceNetwork){
            onClickAdelgazarNutrition();
            onClickTonificarNutrition();
            onClickGainVolumeNutrition();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            first_button.setEnabled(false);
            second_button.setEnabled(false);
            three_button.setEnabled(false);
        }
    }

    private void onClickAdelgazarNutrition(){
        first_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnAdelgazarNutrition();
            }
        });
    }

    private void onClickTonificarNutrition(){
        second_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnTonificarNutrition();
            }
        });
    }

    private void onClickGainVolumeNutrition(){
        three_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnGanarVolumenNutrition();
            }
        });
    }

    /******************************** CONFIGURA PLAN *************************************+/
     *
     */

    private void configViewPlan(){
        textViewInfo.setText(getString(R.string.estas_preparado));
        first_button.setText(getString(R.string.crear_plan));
        second_button.setText(getString(R.string.ver_tu_plan));
        three_button.setVisibility(View.INVISIBLE);

        if(isDeviceNetwork){
            onClickCreatePlan();
            onClickShowPlan();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            first_button.setEnabled(false);
            second_button.setEnabled(false);
            three_button.setEnabled(false);
        }
    }

    private void onClickCreatePlan() {
        first_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnCreatePlan();
            }
        });
    }

    private void onClickShowPlan() {
        second_button.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickOnShowPlan();
            }
        });
    }
}

