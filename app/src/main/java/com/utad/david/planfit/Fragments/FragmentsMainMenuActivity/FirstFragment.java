package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.CreatePlan.FragmentCreatePlan;
import com.utad.david.planfit.R;


public class FirstFragment extends Fragment {

    private static String SELECTED = "SELECTED";
    private int selected;

    public static FirstFragment newInstance(int selected) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(SELECTED,selected);
        fragment.setArguments(args);
        return fragment;
    }

    private OnFragmentInteractionListener mListener;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selected = getArguments().getInt(SELECTED);

    }

    public void setmListener(OnFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    private TextView textViewInfo;
    private Button first_button;
    private Button second_button;
    private Button three_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.firstfragment, container, false);

        findViewById(view);

        switch (selected){
            case 0:
                configViewSport();
                break;
            case 1:
                configViewNutrition();
                break;
            case 2:
                configViewPlan();
                break;
            case 3:
                configFavorite();
                break;
        }

        return view;
    }

    private void configFavorite() {
        textViewInfo.setText(R.string.itemfavoritos);
        first_button.setText(R.string.first_nav_name);
        second_button.setText(R.string.two_nav_name);
        three_button.setVisibility(View.INVISIBLE);
        onClickFavoriteSport();
        onClickFavoriteNutrition();
    }

    private void onClickFavoriteNutrition() {
        second_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickNutritionFavorite();
                }
            }
        });
    }

    private void onClickFavoriteSport() {
        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickSportFavorite();
                }
            }
        });
    }

    private void configViewSport(){
        textViewInfo.setText(getString(R.string.qu_buscas));
        first_button.setText(getString(R.string.adelgazar));
        second_button.setText(getString(R.string.tonificar));
        three_button.setText(getString(R.string.ganar_volumen));
        onClickAdelgazarSport();
        onClickTonificarSport();
        onClickGainVolumeSport();

    }

    private void onClickAdelgazarSport(){
        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnAdelgazarSport();
                }
            }
        });
    }

    private void onClickTonificarSport(){
        second_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnTonificarSport();
                }
            }
        });
    }

    private void onClickGainVolumeSport(){
        three_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnGanarVolumenSport();
                }
            }
        });
    }

    private void configViewNutrition(){
        textViewInfo.setText(getString(R.string.qu_buscas));
        first_button.setText(getString(R.string.adelgazar));
        second_button.setText(getString(R.string.tonificar));
        three_button.setText(getString(R.string.ganar_volumen));
        onClickAdelgazarNutrition();
        onClickTonificarNutrition();
        onClickGainVolumeNutrition();
    }

    private void onClickAdelgazarNutrition(){
        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnAdelgazarNutrition();
                }
            }
        });
    }

    private void onClickTonificarNutrition(){
        second_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnTonificarNutrition();
                }
            }
        });
    }

    private void onClickGainVolumeNutrition(){
        three_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnGanarVolumenNutrition();
                }
            }
        });
    }

    private void configViewPlan(){
        textViewInfo.setText(getString(R.string.estas_preparado));
        textViewInfo.setTextSize(35);
        first_button.setText(getString(R.string.crear_plan));
        second_button.setText(getString(R.string.ver_tu_plan));
        three_button.setVisibility(View.INVISIBLE);
        onClickCreatePlan();
        onClickShowPlan();
    }

    private void onClickCreatePlan() {
        first_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnCreatePlan();
                }
            }
        });
    }

    private void onClickShowPlan() {
        second_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.clickOnShowPlan();
                }
            }
        });
    }

    private void findViewById(View view){
        textViewInfo = view.findViewById(R.id.textViewInfo);
        first_button = view.findViewById(R.id.first_button);
        second_button = view.findViewById(R.id.second_button);
        three_button = view.findViewById(R.id.three_button);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
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
}

