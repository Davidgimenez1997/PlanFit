package com.utad.david.planfit.Data.Plan;

import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;

public class SessionPlan {

    private static SessionPlan instance = new SessionPlan();

    private PlanSport planSport;
    private PlanNutrition planNutrition;

    private SessionPlan () {
        this.planSport = new PlanSport();
        this.planNutrition = new PlanNutrition();
    }

    public static SessionPlan getInstance() {
        if (instance == null){
            synchronized (SessionPlan.class){
                if (instance == null) {
                    instance = new SessionPlan();
                }
            }
        }
        return instance;
    }

    public void setPlanSport(PlanSport planSport) {
        this.planSport = planSport;
    }

    public PlanSport getPlanSport() {
        return this.planSport;
    }

    public PlanNutrition getPlanNutrition() {
        return planNutrition;
    }

    public void setPlanNutrition(PlanNutrition planNutrition) {
        this.planNutrition = planNutrition;
    }
}
