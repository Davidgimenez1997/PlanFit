package com.utad.david.planfit.Data.Nutrition;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class NutritionRepository {

    private static NutritionRepository instance = new NutritionRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetNutrition getNutrition;

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

    public void setGetNutrition(GetNutrition getNutrition) {
        this.getNutrition = getNutrition;
    }

    /**
     * Get nutrition list for firebase by type
     * @param type filter nutrition list
     */
    public void getNutritionList(int type) {
        if (this.getNutrition != null) {
            String collectionName  = "";
            switch (type) {
                case Constants.SportNutritionOption.SLIMMING:
                    collectionName = Constants.CollectionsNames.NUTRITION_SLIMMING;
                    break;
                case Constants.SportNutritionOption.TONING:
                    collectionName = Constants.CollectionsNames.NUTRITION_TONING;
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    collectionName = Constants.CollectionsNames.NUTRITION_GAIN_VOLUME;
                    break;
            }
            CollectionReference collectionReference = this.firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutrition.getNutritionList(false, null, -1);
                }
                List<DefaultNutrition> items = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    items.add(doc.toObject(DefaultNutrition.class));
                }
                this.getNutrition.getNutritionList(true, items, type);
            });
        }
    }
}
