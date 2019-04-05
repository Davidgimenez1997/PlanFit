package com.utad.david.planfit.DialogFragment.Plan.Sport;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.*;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import io.fabric.sdk.android.Fabric;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateSportPlanDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminCreateShowPlanSport{

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
    }

    private TextView textViewTitle;
    private ImageView imageViewSport;
    private Spinner spinnerStart;
    private Spinner spinnerEnd;
    private Button buttonSave;
    private Button buttonClose;
    private Button buttonDelete;
    private CallbackCreateSport listener;
    private String timeStart;
    private String timeEnd;
    private PlanSport current;

    public void setListener(CallbackCreateSport listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(getContext(),new Crashlytics());

        defaultSport = getArguments().getParcelable(SPORT);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateShowPlanSport(this);
        SessionUser.getInstance().firebaseAdmin.downloadAllSportPlanFavorite();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_sport_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showLoading();
        findById(view);
        putData();
        onClickButtonClose();
        onClickButtonSave();
        onClickButtonDelete();
        configureSpinnerStart();
        configureSpinnerEnd();
        return view;
    }

    private void configureSpinnerStart() {
        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                timeStart=spinnerStart.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void configureSpinnerEnd() {
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                timeEnd=spinnerEnd.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void onClickButtonClose() {
        buttonClose.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClickClose();
            }
        });
    }

    private void onClickButtonSave() {
        buttonSave.setOnClickListener(v -> {
            if(timeStart.equals(timeEnd)){
                Toast.makeText(getContext(),getString(R.string.info_create_plan),Toast.LENGTH_LONG).show();
            }else{
                if(listener!=null){
                    double intStart = convertStringToInt(timeStart);
                    double intEnd = convertStringToInt(timeEnd);
                    if(intStart>intEnd){
                        Toast.makeText(getContext(),"No puedes terminar antes de empezar",Toast.LENGTH_LONG).show();
                    }else{
                        showLoading();
                        SessionUser.getInstance().planSport.setName(defaultSport.getName());
                        SessionUser.getInstance().planSport.setPhoto(defaultSport.getPhoto());
                        SessionUser.getInstance().planSport.setTimeStart(intStart);
                        SessionUser.getInstance().planSport.setTimeEnd(intEnd);
                        SessionUser.getInstance().planSport.setIsOk("no");
                        UUID uuid = UUID.randomUUID();
                        SessionUser.getInstance().planSport.setId(uuid.toString());
                        SessionUser.getInstance().firebaseAdmin.dataCreateSportPlan();
                    }
                }
            }
        });
    }

    private double convertStringToInt(String timeStart){
        String [] parts = timeStart.split(":");
        String first = parts[0];
        String second = parts[1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(first);
        stringBuilder.append(".");
        stringBuilder.append(second);
        return Double.parseDouble(stringBuilder.toString());
    }

    private void onClickButtonDelete(){
        buttonDelete.setOnClickListener(v -> {
            showLoading();
            SessionUser.getInstance().firebaseAdmin.deleteSportPlan(defaultSport.getName());
        });
    }

    private void findById(View view) {
        textViewTitle = view.findViewById(R.id.textTitleCreateSport);
        imageViewSport = view.findViewById(R.id.imageViewCreateSport);
        spinnerStart = view.findViewById(R.id.spinner_comienzo);
        spinnerEnd = view.findViewById(R.id.spinner_fin);
        buttonSave = view.findViewById(R.id.save_create_sport);
        buttonDelete = view.findViewById(R.id.close_create_sport);
        buttonClose = view.findViewById(R.id.close_create_sport2);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultSport.getName());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultSport.getPhoto()).into(imageViewSport);
    }

    private List<PlanSport> planSports;

    @Override
    public void downloadSportPlanFirebase(boolean end) {
        if(end==true){
            planSports = new ArrayList<>();
            planSports = SessionUser.getInstance().firebaseAdmin.allPlanSport;
            for(PlanSport item : planSports){
                current = item;
                if(item.getName().equals(defaultSport.getName())){
                    buttonSave.setEnabled(false);
                    buttonDelete.setEnabled(true);

                    String [] timePlanArr = getResources().getStringArray(R.array.timePlan);

                    String timeStart;
                    String str_timeStart = String.valueOf(current.getTimeStart());
                    BigDecimal bigDecimal_start = new BigDecimal(str_timeStart);
                    long first_start = bigDecimal_start.longValue();
                    BigDecimal second_start = bigDecimal_start.remainder(BigDecimal.ONE);
                    StringBuilder stringBuilder_start = new StringBuilder(second_start.toString());
                    stringBuilder_start.delete(0,2);
                    if(stringBuilder_start.toString().length()==1){
                        timeStart = ("0"+Long.valueOf(first_start)+":"+stringBuilder_start.toString()+"0");
                    }else{
                        timeStart = ("0"+Long.valueOf(first_start)+":"+stringBuilder_start.toString());
                    }

                    String timeEnd;
                    String str_timeEnd = String.valueOf(current.getTimeEnd());
                    BigDecimal bigDecimal_end = new BigDecimal(str_timeEnd);
                    long first_end = bigDecimal_end.longValue();
                    BigDecimal second_End = bigDecimal_end.remainder(BigDecimal.ONE);
                    StringBuilder stringBuilder_end = new StringBuilder(second_End.toString());
                    stringBuilder_end.delete(0,2);
                    if(stringBuilder_end.toString().length()==1){
                        timeEnd = ("0"+Long.valueOf(first_end)+":"+stringBuilder_end.toString()+"0");
                    }else{
                        timeEnd = ("0"+Long.valueOf(first_end)+":"+stringBuilder_end.toString());
                    }


                    for(int i=0;i<timePlanArr.length;i++){
                        if(timePlanArr[i].equals(timeStart)){
                            spinnerStart.setSelection(i);
                        }
                    }

                    for(int i=0;i<timePlanArr.length;i++){
                        if(timePlanArr[i].equals(timeEnd)){
                            spinnerEnd.setSelection(i);
                        }
                    }
                    hideLoading();
                }
            }
        }
    }

    @Override
    public void insertSportPlanFirebase(boolean end) {
        if(end){
            buttonSave.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            Toast.makeText(getContext(),defaultSport.getName()+" "+getString(R.string.add_create_sport),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void deleteSportPlanFirebase(boolean end) {
        if(end==true){
            buttonSave.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            Toast.makeText(getContext(),defaultSport.getName()+" "+getString(R.string.delete_create_sport),Toast.LENGTH_LONG).show();
        }
    }

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

    @Override
    public void updateSportPlanFirebase(boolean end) {}

    @Override
    public void emptySportPlanFirebase(boolean end) {}

}
