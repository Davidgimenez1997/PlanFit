package com.utad.david.planfit.Data.Plan.Nutrition;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Data.Plan.SessionPlan;
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

    /**
     * Get nutrition plan for user
     * If get data call to getNutritionPlan
     * If empty list call to emptyNutritionPlan
     */
    public void getNutritionPlan() {
        if (this.getNutritionPlan != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            CollectionReference collectionReference = this.firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionPlan.getNutritiontPlan(false, null);
                }
                List<PlanNutrition> planNutritions = new ArrayList<>();
                this.planNutrition = planNutritions;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    planNutritions.add(doc.toObject(PlanNutrition.class));
                }
                if (planNutritions.size() == 0) {
                    this.getNutritionPlan.emptyNutritionPlan(true);
                }

                if (planNutritions.size() != 0) {
                    this.getNutritionPlan.getNutritiontPlan(true, planNutritions);
                }
            });
        }
    }

    /**
     * Add  nutrition favorite in plan
     * Use SessionPlan for get nutrition plan
     */
    public void addNutritionPlan() {
        if (this.getNutritionPlan != null) {
            Map<String, Object> planNutrition = new HashMap<>();
            PlanNutrition item = SessionPlan.getInstance().getPlanNutrition();
            planNutrition.put(Constants.ModelNutritionPlan.NAME, item.getName());
            planNutrition.put(Constants.ModelNutritionPlan.PHOTO, item.getPhoto());
            planNutrition.put(Constants.ModelNutritionPlan.TYPE, item.getType());
            planNutrition.put(Constants.ModelNutritionPlan.IS_OK, item.getIsOk());
            planNutrition.put(Constants.ModelNutritionPlan.ID, item.getId());
            String collectioName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            this.firebaseFirestore.collection(collectioName).document(SessionPlan.getInstance().getPlanNutrition().getId())
                    .set(planNutrition)
                    .addOnSuccessListener(aVoid -> this.getNutritionPlan.addNutritionPlan(true))
                    .addOnFailureListener(e -> this.getNutritionPlan.addNutritionPlan(false));
        }
    }

    /**
     * Delete nutrition plan
     * @param namePlanNutrition for delete sport plan
     */
    public void deleteNutritionPlan(String namePlanNutrition) {
        if (this.getNutritionPlan != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            this.firebaseFirestore.collection(collectionName)
                    .whereEqualTo(Constants.ModelNutritionPlan.NAME, namePlanNutrition)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                this.firebaseFirestore.collection(collectionName).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionPlan.deleteNutritionPlan(true))
                                        .addOnFailureListener(e -> this.getNutritionPlan.deleteNutritionPlan(false));
                            }
                        }
                    });
        }
    }

    /**
     * Update nutrition plan
     * @param planNutrition update item
     */
    public void updatePlanNutrition(PlanNutrition planNutrition) {
        if (this.getNutritionPlan != null) {
            String collectioName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_PLAN;
            DocumentReference myUserRef = this.firebaseFirestore.collection(collectioName).document(planNutrition.getId());
            Map<String, Object> plan = new HashMap<>();
            plan.put(Constants.ModelNutritionPlan.IS_OK, planNutrition.getIsOk());
            myUserRef.update(plan)
                    .addOnSuccessListener(aVoid -> this.getNutritionPlan.updateNutritionPlan(true, this.planNutrition))
                    .addOnFailureListener(e -> this.getNutritionPlan.updateNutritionPlan(false, null));
        }
    }
}
