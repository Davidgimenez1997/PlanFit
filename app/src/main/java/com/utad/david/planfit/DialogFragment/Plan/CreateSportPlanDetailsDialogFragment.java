package com.utad.david.planfit.DialogFragment.Plan;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;

public class CreateSportPlanDetailsDialogFragment extends DialogFragment{


    private static String SPORT = "SPORT";
    private DefaultSport defaultSport;

    public static CreateSportPlanDetailsDialogFragment newInstance(DefaultSport defaultSport) {
        CreateSportPlanDetailsDialogFragment fragment = new CreateSportPlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SPORT, defaultSport);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackCreateSport{
        void onClickClose();
        void onClickSave(DefaultSport defaultSport,String timeStart,String timeEnd);
    }

    private TextView textViewTitle;
    private ImageView imageViewSport;
    private Spinner spinnerStart;
    private Spinner spinnerEnd;
    private Button buttonSave;
    private Button buttonClose;
    private CallbackCreateSport listener;
    private String timeStart;
    private String timeEnd;

    public void setListener(CallbackCreateSport listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultSport = getArguments().getParcelable(SPORT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_sport_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(view);
        putData();
        onClickButtonClose();
        onClickButtonSave();

        configureSpinnerStart();
        configureSpinnerEnd();

        return view;
    }

    private void configureSpinnerStart() {
        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                timeStart=spinnerStart.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void configureSpinnerEnd() {
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                timeEnd=spinnerEnd.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void onClickButtonClose() {
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClickClose();
                }
            }
        });
    }

    private void onClickButtonSave() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeStart.equals(timeEnd)){
                    Toast.makeText(getContext(),"No puedes elegir la misma hora de comienzo y de fin.",Toast.LENGTH_LONG).show();
                }else{
                    if(listener!=null){
                        listener.onClickSave(defaultSport,timeStart,timeEnd);
                    }
                }
            }
        });
    }

    private void findById(View view) {
        textViewTitle = view.findViewById(R.id.textTitleCreateSport);
        imageViewSport = view.findViewById(R.id.imageViewCreateSport);
        spinnerStart = view.findViewById(R.id.spinner_comienzo);
        spinnerEnd = view.findViewById(R.id.spinner_fin);
        buttonSave = view.findViewById(R.id.save_create_sport);
        buttonClose = view.findViewById(R.id.close_create_sport);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultSport.getName());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultSport.getPhoto()).into(imageViewSport);
    }

}
