package com.utad.david.planfit.Data.Sport;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class SportRepository {

    private static SportRepository instance = new SportRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetSport getSport;

    private SportRepository () {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static SportRepository getInstance() {
        if (instance == null){
            synchronized (SportRepository.class){
                if (instance == null) {
                    instance = new SportRepository();
                }
            }
        }
        return instance;
    }

    public void setGetSport(GetSport getSport) {
        this.getSport = getSport;
    }

    /**
     * Get sport list for firebase by type
     * @param type filter sport list
     */
    public void getSportList(int type) {
        if (this.getSport != null) {
            String collectionName  = "";
            switch (type) {
                case Constants.SportNutritionOption.SLIMMING:
                    collectionName = Constants.CollectionsNames.SPORT_SLIMMING;
                    break;
                case Constants.SportNutritionOption.TONING:
                    collectionName = Constants.CollectionsNames.SPORT_TONING;
                    break;
                case Constants.SportNutritionOption.GAIN_VOLUMEN:
                    collectionName = Constants.CollectionsNames.SPORT_GAIN_VOLUME;
                    break;
            }
            CollectionReference collectionReference = this.firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSport.getSportList(false, null, -1);
                }
                List<DefaultSport> items = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    items.add(doc.toObject(DefaultSport.class));
                }
                this.getSport.getSportList(true, items, type);
            });
        }
    }
}
