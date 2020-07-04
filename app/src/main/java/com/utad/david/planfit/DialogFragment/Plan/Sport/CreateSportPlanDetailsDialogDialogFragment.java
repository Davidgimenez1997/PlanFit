package com.utad.david.planfit.DialogFragment.Plan.Sport;

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
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import io.fabric.sdk.android.Fabric;
import java.util.List;

public class CreateSportPlanDetailsDialogDialogFragment extends BaseDialogFragment
        implements CreateSportPlanDetailsDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private static String SPORT = Constants.SportPlanDetails.EXTRA_SPORT;
    private DefaultSport defaultSport;

    private TextView textViewTitle;
    private ImageView imageViewSport;
    private Spinner spinnerStart;
    private Spinner spinnerEnd;
    private Button buttonSave;
    private Button buttonClose;
    private Button buttonDelete;
    private Callback callback;
    private String timeStart;
    private String timeEnd;
    private CreateSportPlanDetailsDialogPresenter createSportPlanDetailsDialogPresenter;

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

    public static CreateSportPlanDetailsDialogDialogFragment newInstance(DefaultSport defaultSport) {
        CreateSportPlanDetailsDialogDialogFragment fragment = new CreateSportPlanDetailsDialogDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(SPORT, defaultSport);
        fragment.setArguments(args);
        return fragment;
    }

    /******************************** GET ARGUMENTS *************************************+/
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.createSportPlanDetailsDialogPresenter = new CreateSportPlanDetailsDialogPresenter(this);

        if (this.createSportPlanDetailsDialogPresenter.checkInternetDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(),new Crashlytics());
        }

        this.defaultSport = getArguments().getParcelable(SPORT);
        this.createSportPlanDetailsDialogPresenter.setSportFavorite(this.defaultSport);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.create_sport_plan_details_dialog_fragment, container, false);
        view.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.findById(view);
        if (this.createSportPlanDetailsDialogPresenter.checkInternetDevice(getContext())) {
            this.configureSpinnerDiseng();
            this.onClickButtonClose();
            this.putData();
            this.onClickButtonSave();
            this.onClickButtonDelete();
            this.configureSpinnerStart();
            this.configureSpinnerEnd();
        } else {
            this.configureSpinnerDiseng();
            this.onClickButtonClose();
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
        this.imageViewSport = view.findViewById(R.id.imageViewCreateSport);
        this.spinnerStart = view.findViewById(R.id.spinner_comienzo);
        this.spinnerEnd = view.findViewById(R.id.spinner_fin);
        this.buttonSave = view.findViewById(R.id.save_create_sport);
        this.buttonDelete = view.findViewById(R.id.close_create_sport);
        this.buttonClose = view.findViewById(R.id.close_create_sport2);
    }

    private void configureSpinnerDiseng() {
        ArrayAdapter arrayAdapter = this.createSportPlanDetailsDialogPresenter.getSpinnerArrayAdapter(getContext());
        this.spinnerEnd.setAdapter(arrayAdapter);
        this.spinnerStart.setAdapter(arrayAdapter);
    }

    private void putData() {
        this.textViewTitle.setText(this.defaultSport.getName());
        Utils.loadImage(this.defaultSport.getPhoto(), imageViewSport, Utils.PLACEHOLDER_GALLERY);
    }

    /******************************** ONCLICK SPINNER *************************************+/
     *
     */

    private void configureSpinnerStart() {
        this.spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                timeStart = spinnerStart.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void configureSpinnerEnd() {
        this.spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                timeEnd = spinnerEnd.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
            if (this.timeStart.equals(this.timeEnd)) {
                Toast.makeText(getContext(),getString(R.string.info_create_plan),Toast.LENGTH_LONG).show();
            } else {
                if  (this.callback != null) {
                    showLoading();
                    this.createSportPlanDetailsDialogPresenter.createSportPlan(timeStart, timeEnd);
                }
            }
        });
    }

    /******************************** BORRAR PLAN *************************************+/
     *
     */

    private void onClickButtonDelete(){
        this.buttonDelete.setOnClickListener(v -> {
            showLoading();
            this.createSportPlanDetailsDialogPresenter.deletePlan();
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
    public void errorSelectedTimes() {
        Toast.makeText(getContext(),"No puedes terminar antes de empezar",Toast.LENGTH_LONG).show();
    }

    @Override
    public void addSportPlan() {
        Toast.makeText(getContext(),this.defaultSport.getName()+" "+getString(R.string.add_create_sport),Toast.LENGTH_LONG).show();
        this.buttonSave.setEnabled(false);
        this.buttonDelete.setEnabled(true);
        hideLoading();
    }

    @Override
    public void deleteSportPlan() {
        this.buttonSave.setEnabled(true);
        this.buttonDelete.setEnabled(false);
        hideLoading();
        Toast.makeText(getContext(),this.defaultSport.getName()+" "+getString(R.string.delete_create_sport),Toast.LENGTH_LONG).show();
    }

    @Override
    public void getSportPlan(List<PlanSport> planSports) {
        for (PlanSport item : planSports) {
            if (item.getName().equals(this.defaultSport.getName())) {
                this.buttonSave.setEnabled(false);
                this.buttonDelete.setEnabled(true);
                String [] timePlanArr = getResources().getStringArray(R.array.timePlan);
                String timeStart = this.createSportPlanDetailsDialogPresenter.getTimes(item.getTimeStart());
                String timeEnd = this.createSportPlanDetailsDialogPresenter.getTimes(item.getTimeEnd());
                for (int i = 0; i < timePlanArr.length; i++) {
                    if (timePlanArr[i].equals(timeStart)) {
                        this.spinnerStart.setSelection(i);
                    }
                }
                for (int i = 0;i < timePlanArr.length; i++){
                    if (timePlanArr[i].equals(timeEnd)) {
                        this.spinnerEnd.setSelection(i);
                    }
                }
                hideLoading();
            }
        }
    }
}
