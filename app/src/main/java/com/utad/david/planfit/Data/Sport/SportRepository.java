package com.utad.david.planfit.Data.Sport;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
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

    // Get Sport Data

    public void getSlimmingSport() {
        if (this.getSport != null) {
            String COLLECTION_SPORT_SLIMMING = Constants.CollectionsNames.SPORT_SLIMMING;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_SLIMMING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSport.getSlimmingSports(false, null);
                }
                List<SportSlimming> sportSlimmingList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportSlimmingList.add(doc.toObject(SportSlimming.class));
                }
                this.getSport.getSlimmingSports(true, sportSlimmingList);
            });
        }
    }

    public void getToningSport() {
        if (this.getSport != null) {
            String COLLECTION_SPORT_TONING = Constants.CollectionsNames.SPORT_TONING;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_TONING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSport.getToningSports(false, null);
                }
                List<SportToning> sportToningList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportToningList.add(doc.toObject(SportToning.class));
                }
                this.getSport.getToningSports(true, sportToningList);
            });
        }
    }

    public void getGainVolumeSport() {
        if (this.getSport != null) {
            String COLLECTION_SPORT_GAIN_VOLUME = Constants.CollectionsNames.SPORT_GAIN_VOLUME;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_GAIN_VOLUME);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSport.getGainVolumeSports(false, null);
                }
                List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                }
                this.getSport.getGainVolumeSports(true, sportGainVolumes);
            });
        }
    }

}
