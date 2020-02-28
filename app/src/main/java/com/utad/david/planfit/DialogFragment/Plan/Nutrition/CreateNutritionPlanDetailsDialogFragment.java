package com.utad.david.planfit.DialogFragment.Plan.Nutrition;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Data.Plan.Nutrition.GetNutritionPlan;
import com.utad.david.planfit.Data.Plan.Nutrition.NutritionPlanRepository;
import com.utad.david.planfit.Data.Plan.SessionPlan;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.List;

public class CreateNutritionPlanDetailsDialogFragment extends BaseDialogFragment
        implements GetNutritionPlan {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String NUTRITION = Constants.NutritionPlanDetails.EXTRA_NUTRITION;
    private DefaultNutrition defaultNutrition;

    private static String DESAYUNO = Constants.TypesPlanNutrition.DESAYUNO;
    private static String COMIDA = Constants.TypesPlanNutrition.COMIDA;
    private static String MERIENDA = Constants.TypesPlanNutrition.MERIENDA;
    private static String CENA = Constants.TypesPlanNutrition.CENA;

    private TextView textViewTitle;
    private ImageView imageViewNutrtion;
    private Spinner spinnerType;
    private Button buttonSave;
    private Button buttonClose;
    private Button buttonDelete;
    private Callback listener;
    private int type;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    /******************************** NEW INSTANCE *************************************+/
     *
     */

    public static CreateNutritionPlanDetailsDialogFragment newInstance(DefaultNutrition defaultNutrition) {
        CreateNutritionPlanDetailsDialogFragment fragment = new CreateNutritionPlanDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(NUTRITION, defaultNutrition);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            Fabric.with(getContext(),new Crashlytics());
            NutritionPlanRepository.getInstance().setGetNutritionPlan(this);
            NutritionPlanRepository.getInstance().getNutrtionPlan();
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

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View view) {
        textViewTitle = view.findViewById(R.id.textTitleCreateSport);
        imageViewNutrtion = view.findViewById(R.id.imageViewCreateSport);
        spinnerType = view.findViewById(R.id.spinner_comienzo);
        buttonSave = view.findViewById(R.id.save_create_sport);
        buttonDelete = view.findViewById(R.id.close_create_sport);
        buttonClose = view.findViewById(R.id.close_create_sport2);
    }

    private void configureSpinnerDiseng() {
        ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.nutrition, R.layout.spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerType.setAdapter(spinnerArrayAdapter);
    }

    private void putData() {
        textViewTitle.setText(defaultNutrition.getName());
        Utils.loadImage(defaultNutrition.getPhoto(),imageViewNutrtion,Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ONCLICK SPINNER *************************************+/
     *
     */

    private void configureSpinnerType() {
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (spinnerType.getSelectedItem().toString()){
                    case Constants.TypesPlanNutrition.DESAYUNO:
                        type=1;
                        break;
                    case Constants.TypesPlanNutrition.COMIDA:
                        type=2;
                        break;
                    case Constants.TypesPlanNutrition.MERIENDA:
                        type=3;
                        break;
                    case Constants.TypesPlanNutrition.CENA:
                        type=4;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickButtonClose() {
        buttonClose.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClickClose();
            }
        });
    }

    /******************************** GUARDAR PLAN *************************************+/
     *
     */

    private void onClickButtonSave() {
        buttonSave.setOnClickListener(v -> {
            SessionPlan.getInstance().setPlanNutrition(
                    new PlanNutrition(
                            defaultNutrition.getName(),
                            defaultNutrition.getPhoto(),
                            type,
                            Constants.ModePlan.NO
                    ));
            NutritionPlanRepository.getInstance().addNutritionPlan();
        });
    }

    /******************************** BORRAR PLAN *************************************+/
     *
     */

    private void onClickButtonDelete(){
        buttonDelete.setOnClickListener(v -> {
            showLoading();
            NutritionPlanRepository.getInstance().deleteNutritionPlan(defaultNutrition.getName());
        });
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void addNutritionPlan(boolean status) {
        if(status){
            buttonSave.setEnabled(false);
            buttonDelete.setEnabled(true);
            hideLoading();
            Toast.makeText(getContext(),defaultNutrition.getName()+" "+getString(R.string.add_create_nutrition),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getNutritiontPlan(boolean status, List<PlanNutrition> planNutritions) {
        if(status){
            hideLoading();
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
    public void deleteNutritionPlan(boolean status) {
        if(status){
            buttonSave.setEnabled(true);
            buttonDelete.setEnabled(false);
            hideLoading();
            Toast.makeText(getContext(),defaultNutrition.getName()+" "+getString(R.string.delete_create_nutrition),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void emptyNutritionPlan(boolean status) {}
    @Override
    public void updateNutritionPlan(boolean status, List<PlanNutrition> updateList) {}
}
