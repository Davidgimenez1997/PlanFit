package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport;

import android.content.Context;

import com.utad.david.planfit.Data.Plan.Sport.GetSportPlan;
import com.utad.david.planfit.Data.Plan.Sport.SportPlanRepository;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowSportPlanPresenter implements GetSportPlan {

    private ShowSportPlanView view;

    public ShowSportPlanPresenter(ShowSportPlanView view) {
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

    public void shortArray(ArrayList<PlanSport> list) {
        Collections.sort(list);
    }

    public void checkArraySize(ArrayList<PlanSport> arrSport, List<PlanSport> planSports) {
        if (arrSport.size() == 0) {
            this.updateSportPlan(false, null);
        } else {
            this.updateSportPlan(true, planSports);
        }
    }

    public void updatePlan(PlanSport planSport) {
        SportPlanRepository.getInstance().updatePlanSport(planSport);
    }

    @Override
    public void getSportPlan(boolean status, List<PlanSport> planSports) {
        if (status) {
            this.view.getSportPlan(planSports);
        }
    }

    @Override
    public void emptySportPlan(boolean status) {
        if (status) {
            this.view.emptySportPlan();
        }
    }

    @Override
    public void updateSportPlan(boolean status, List<PlanSport> updateList) {
        if (status) {
            this.view.updateSportPlan(updateList);
        }
    }

    @Override
    public void addSportPlan(boolean status) {}
    @Override
    public void deleteSportPlan(boolean status) {}
}
