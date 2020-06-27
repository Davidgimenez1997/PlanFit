package com.utad.david.planfit.Data.Plan.Sport;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Data.Plan.SessionPlan;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportPlanRepository {

    private static SportPlanRepository instance = new SportPlanRepository();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private GetSportPlan getSportPlan;
    private List<PlanSport> planSports;

    private SportPlanRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static SportPlanRepository getInstance() {
        if (instance == null){
            synchronized (SportPlanRepository.class){
                if (instance == null) {
                    instance = new SportPlanRepository();
                }
            }
        }
        return instance;
    }

    public void setGetSportPlan(GetSportPlan getSportPlan) {
        this.getSportPlan = getSportPlan;
    }

    /**
     * Get sport plan for user
     * If get data call to getSportPlan
     * If empty list call to emptySportPlan
     */
    public void getSportPlan() {
        if (this.getSportPlan != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_PLAN;
            CollectionReference collectionReference = firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSportPlan.getSportPlan(false, null);
                }
                List<PlanSport> planSports = new ArrayList<>();
                this.planSports = planSports;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    planSports.add(doc.toObject(PlanSport.class));
                }
                if (planSports.size() == 0) {
                    this.getSportPlan.emptySportPlan(true);
                }

                if (planSports.size() != 0) {
                    this.getSportPlan.getSportPlan(true, planSports);
                }
            });
        }
    }

    /**
     * Add  sport favorite in plan
     * Use SessionPlan for get sport plan
     */
    public void addSportPlan() {
        if (this.getSportPlan != null) {
            Map<String, Object> planSport = new HashMap<>();
            PlanSport item = SessionPlan.getInstance().getPlanSport();
            planSport.put(Constants.ModelSportPlan.NAME, item.getName());
            planSport.put(Constants.ModelSportPlan.PHOTO, item.getPhoto());
            planSport.put(Constants.ModelSportPlan.TIME_START, item.getTimeStart());
            planSport.put(Constants.ModelSportPlan.TIME_END, item.getTimeEnd());
            planSport.put(Constants.ModelSportPlan.IS_OK, item.getIsOk());
            planSport.put(Constants.ModelSportPlan.ID, item.getId());
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_PLAN;
            firebaseFirestore.collection(collectionName).document(SessionPlan.getInstance().getPlanSport().getId())
                    .set(planSport)
                    .addOnSuccessListener(aVoid -> this.getSportPlan.addSportPlan(true))
                    .addOnFailureListener(e -> this.getSportPlan.addSportPlan(false));
        }
    }

    /**
     * Delete sport plan
     * @param namePlanSport for delete sport plan
     */
    public void deleteSportPlan(String namePlanSport){
        if (this.getSportPlan != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_PLAN;
            firebaseFirestore.collection(collectionName)
                    .whereEqualTo(Constants.ModelSportPlan.NAME, namePlanSport)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(collectionName).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportPlan.deleteSportPlan(true))
                                        .addOnFailureListener(e -> this.getSportPlan.deleteSportPlan(false));
                            }
                        }
                    });
        }
    }

    /**
     * Update sport plan
     * @param planSport update item
     */
    public void updatePlanSport(PlanSport planSport) {
        if (this.getSportPlan != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_PLAN;
            DocumentReference myUserRef = firebaseFirestore.collection(collectionName).document(planSport.getId());
            Map<String, Object> plan = new HashMap<>();
            plan.put(Constants.ModelSportPlan.IS_OK, planSport.getIsOk());
            myUserRef.update(plan)
                    .addOnSuccessListener(aVoid -> this.getSportPlan.updateSportPlan(true, this.planSports))
                    .addOnFailureListener(e -> this.getSportPlan.updateSportPlan(false, null));
        }
    }
}
