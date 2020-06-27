package com.utad.david.planfit.DialogFragment.Plan.Sport;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.utad.david.planfit.Data.Plan.SessionPlan;
import com.utad.david.planfit.Data.Plan.Sport.GetSportPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.math.BigDecimal;
import java.util.List;

public class CreateSportPlanDetailsPresenter implements GetSportPlan {

    private CreateSportPlanDetailsView view;
    private DefaultSport sportFavorite;
    private ArrayAdapter spinnerArrayAdapter;

    public CreateSportPlanDetailsPresenter(CreateSportPlanDetailsView view) {
        this.view = view;
        SportPlanRepository.getInstance().setGetSportPlan(this);
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            SportPlanRepository.getInstance().getSportPlan();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void setSportFavorite(DefaultSport sportFavorite) {
        this.sportFavorite = sportFavorite;
    }

    public ArrayAdapter getSpinnerArrayAdapter(Context context) {
        this.spinnerArrayAdapter = ArrayAdapter.createFromResource(context, R.array.timePlan, R.layout.spinner_item);
        this.spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        return this.spinnerArrayAdapter;
    }

    public void createSportPlan(String timeStart, String timeEnd) {
        double start = this.convertStringToDouble(timeStart);
        double end = this.convertStringToDouble(timeEnd);
        if (start > end) {
            this.view.errorSelectedTimes();
        } else {
            SessionPlan.getInstance().setPlanSport(new PlanSport(this.sportFavorite.getName(), this.sportFavorite.getPhoto(), start, end, Constants.ModePlan.NO));
            SportPlanRepository.getInstance().addSportPlan();
        }
    }

    private double convertStringToDouble(String message){
        String [] parts = message.split(":");
        String first = parts[0];
        String second = parts[1];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(first);
        stringBuilder.append(".");
        stringBuilder.append(second);
        return Double.parseDouble(stringBuilder.toString());
    }

    public String getTimes(double time) {
        String aux = String.valueOf(time);
        BigDecimal bigDecimal = new BigDecimal(aux);
        long longValue = bigDecimal.longValue();
        BigDecimal bigDecimal1 = bigDecimal.remainder(BigDecimal.ONE);
        StringBuilder stringBuilder = new StringBuilder(bigDecimal1.toString());
        stringBuilder.delete(0, 2);
        String result = "0" + Long.valueOf(longValue) + ":" + stringBuilder.toString();
        if (stringBuilder.toString().length() == 1) {
            return result + "0";
        } else {
            return result;
        }
    }

    public void deletePlan() {
        SportPlanRepository.getInstance().deleteSportPlan(sportFavorite.getName());

    }

    @Override
    public void addSportPlan(boolean status) {
        if (status) {
            this.view.addSportPlan();
        }
    }

    @Override
    public void getSportPlan(boolean status, List<PlanSport> planSports) {
        if (status) {
            this.view.getSportPlan(planSports);
        }
    }

    @Override
    public void deleteSportPlan(boolean status) {
        if (status) {
            this.view.deleteSportPlan();
        }
    }

    @Override
    public void emptySportPlan(boolean status) {

    }
    @Override
    public void updateSportPlan(boolean status, List<PlanSport> updateList) {

    }
}
