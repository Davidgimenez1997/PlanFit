package com.utad.david.planfit.Data.Plan.Nutrition;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Data.Nutrition.GetNutrition;
import com.utad.david.planfit.Data.Plan.SessionPlan;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionPlanRepository {

    private static NutritionPlanRepository instance = new NutritionPlanRepository();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private GetNutritionPlan getNutritionPlan;
    private List<PlanNutrition> planNutrition;

    private NutritionPlanRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static NutritionPlanRepository getInstance() {
        if (instance == null){
            synchronized (NutritionPlanRepository.class){
                if (instance == null) {
                    instance = new NutritionPlanRepository();
                }
            }
        }
        return instance;
    }

    public void setGetNutritionPlan(GetNutritionPlan getNutritionPlan) {
        this.getNutritionPlan = getNutritionPlan;
    }

    // Add Nutrition Plan

    public void addNutritionPlan() {
        if(this.getNutritionPlan != null){
            Map<String, Object> planNutrition = new HashMap<>();
            planNutrition.put(Constants.ModelNutritionPlan.NAME, SessionPlan.getInstance().getPlanNutrition().getName());
            planNutrition.put(Constants.ModelNutritionPlan.PHOTO, SessionPlan.getInstance().getPlanNutrition().getPhoto());
            planNutrition.put(Constants.ModelNutritionPlan.TYPE, SessionPlan.getInstance().getPlanNutrition().getType());
            planNutrition.put(Constants.ModelNutritionPlan.IS_OK, SessionPlan.getInstance().getPlanNutrition().getIsOk());
            planNutrition.put(Constants.ModelNutritionPlan.ID, SessionPlan.getInstance().getPlanNutrition().getId());
            String COLLECTION_PLAN_NUTRITION_USER = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(SessionPlan.getInstance().getPlanNutrition().getId())
                    .set(planNutrition)
                    .addOnSuccessListener(aVoid -> this.getNutritionPlan.addNutritionPlan(true))
                    .addOnFailureListener(e -> this.getNutritionPlan.addNutritionPlan(false));
        }
    }

    // Get Nutrition Plan

    public void getNutrtionPlan() {
        if (this.getNutritionPlan != null) {
            String COLLECTION_PLAN_NUTRITION_USER = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionPlan.getNutritiontPlan(false, null);
                }
                List<PlanNutrition> planNutritions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    planNutritions.add(doc.toObject(PlanNutrition.class));
                }
                this.planNutrition = planNutritions;
                if(planNutritions.size()==0){
                    this.getNutritionPlan.emptyNutritionPlan(true);
                }

                if(planNutritions.size()!=0){
                    this.getNutritionPlan.getNutritiontPlan(true, planNutritions);
                }
            });
        }
    }

    // Delete Nutrition Plan

    public void deleteNutritionPlan(String namePlanSport){
        if(this.getNutritionPlan != null){
            String COLLECTION_PLAN_NUTRITION_USER = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER)
                    .whereEqualTo(Constants.ModelNutritionPlan.NAME, namePlanSport)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionPlan.deleteNutritionPlan(true))
                                        .addOnFailureListener(e -> this.getNutritionPlan.deleteNutritionPlan(false));
                            }
                        }
                    });
        }
    }

    // Update Nutrition Plan

    public void updatePlanNutrtion(PlanNutrition planNutrition) {
        if (this.getNutritionPlan != null) {
            String COLLECTION_PLAN_NUTRITION_USER = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(planNutrition.getId());
            Map<String, Object> plan = new HashMap<>();
            plan.put("isOk", planNutrition.getIsOk());
            myUserRef.update(plan)
                    .addOnSuccessListener(aVoid -> this.getNutritionPlan.updateNutritionPlan(true, this.planNutrition))
                    .addOnFailureListener(e -> this.getNutritionPlan.updateNutritionPlan(false, null));
        }
    }
}
