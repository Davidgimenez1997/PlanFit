package com.utad.david.planfit.Data.Favorite.Sport;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Sport.DefaultSport;
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

    /**
     * Get favorite sport list by type
     * @param type for search
     */
    public void getSportFavoriteListByType(String type) {
        if (this.getSportFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(collectionName);
            collectionReference.whereEqualTo(Constants.ModelSportFavorite.TYPE, type)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getSportFavorite.getSportFavoriteListByType(false, null);
                        }
                        List<DefaultSport> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelSportFavorite.TYPE) != null) {
                                list.add(doc.toObject(DefaultSport.class));
                            }
                        }
                        this.getSportFavorite.getSportFavoriteListByType(true, list);
                    });
        }
    }

    /**
     * Get favorite sport list
     */
    public void getSportFavoriteList() {
        if (this.getSportFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getSportFavorite.getSportFavoriteList(false, null);
                }
                List<DefaultSport> defaultSports = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultSports.add(doc.toObject(DefaultSport.class));
                }

                if(defaultSports.size() == 0){
                    this.getSportFavorite.getEmptySportFavoriteList(true);
                }

                if(defaultSports.size() != 0){
                    this.getSportFavorite.getSportFavoriteList(true, defaultSports);
                }
            });
        }

    }

    /**
     * Add favorite sport
     * @param defaultSport item for search
     * @param type search sport
     */
    public void addFavoriteSportByType(DefaultSport defaultSport, String type) {
        if (this.getSportFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelSportFavorite.NAME, defaultSport.getName());
            map.put(Constants.ModelSportFavorite.PHOTO , defaultSport.getPhoto());
            map.put(Constants.ModelSportFavorite.VIDEO , defaultSport.getVideo());
            map.put(Constants.ModelSportFavorite.DESCRIPTION , defaultSport.getDescription());
            map.put(Constants.ModelSportFavorite.TYPE, type);
            this.firebaseFirestore.collection(collectionName).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getSportFavorite.addSportFavorite(true))
                    .addOnFailureListener(e -> this.getSportFavorite.addSportFavorite(false));
        }
    }

    /**
     * Delete favorite sport
     * @param defaultSport item for search
     */
    public void deleteFavoriteSport(DefaultSport defaultSport) {
        if (this.getSportFavorite !=  null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.SPORTS_FAVORITE;
            firebaseFirestore.collection(collectionName)
                    .whereEqualTo(Constants.ModelSportFavorite.NAME, defaultSport.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(collectionName).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getSportFavorite.deleteSportFavorite(true))
                                        .addOnFailureListener(e -> this.getSportFavorite.deleteSportFavorite(false));
                            }
                        }
                    });
        }
    }
}
