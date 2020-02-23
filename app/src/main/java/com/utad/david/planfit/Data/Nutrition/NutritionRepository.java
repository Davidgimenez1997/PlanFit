package com.utad.david.planfit.Data.Nutrition;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class NutritionRepository {

    private static NutritionRepository instance = new NutritionRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetNutritionData getNutritionData;

    private NutritionRepository () {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static NutritionRepository getInstance() {
        if (instance == null){
            synchronized (NutritionRepository.class){
                if (instance == null) {
                    instance = new NutritionRepository();
                }
            }
        }
        return instance;
    }

    public void setGetNutritionData(GetNutritionData getNutritionData) {
        this.getNutritionData = getNutritionData;
    }

    // Get Nutrition data

    public void getSlimmingNutrition() {
        if (this.getNutritionData != null) {
            String COLLECTION_NUTRITION_SLIMMING = Constants.CollectionsNames.NUTRITION_SLIMMING;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_SLIMMING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionData.getSlimmingNutritions(false, null);
                }
                List<NutritionSlimming> nutritionSlimmingList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionSlimmingList.add(doc.toObject(NutritionSlimming.class));
                }
                this.getNutritionData.getSlimmingNutritions(true, nutritionSlimmingList);
            });
        }
    }

    public void getToningNutrition() {
        if (this.getNutritionData != null) {
            String COLLECTION_NUTRITION_TONING = Constants.CollectionsNames.NUTRITION_TONING;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_TONING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionData.getToningNutritions(false, null);
                }
                List<NutritionToning> nutritionTonings = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionTonings.add(doc.toObject(NutritionToning.class));
                }
                this.getNutritionData.getToningNutritions(true, nutritionTonings);
            });
        }
    }

    public void getGainVolumeNutrition() {
        if (this.getNutritionData != null) {
            String COLLECTION_NUTRITION_GAIN_VOLUME = Constants.CollectionsNames.NUTRITION_GAIN_VOLUME;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_GAIN_VOLUME);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionData.getGainVolumeNutritions(false, null);
                }
                List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                }
                this.getNutritionData.getGainVolumeNutritions(true, nutritionGainVolumes);
            });
        }
    }
}
