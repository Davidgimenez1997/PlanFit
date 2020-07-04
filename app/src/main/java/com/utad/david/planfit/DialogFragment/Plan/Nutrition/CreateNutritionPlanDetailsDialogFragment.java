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
        implements CreateNutritionPlanDetailsDialogView {

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
    private Callback callback;
    private int type;
    private CreateNutritionPlanDetailsDialogPresenter createNutritionPlanDetailsDialogPresenter;


    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void onClickClose();
    }

    public void setListener(Callback callback) {
        this.callback = callback;
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

        this.createNutritionPlanDetailsDialogPresenter = new CreateNutritionPlanDetailsDialogPresenter(this);

        if (this.createNutritionPlanDetailsDialogPresenter.checkInternetDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(),new Crashlytics());
        }

        this.defaultNutrition = getArguments().getParcelable(NUTRITION);
        this.createNutritionPlanDetailsDialogPresenter.setNutritionFavorite(this.defaultNutrition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_nutrition_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (this.createNutritionPlanDetailsDialogPresenter.checkInternetDevice(getContext())) {
            this.findById(view);
            this.configureSpinnerDiseng();
            this.putData();
            this.onClickButtonClose();
            this.onClickButtonSave();
            this.onClickButtonDelete();
            this.configureSpinnerType();
        } else {
            this.findById(view);
            this.configureSpinnerDiseng();
            onClickButtonClose();
            this.putData();
            this.buttonSave.setEnabled(false);
            this.buttonDelete.setEnabled(false);
        }

        return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View view) {
        this.textViewTitle = view.findViewById(R.id.textTitleCreateSport);
        this.imageViewNutrtion = view.findViewById(R.id.imageViewCreateSport);
        this.spinnerType = view.findViewById(R.id.spinner_comienzo);
        this.buttonSave = view.findViewById(R.id.save_create_sport);
        this.buttonDelete = view.findViewById(R.id.close_create_sport);
        this.buttonClose = view.findViewById(R.id.close_create_sport2);
    }

    private void configureSpinnerDiseng() {
        ArrayAdapter arrayAdapter = this.createNutritionPlanDetailsDialogPresenter.getSpinnerArrayAdapter(getContext());
        this.spinnerType.setAdapter(arrayAdapter);
    }

    private void putData() {
        this.textViewTitle.setText(this.defaultNutrition.getName());
        Utils.loadImage(this.defaultNutrition.getPhoto(), this.imageViewNutrtion, Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ONCLICK SPINNER *************************************+/
     *
     */

    private void configureSpinnerType() {
        this.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                type = createNutritionPlanDetailsDialogPresenter.setType(spinnerType.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void onClickButtonClose() {
        this.buttonClose.setOnClickListener(v -> {
            if (this.callback != null) {
                this.callback.onClickClose();
            }
        });
    }

    /******************************** GUARDAR PLAN *************************************+/
     *
     */

    private void onClickButtonSave() {
        this.buttonSave.setOnClickListener(v -> {
            showLoading();
            this.createNutritionPlanDetailsDialogPresenter.createNutritionPlan(this.type);
        });
    }

    /******************************** BORRAR PLAN *************************************+/
     *
     */

    private void onClickButtonDelete(){
        this.buttonDelete.setOnClickListener(v -> {
            showLoading();
            this.createNutritionPlanDetailsDialogPresenter.deletePlan();
        });
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        hideLoading();
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void deletetNutritionPlan() {
        this.buttonSave.setEnabled(true);
        this.buttonDelete.setEnabled(false);
        hideLoading();
        Toast.makeText(getContext(),this.defaultNutrition.getName()+" "+getString(R.string.delete_create_nutrition),Toast.LENGTH_LONG).show();
    }

    @Override
    public void addNutritionPlan() {
        this.buttonSave.setEnabled(false);
        this.buttonDelete.setEnabled(true);
        hideLoading();
        Toast.makeText(getContext(),this.defaultNutrition.getName()+" "+getString(R.string.add_create_nutrition),Toast.LENGTH_LONG).show();
    }

    @Override
    public void getNutritionPlan(List<PlanNutrition> planNutrition) {
        hideLoading();
        for(PlanNutrition item : planNutrition) {
            if (item.getName().equals(this.defaultNutrition.getName())) {
                this.buttonSave.setEnabled(false);
                this.buttonDelete.setEnabled(true);
                int type = item.getType();
                String strType = null;
                switch (type) {
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
                String[] arrType = getResources().getStringArray(R.array.nutrition);
                for (int i = 0; i < arrType.length; i++) {
                    if (arrType[i].equals(strType)) {
                        this.spinnerType.setSelection(i);
                    }
                }
            }
        }
    }
}
