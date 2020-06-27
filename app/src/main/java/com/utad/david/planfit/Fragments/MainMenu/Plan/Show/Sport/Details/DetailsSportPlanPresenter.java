package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport.Details;

import android.content.Context;

import com.utad.david.planfit.Data.Plan.Sport.GetSportPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.List;

public class DetailsSportPlanPresenter implements GetSportPlan {

    private DetailsSportPlanView view;

    public DetailsSportPlanPresenter(DetailsSportPlanView view) {
        this.view = view;
        SportPlanRepository.getInstance().setGetSportPlan(this);
    }

    public boolean checkInternetInDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            SportPlanRepository.getInstance().getSportPlan();
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    @Override
    public void getSportPlan(boolean status, List<PlanSport> planSports) {
        this.view.getSportPlan();
    }

    @Override
    public void addSportPlan(boolean status) {}
    @Override
    public void emptySportPlan(boolean status) {}
    @Override
    public void deleteSportPlan(boolean status) {}
    @Override
    public void updateSportPlan(boolean status, List<PlanSport> updateList) {}
}
