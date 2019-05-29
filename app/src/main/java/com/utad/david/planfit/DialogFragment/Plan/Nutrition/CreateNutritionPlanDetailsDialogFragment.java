package com.utad.david.planfit.DialogFragment.Plan.Nutrition;

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
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateNutritionPlanDetailsDialogFragment extends DialogFragment implements FirebaseAdmin.FirebaseAdminCreateShowPlanNutrition {

    private static String NUTRITION = Constants.NutricionPlanDetails.EXTRA_NUTRITION;
    private DefaultNutrition defaultNutrition;

    private static String DESAYUNO = Constants.TiposPlanNutricion.DESAYUNO;
    private static String COMIDA = Constants.TiposPlanNutricion.COMIDA;
    private static String MERIENDA = Constants.TiposPlanNutricion.MERIENDA;
    private static String CENA = Constants.TiposPlanNutricion.CENA;


    public static CreateNutritionPlanDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        CreateNutritionPlanDetailsDialogFragment fragment = new CreateNutritionPlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(NUTRITION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    public interface CallbackCreateNutrtion{
        void onClickClose();
    }

    public void setListener(CallbackCreateNutrtion listener) {
        this.listener = listener;
    }

    private TextView textViewTitle;
    private ImageView imageViewNutrtion;
    private Spinner spinnerType;
    private Button buttonSave;
    private Button buttonClose;
    private Button buttonDelete;
    private CallbackCreateNutrtion listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            Fabric.with(getContext(),new Crashlytics());
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminCreateShowPlanNutrition(this);
            SessionUser.getInstance().firebaseAdmin.downloadAllNutrtionPlanFavorite();
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

        defaultNutrition = getArguments().getParcelable(NUTRITION);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_nutrition_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            findById(view);
            configureSpinnerDiseng();
            putData();
            onClickButtonClose();
            onClickButtonSave();
            onClickButtonDelete();
            configureSpinnerType();
        }else{
            findById(view);
            configureSpinnerDiseng();
            onClickButtonClose();
            putData();
            buttonSave.setEnabled(false);
            buttonDelete.setEnabled(false);
        }

        return view;
    }

    private void configureSpinnerDiseng() {
        ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.nutrition, R.layout.spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerType.setAdapter(spinnerArrayAdapter);
    }

    private int type;

    private void configureSpinnerType() {
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (spinnerType.getSelectedItem().toString()){
                    case Constants.TiposPlanNutricion.DESAYUNO:
                        type=1;
                        break;
                    case Constants.TiposPlanNutricion.COMIDA:
                        type=2;
                        break;
                    case Constants.TiposPlanNutricion.MERIENDA:
                        type=3;
                        break;
                    case Constants.TiposPlanNutricion.CENA:
                        type=4;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
            SessionUser.getInstance().planNutrition.setName(defaultNutrition.getName());
            SessionUser.getInstance().planNutrition.setPhoto(defaultNutrition.getPhoto());
            SessionUser.getInstance().planNutrition.setType(type);
            SessionUser.getInstance().planNutrition.setIsOk(Constants.ModePlan.NO);
            UUID uuid = UUID.randomUUID();
            SessionUser.getInstance().planNutrition.setId(uuid.toString());
            SessionUser.getInstance().firebaseAdmin.dataCreateNutrtionPlan();
        });
    }

    private void onClickButtonDelete(){
        buttonDelete.setOnClickListener(v -> {
            showLoading();
            SessionUser.getInstance().firebaseAdmin.deleteNutritionPlan(defaultNutrition.getName());
        });
    }

    private void findById(View view) {
        textViewTitle = view.findViewById(R.id.textTitleCreateSport);
        imageViewNutrtion = view.findViewById(R.id.imageViewCreateSport);
        spinnerType = view.findViewById(R.id.spinner_comienzo);
        buttonSave = view.findViewById(R.id.save_create_sport);
        buttonDelete = view.findViewById(R.id.close_create_sport);
        buttonClose = view.findViewById(R.id.close_create_sport2);
    }

    private void putData() {
        RequestOptions requestOptions = new RequestOptions();
        textViewTitle.setText(defaultNutrition.getName());
        requestOptions.placeholder(R.drawable.icon_gallery);
        Glide.with(this).load(defaultNutrition.getPhoto()).into(imageViewNutrtion);
    }

    private List<PlanNutrition> planNutritions;

    @Override
    public void downloadNutritionPlanFirebase(boolean end) {
        if(end==true){
            hideLoading();
            planNutritions = new ArrayList<>();
            planNutritions = SessionUser.getInstance().firebaseAdmin.allPlanNutrition;
            for(PlanNutrition item : planNutritions){
                if(item.getName().equals(defaultNutrition.getName())){
                    buttonSave.setEnabled(false);
                    buttonDelete.setEnabled(true);

                    int type = item.getType();
                    String strType = null;
                    switch (type){
                        case 1:
                            strType = DESAYUNO;
                            break;
                        case 2:
                            strType = COMIDA;
                            break;
                        case 3:
                            strType = MERIENDA;
                            break;
                        case 4:
                            strType = CENA;
                            break;
                    }

                    String [] arrType = getResources().getStringArray(R.array.nutrition);

                    for(int i=0;i<arrType.length;i++){
                        if(arrType[i].equals(strType)){
                            spinnerType.setSelection(i);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void insertNutritionPlanFirebase(boolean end) {
        if(end){
            buttonSave.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            Toast.makeText(getContext(),defaultNutrition.getName()+" "+getString(R.string.add_create_nutrition),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void deleteNutritionPlanFirebase(boolean end) {
        if(end==true){
            buttonSave.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            Toast.makeText(getContext(),defaultNutrition.getName()+" "+getString(R.string.delete_create_nutrition),Toast.LENGTH_LONG).show();
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
    public void updateNutritionPlanFirebase(boolean end) {}

    @Override
    public void emptyNutritionPlanFirebase(boolean end) {}
}
