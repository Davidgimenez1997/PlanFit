package com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport;

import com.utad.david.planfit.Model.Plan.PlanSport;
import java.util.List;

public interface ShowSportPlanView {
    void deviceOfflineMessage();
    void getSportPlan(List<PlanSport> planSports);
    void updateSportPlan(List<PlanSport> updateList);
    void emptySportPlan();
}
