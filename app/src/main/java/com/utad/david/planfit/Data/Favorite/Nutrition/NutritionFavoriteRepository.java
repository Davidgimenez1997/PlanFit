package com.utad.david.planfit.Data.Favorite.Nutrition;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutritionFavoriteRepository {

    private static NutritionFavoriteRepository instance = new NutritionFavoriteRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetNutritionFavorite getNutritionFavorite;
    private FirebaseUser currentUser;

    private NutritionFavoriteRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static NutritionFavoriteRepository getInstance() {
        if (instance == null){
            synchronized (NutritionFavoriteRepository.class){
                if (instance == null) {
                    instance = new NutritionFavoriteRepository();
                }
            }
        }
        return instance;
    }

    public void setGetNutritionFavorite(GetNutritionFavorite getNutritionFavorite) {
        this.getNutritionFavorite = getNutritionFavorite;
    }

    /**
     * Get favorite nutrition list by type
     * @param type for search
     */
    public void getNutritionFavoriteListByType(String type) {
        if (this.getNutritionFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = this.firebaseFirestore.collection(collectionName);
            collectionReference.whereEqualTo(Constants.ModelNutritionFavorite.TYPE, type)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getNutritionFavorite.getNutritionFavoriteListByType(false, null);
                        }
                        List<DefaultNutrition> items = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelNutritionFavorite.TYPE) != null) {
                                items.add(doc.toObject(DefaultNutrition.class));
                            }
                        }
                        this.getNutritionFavorite.getNutritionFavoriteListByType(true, items);
                    });

        }
    }

    /**
     * Get favorite nutrition list
     */
    public void getNutritionFavoriteList() {
        if (this.getNutritionFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = this.firebaseFirestore.collection(collectionName);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionFavorite.getNutritionFavoriteList(false, null);
                }
                List<DefaultNutrition> defaultNutritions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultNutritions.add(doc.toObject(DefaultNutrition.class));
                }
                if(defaultNutritions.size() == 0){
                    this.getNutritionFavorite.getEmptyNutritionFavorite(true);
                }

                if(defaultNutritions.size() != 0){
                    this.getNutritionFavorite.getNutritionFavoriteList(true, defaultNutritions);
                }
            });
        }
    }

    /**
     * Add favorite nutrition
     * @param defaultNutrition item for search
     * @param type search nutrition
     */
    public void addFavoriteNutritionByType(DefaultNutrition defaultNutrition, String type) {
        if (this.getNutritionFavorite != null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelNutritionFavorite.NAME, defaultNutrition.getName());
            map.put(Constants.ModelNutritionFavorite.PHOTO, defaultNutrition.getPhoto());
            map.put(Constants.ModelNutritionFavorite.URL, defaultNutrition.getUrl());
            map.put(Constants.ModelNutritionFavorite.DESCRIPTION, defaultNutrition.getDescription());
            map.put(Constants.ModelNutritionFavorite.TYPE, type);
            this.firebaseFirestore.collection(collectionName).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getNutritionFavorite.addNutritionFavorite(true))
                    .addOnFailureListener(e -> this.getNutritionFavorite.addNutritionFavorite(false));
        }
    }

    /**
     * Delete favorite nutrition
     * @param defaultNutrition item for search
     */
    public void deleteFavoriteNutrition(DefaultNutrition defaultNutrition) {
        if (this.getNutritionFavorite !=  null) {
            String collectionName = Constants.CollectionsNames.USERS + this.currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            this.firebaseFirestore.collection(collectionName)
                    .whereEqualTo(Constants.ModelNutritionFavorite.NAME, defaultNutrition.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                this.firebaseFirestore.collection(collectionName).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionFavorite.deleteNutritionFavorite(true))
                                        .addOnFailureListener(e -> this.getNutritionFavorite.deleteNutritionFavorite(false));
                            }
                        }
                    });
        }
    }
}
