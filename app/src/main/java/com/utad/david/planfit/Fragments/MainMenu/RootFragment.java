package com.utad.david.planfit.Fragments.MainMenu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class RootFragment extends BaseFragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SELECTED = "SELECTED";
    private int selected;
    private Runnable toolbarRunnable;

    private TextView textViewInfo;
    private Button first_button;
    private Button second_button;
    private Button three_button;
    private boolean isDeviceNetwork;

    private Context context;
    private Sport sports;
    private Nutrition nutritions;
    private Favorite favorites;
    private Plan plans;

    /******************************** INTERFAZES *************************************+/
     *
     */

    public interface Sport {
        void clickOnAdelgazarSport();
        void clickOnTonificarSport();
        void clickOnGanarVolumenSport();
    }

    public interface Nutrition {
        void clickOnAdelgazarNutrition();
        void clickOnTonificarNutrition();
        void clickOnGanarVolumenNutrition();
    }

    public interface Favorite {
        void clickSportFavorite();
        void clickNutritionFavorite();
    }

    public interface Plan {
        void clickOnCreatePlan();
        void clickOnShowPlan();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context != null) {
            if (context instanceof Sport) {
                this.sports = (Sport) context;
            }
            if (context instanceof Nutrition) {
                this.nutritions = (Nutrition) context;
            }
            if (context instanceof Favorite) {
                this.favorites = (Favorite) context;
            }
            if (context instanceof Plan) {
                this.plans = (Plan) context;
            }
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.context instanceof Sport) {
            this.sports = null;
        } else if (this.context instanceof Nutrition) {
            this.nutritions = null;
        } else if (this.context instanceof Favorite) {
            this.favorites = null;
        } else if (this.context instanceof Plan) {
            this.plans = null;
        }
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

        if (getArguments() != null) {
            this.selected = getArguments().getInt(SELECTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.root_fragment, container, false);

        if (this.toolbarRunnable != null) {
            this.toolbarRunnable.run();
        }

        this.isDeviceNetwork = UtilsNetwork.checkConnectionInternetDevice(this.context);

        this.findViewById(view);

        switch (this.selected){
            case Constants.ModeRootFragment.MODE_SPORT:
                this.configViewSport();
                break;
            case Constants.ModeRootFragment.MODE_NUTRITION:
                this.configViewNutrition();
                break;
            case Constants.ModeRootFragment.MODE_FAVORITE:
                this.configFavorite();
                break;
            case Constants.ModeRootFragment.MODE_PLAN:
                this.configViewPlan();
                break;

        }

        return view;
    }


    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findViewById(View view){
        this.textViewInfo = view.findViewById(R.id.textViewInfo);
        this.first_button = view.findViewById(R.id.first_button);
        this.second_button = view.findViewById(R.id.second_button);
        this.three_button = view.findViewById(R.id.three_button);
    }

    /******************************** CONFIGURA DEPORTES *************************************+/
     *
     */

    private void configViewSport(){
        this.textViewInfo.setText(getString(R.string.first_nav_name));
        this.first_button.setText(getString(R.string.adelgazar));
        this.second_button.setText(getString(R.string.tonificar));
        this.three_button.setText(getString(R.string.ganar_volumen));

        if (this.isDeviceNetwork) {
            this.onClickAdelgazarSport();
            this.onClickTonificarSport();
            this.onClickGainVolumeSport();
        } else {
           this.deviceOffline();
        }
    }

    private void onClickAdelgazarSport(){
        this.first_button.setOnClickListener(v -> {
            if (this.sports != null) {
                this.sports.clickOnAdelgazarSport();
            }
        });
    }

    private void onClickTonificarSport(){
        this.second_button.setOnClickListener(v -> {
            if (this.sports != null) {
                this.sports.clickOnTonificarSport();
            }
        });
    }

    private void onClickGainVolumeSport(){
        this.three_button.setOnClickListener(v -> {
            if (this.sports != null) {
                this.sports.clickOnGanarVolumenSport();
            }
        });
    }

    /******************************** CONFIGURA NUTRICION *************************************+/
     *
     */

    private void configViewNutrition(){
        this.textViewInfo.setText(getString(R.string.two_nav_name));
        this.first_button.setText(getString(R.string.adelgazar));
        this.second_button.setText(getString(R.string.tonificar));
        this.three_button.setText(getString(R.string.ganar_volumen));

        if (this.isDeviceNetwork) {
            this.onClickAdelgazarNutrition();
            this.onClickTonificarNutrition();
            this.onClickGainVolumeNutrition();
        } else {
            this.deviceOffline();
        }
    }

    private void onClickAdelgazarNutrition(){
        this.first_button.setOnClickListener(v -> {
            if (this.nutritions != null){
                this.nutritions.clickOnAdelgazarNutrition();
            }
        });
    }

    private void onClickTonificarNutrition(){
        this.second_button.setOnClickListener(v -> {
            if (this.nutritions != null) {
                this.nutritions.clickOnTonificarNutrition();
            }
        });
    }

    private void onClickGainVolumeNutrition(){
        this.three_button.setOnClickListener(v -> {
            if (this.nutritions != null) {
                this.nutritions.clickOnGanarVolumenNutrition();
            }
        });
    }

    /******************************** CONFIGURA FAVORITOS *************************************+/
     *
     */

    private void configFavorite() {
        this.textViewInfo.setText(R.string.itemfavoritos);
        this.first_button.setText(R.string.first_nav_name);
        this.second_button.setText(R.string.two_nav_name);
        this.three_button.setVisibility(View.INVISIBLE);

        if (this.isDeviceNetwork) {
            this.onClickFavoriteSport();
            this.onClickFavoriteNutrition();
        } else {
            this.deviceOffline();
        }
    }

    private void onClickFavoriteNutrition() {
        this.second_button.setOnClickListener(v -> {
            if (this.favorites != null) {
                this.favorites.clickNutritionFavorite();
            }
        });
    }

    private void onClickFavoriteSport() {
        this.first_button.setOnClickListener(v -> {
            if (this.favorites != null) {
                this.favorites.clickSportFavorite();
            }
        });
    }

    /******************************** CONFIGURA PLAN *************************************+/
     *
     */

    private void configViewPlan(){
        this.textViewInfo.setText(getString(R.string.estas_preparado));
        this.first_button.setText(getString(R.string.crear_plan));
        this.second_button.setText(getString(R.string.ver_tu_plan));
        this.three_button.setVisibility(View.INVISIBLE);

        if (this.isDeviceNetwork) {
            this.onClickCreatePlan();
            this.onClickShowPlan();
        } else {
            this.deviceOffline();
        }
    }

    private void onClickCreatePlan() {
        this.first_button.setOnClickListener(v -> {
            if (this.plans != null) {
                this.plans.clickOnCreatePlan();
            }
        });
    }

    private void onClickShowPlan() {
        this.second_button.setOnClickListener(v -> {
            if (this.plans != null) {
                this.plans.clickOnShowPlan();
            }
        });
    }

    private void deviceOffline() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        this.first_button.setEnabled(false);
        this.second_button.setEnabled(false);
        this.three_button.setEnabled(false);
    }
}

