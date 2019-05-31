package com.utad.david.planfit.Data;

import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Model.User;

public class SessionUser {

    private static SessionUser instance = new SessionUser();

    public FirebaseAdmin firebaseAdmin;
    public User user;
    public PlanSport planSport;
    public PlanNutrition planNutrition;

    private SessionUser() {
        firebaseAdmin = new FirebaseAdmin();
        user = new User();
        planSport = new PlanSport();
        planNutrition = new PlanNutrition();
    }

    public static SessionUser getInstance() {
        if (instance == null){
            synchronized (SessionUser.class){
                if (instance == null) {
                    instance = new SessionUser();
                }
            }
        }
        return instance;
    }

    public void removeUser(){
        user.setEmail(null);
        user.setPassword(null);
        user.setImgUser(null);
        user.setNickName(null);
        user.setFullName(null);
    }
}
