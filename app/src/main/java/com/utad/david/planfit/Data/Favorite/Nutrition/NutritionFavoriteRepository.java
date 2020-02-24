package com.utad.david.planfit.Data.Favorite.Nutrition;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
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
    private List<DefaultNutrition> allFavoriteNutritions;

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

    // Get Favorite Nutrition By Type

    public void getSlimmingNutritionFavorite() {
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.ADELGAZAR)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getNutritionFavorite.getNutritionSlimmingFavorite(false, null);
                        }
                        List<NutritionSlimming> nutritionSlimmings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelNutritionFavorite.TYPE) != null) {
                                nutritionSlimmings.add(doc.toObject(NutritionSlimming.class));
                            }
                        }
                        this.getNutritionFavorite.getNutritionSlimmingFavorite(true, nutritionSlimmings);
                    });
        }
    }

    public void getToningNutritionFavorite() {
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.TONIFICAR)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getNutritionFavorite.getNutritionToningFavorite(false, null);
                        }
                        List<NutritionToning> nutritionTonings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelNutritionFavorite.TYPE) != null) {
                                nutritionTonings.add(doc.toObject(NutritionToning.class));
                            }
                        }
                        this.getNutritionFavorite.getNutritionToningFavorite(true, nutritionTonings);
                    });
        }
    }

    public void getGainVolumeNutritionFavorite() {

        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.GANAR_VOLUMEN)
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            this.getNutritionFavorite.getNutritionGainVolumeFavorite(false, null);
                        }
                        List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get(Constants.ModelNutritionFavorite.TYPE) != null) {
                                nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                            }
                        }
                        this.getNutritionFavorite.getNutritionGainVolumeFavorite(true, nutritionGainVolumes);
                    });
        }

    }

    // Get All Favorite Nutritions

    public void getAllNutritionFavorite() {
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    this.getNutritionFavorite.getNutritionAllFavorite(false, null);
                }
                List<DefaultNutrition> defaultNutritions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultNutritions.add(doc.toObject(DefaultNutrition.class));
                }
                if(defaultNutritions.size() == 0){
                    this.getNutritionFavorite.emptyNutritionFavorite(true);
                }

                if(defaultNutritions.size() != 0){
                    this.allFavoriteNutritions =  defaultNutritions;
                    this.getNutritionFavorite.getNutritionAllFavorite(true, defaultNutritions);
                }
            });
        }

    }

    // Add Favorite Nutrition

    public void addFavoriteNutritionnSlimming(NutritionSlimming nutritionSlimming) {
        String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
        if (this.getNutritionFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelNutritionFavorite.NAME, nutritionSlimming.getName());
            map.put(Constants.ModelNutritionFavorite.PHOTO, nutritionSlimming.getPhoto());
            map.put(Constants.ModelNutritionFavorite.URL, nutritionSlimming.getUrl());
            map.put(Constants.ModelNutritionFavorite.DESCRIPTION, nutritionSlimming.getDescription());
            map.put(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.ADELGAZAR);
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getNutritionFavorite.addNutritionFavorite(true))
                    .addOnFailureListener(e -> this.getNutritionFavorite.addNutritionFavorite(false));
        }
    }

    public void addFavoriteNutritionToning(NutritionToning nutritionToning) {
        String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
        if (this.getNutritionFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelNutritionFavorite.NAME, nutritionToning.getName());
            map.put(Constants.ModelNutritionFavorite.PHOTO, nutritionToning.getPhoto());
            map.put(Constants.ModelNutritionFavorite.URL, nutritionToning.getUrl());
            map.put(Constants.ModelNutritionFavorite.DESCRIPTION, nutritionToning.getDescription());
            map.put(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.TONIFICAR);
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getNutritionFavorite.addNutritionFavorite(true))
                    .addOnFailureListener(e -> this.getNutritionFavorite.addNutritionFavorite(false));
        }
    }

    public void addFavoriteNutritionGainVolume(NutritionGainVolume nutritionGainVolume) {
        String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
        if (this.getNutritionFavorite != null) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.ModelNutritionFavorite.NAME, nutritionGainVolume.getName());
            map.put(Constants.ModelNutritionFavorite.PHOTO, nutritionGainVolume.getPhoto());
            map.put(Constants.ModelNutritionFavorite.URL, nutritionGainVolume.getUrl());
            map.put(Constants.ModelNutritionFavorite.DESCRIPTION, nutritionGainVolume.getDescription());
            map.put(Constants.ModelNutritionFavorite.TYPE, Constants.ModelNutritionFavorite.GANAR_VOLUMEN);
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getNutritionFavorite.addNutritionFavorite(true))
                    .addOnFailureListener(e -> this.getNutritionFavorite.addNutritionFavorite(false));
        }
    }

    // Delete Favorite Nutrition

    public void deleteFavoriteNutritionSlimming(NutritionSlimming nutritionSlimming){
        if(this.getNutritionFavorite !=null){
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo(Constants.ModelNutritionFavorite.NAME,nutritionSlimming.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionFavorite.deleteNutritionFavorite(true))
                                        .addOnFailureListener(e -> this.getNutritionFavorite.deleteNutritionFavorite(false));
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteNutritionToning(NutritionToning nutritionToning) {
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo(Constants.ModelNutritionFavorite.NAME, nutritionToning.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionFavorite.deleteNutritionFavorite(true))
                                        .addOnFailureListener(e -> this.getNutritionFavorite.deleteNutritionFavorite(false));
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteNutritionGainVolume(NutritionGainVolume nutritionGainVolume) {
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo(Constants.ModelNutritionFavorite.NAME, nutritionGainVolume.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionFavorite.deleteNutritionFavorite(true))
                                        .addOnFailureListener(e -> this.getNutritionFavorite.deleteNutritionFavorite(false));
                            }
                        }
                    });
        }
    }

    public void deleteDefaultNutritionFavorite(DefaultNutrition defaultNutrition){
        if (this.getNutritionFavorite != null) {
            String COLLECTION_FAVORITE_NUTRITION = Constants.CollectionsNames.USERS + currentUser.getUid() + Constants.CollectionsNames.NUTRITION_FAVORITE;
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo(Constants.ModelNutritionFavorite.NAME, defaultNutrition.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> this.getNutritionFavorite.deleteNutritionFavorite(true))
                                        .addOnFailureListener(e -> this.getNutritionFavorite.deleteNutritionFavorite(false));
                            }
                        }
                    });
        }
    }

    public List<DefaultNutrition> getAllFavoriteNutritions() {
        return allFavoriteNutritions;
    }
}
