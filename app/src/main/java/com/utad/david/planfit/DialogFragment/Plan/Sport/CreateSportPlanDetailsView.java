package com.utad.david.planfit.DialogFragment.Plan.Sport;

import com.utad.david.planfit.Model.Plan.PlanSport;

import java.util.List;

public interface CreateSportPlanDetailsView {
    void deviceOfflineMessage();
    void errorSelectedTimes();
    void deleteSportPlan();
    void addSportPlan();
    void getSportPlan(List<PlanSport> planSports);
}
