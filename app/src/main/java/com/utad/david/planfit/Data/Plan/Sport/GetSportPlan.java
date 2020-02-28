package com.utad.david.planfit.Data.Plan.Sport;

import com.utad.david.planfit.Model.Plan.PlanSport;

import java.util.List;

public interface GetSportPlan {
    void addSportPlan(boolean status);
    void getSportPlan(boolean status, List<PlanSport> planSports);
    void emptySportPlan(boolean status);
    void deleteSportPlan(boolean status);
    void updateSportPlan(boolean status, List<PlanSport> updateList);
}
