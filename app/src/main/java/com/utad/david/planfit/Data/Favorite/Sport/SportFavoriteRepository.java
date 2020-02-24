package com.utad.david.planfit.Data.Favorite.Sport;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportFavoriteRepository {

    private static SportFavoriteRepository instance = new SportFavoriteRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetSportFavorite getSportFavorite;
    private FirebaseUser currentUser;
    private List<DefaultSport> allFavoriteSports;

    private SportFavoriteRepository () {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static SportFavoriteRepository getInstance() {
        if (instance == null){
            synchronized (SportFavoriteRepository.class){
                if (instance == null) {
                    instance = new SportFavoriteRepository();
                }
            }
        }
        return instance;
    }

    public void setGetSportFavorite(GetSportFavorite getSportFavorite) {
        this.getSportFavorite = getSportFavorite;
    }

    // Get Favorite Sport By Type

    public void getSlimmingSportFavorite() {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.ADELGAZAR)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getSportFavorite.getSportSlimmingFavorite(false, null);
                        }
                        List<SportSlimming> sportSlimmings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelSportFavorite.TYPE) != null) {
                                sportSlimmings.add(doc.toObject(SportSlimming.class));
                            }
                        }
                        this.getSportFavorite.getSportSlimmingFavorite(true, sportSlimmings);
                    });
        }
    }

    public void getToningSportFavorite() {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.TONIFICAR)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getSportFavorite.getSportToningFavorite(false, null);
                        }
                        List<SportToning> sportToningList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelSportFavorite.TYPE) != null) {
                                sportToningList.add(doc.toObject(SportToning.class));
                            }
                        }
                        this.getSportFavorite.getSportToningFavorite(true, sportToningList);
                    });
        }
    }

    public void getGainVolumeSportFavorite() {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.GANAR_VOLUMEN)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getSportFavorite.getSportGainVolumeFavorite(false, null);
                        }
                        List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelSportFavorite.TYPE) != null) {
                                sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                            }
                        }
                        this.getSportFavorite.getSportGainVolumeFavorite(true, sportGainVolumes);
                    });
        }
    }

    // Get All Favorite Sport

    public void getAllSportFavorite() {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSportFavorite.getSportAllFavorite(false, null);
                }
                List<DefaultSport> defaultSports = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultSports.add(doc.toObject(DefaultSport.class));
                }

                if(defaultSports.size() == 0){
                    this.getSportFavorite.emptySportFavorite(true);
                }

                if(defaultSports.size() != 0){
                    this.allFavoriteSports = defaultSports;
                    this.getSportFavorite.getSportAllFavorite(true, defaultSports);
                }
            });
        }

    }

    // Add Favorite Sport

    public void addFavoriteSportSlimming(SportSlimming sportSlimming) {
        String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
        if (this.getSportFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelSportFavorite.NAME, sportSlimming.getName());
            map.put(Constants.ModelSportFavorite.PHOTO , sportSlimming.getPhoto());
            map.put(Constants.ModelSportFavorite.VIDEO , sportSlimming.getVideo());
            map.put(Constants.ModelSportFavorite.DESCRIPTION , sportSlimming.getDescription());
            map.put(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.ADELGAZAR);
            this.firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getSportFavorite.addSportFavorite(true))
                    .addOnFailureListener(e -> this.getSportFavorite.addSportFavorite(false));
        }
    }

    public void addFavoriteSportToning(SportToning sportToning) {
        String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
        if (this.getSportFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelSportFavorite.NAME , sportToning.getName());
            map.put(Constants.ModelSportFavorite.PHOTO , sportToning.getPhoto());
            map.put(Constants.ModelSportFavorite.VIDEO , sportToning.getVideo());
            map.put(Constants.ModelSportFavorite.DESCRIPTION , sportToning.getDescription());
            map.put(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.TONIFICAR);
            this.firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getSportFavorite.addSportFavorite(true))
                    .addOnFailureListener(e -> this.getSportFavorite.addSportFavorite(false));
        }
    }

    public void addFavoriteSportGainVolume(SportGainVolume sportGainVolume) {
        String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
        if (this.getSportFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelSportFavorite.NAME , sportGainVolume.getName());
            map.put(Constants.ModelSportFavorite.PHOTO , sportGainVolume.getPhoto());
            map.put(Constants.ModelSportFavorite.VIDEO , sportGainVolume.getVideo());
            map.put(Constants.ModelSportFavorite.DESCRIPTION , sportGainVolume.getDescription());
            map.put(Constants.ModelSportFavorite.TYPE, Constants.ModelSportFavorite.GANAR_VOLUMEN);
            this.firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getSportFavorite.addSportFavorite(true))
                    .addOnFailureListener(e -> this.getSportFavorite.addSportFavorite(false));
        }
    }

    // Delete Favorite Sport

    public void deleteFavoriteSportSlimming(SportSlimming sportSlimming){
        if(this.getSportFavorite !=null){
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo(Constants.ModelSportFavorite.NAME,sportSlimming.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportFavorite.deleteSportFavorite(true))
                                        .addOnFailureListener(e -> this.getSportFavorite.deleteSportFavorite(false));
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteSportToning(SportToning sportToning) {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo(Constants.ModelSportFavorite.NAME, sportToning.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportFavorite.deleteSportFavorite(true))
                                        .addOnFailureListener(e -> this.getSportFavorite.deleteSportFavorite(false));
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteSportGainVolume(SportGainVolume sportGainVolume) {
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo(Constants.ModelSportFavorite.NAME, sportGainVolume.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportFavorite.deleteSportFavorite(true))
                                        .addOnFailureListener(e -> this.getSportFavorite.deleteSportFavorite(false));
                            }
                        }
                    });
        }
    }

    public void deleteDefaultSportFavorite(DefaultSport defaultSport){
        if (this.getSportFavorite != null) {
            String COLLECTION_FAVORITE_SPORT = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo(Constants.ModelSportFavorite.NAME, defaultSport.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportFavorite.deleteSportFavorite(true))
                                        .addOnFailureListener(e -> this.getSportFavorite.deleteSportFavorite(false));
                            }
                        }
                    });
        }
    }

    public List<DefaultSport> getAllFavoriteSports() {
        return allFavoriteSports;
    }
}
