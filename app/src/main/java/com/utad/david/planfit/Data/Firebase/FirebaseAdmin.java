package com.utad.david.planfit.Data.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.google.firebase.firestore.*;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.Model.Nutrition.DefaultNutrition;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.Model.Plan.PlanNutrition;
import com.utad.david.planfit.Model.Plan.PlanSport;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAdmin {

    /********USUARIO********/

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseAuth.AuthStateListener authStateListener;
    private AuthCredential credential;

    /********BASE DE DATOS********/

    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    /********INTERFAZES********/

    public FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener;
    public FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener;
    public FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener;
    public FirebaseAdminDownloandFragmentData firebaseAdminDownloandFragmentData;

    public FirebaseAdminFavoriteSport firebaseAdminFavoriteSport;
    public FirebaseAdminFavoriteNutrition firebaseAdminFavoriteNutrition;

    public FirebaseAdminCreateShowPlanSport firebaseAdminCreateShowPlanSport;
    public FirebaseAdminCreateShowPlanNutrition firebaseAdminCreateShowPlanNutrition;

    public FirebaseAdimChatLisetener firebaseAdimChatLisetener;

    public User userDataFirebase;
    public User userDetails;
    public Developer developerInfo;

    /********LISTAS DE DEPORTES********/

    public List<SportSlimming> sportSlimmingListSport;
    public List<SportToning> sportToningListSport;
    public List<SportGainVolume> sportGainVolumeListSport;

    public List<DefaultSport> allSportFavorite;

    /********LISTAS DE ALIMENTOS********/

    public List<NutritionSlimming> nutritionSlimmingListNutrition;
    public List<NutritionToning> nutritionToningsListNutrition;
    public List<NutritionGainVolume> nutritionGainVolumeListNutrition;

    public List<DefaultNutrition> allNutritionFavorite;

    /********LISTAS DE DEPORTES FAVORITOS********/

    public List<SportSlimming> sportSlimmingListSportFavorite;
    public List<SportToning> sportToningListSportFavorite;
    public List<SportGainVolume> sportGainVolumeListSportFavorite;

    /********LISTAS DE ALIMENTOS FAVORITOS********/

    public List<NutritionSlimming> nutritionSlimmingListNutritionFavorite;
    public List<NutritionToning> nutritionToningListNutritionFavorite;
    public List<NutritionGainVolume> nutritionGainVolumeListNutritionFavorite;

    /********LISTAS DE PLANES********/

    public List<PlanSport> allPlanSport;
    public List<PlanNutrition> allPlanNutrition;

    /********COLECCIONES DE FIREBASE********/

    private static String COLLECTION_USER_FIREBASE = "users";
    private static String COLLECTION_MESSAGES_FIREBASE = "messages";
    private static String DOCUMENT_DEVELOPER_INFO_FIREBASE = "david";
    private static String COLLECTION_DEVELOPER_INFO_FIREBASE = "developer_info";
    private static String COLLECTION_SPORT_SLIMMING = "deportes/adelgazar/detalles";
    private static String COLLECTION_SPORT_TONING = "deportes/tonificar/detalles";
    private static String COLLECTION_SPORT_GAIN_VOLUME = "deportes/ganarVolumen/detalles";
    private static String COLLECTION_NUTRITION_SLIMMING = "nutricion/adelgazar/detalles";
    private static String COLLECTION_NUTRITION_TONING = "nutricion/tonificar/detalles";
    private static String COLLECTION_NUTRITION_GAIN_VOLUME = "nutricion/ganarVolumen/detalles";

    private String COLLECTION_FAVORITE_SPORT;
    private String COLLECTION_FAVORITE_NUTRITION;
    private String COLLECTION_PLAN_SPORT_USER;
    private String COLLECTION_PLAN_NUTRITION_USER;


    public FirebaseAdmin() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    /*
    INTERFACES
     */

    //Login y registro

    public interface FirebaseAdminLoginAndRegisterListener {
        void singInWithEmailAndPassword(boolean end);

        void registerWithEmailAndPassword(boolean end);
    }

    //Insert and download user info and developed info

    public interface FirebaseAdminInsertAndDownloandListener {
        void insertUserDataInFirebase(boolean end);

        void downloadUserDataInFirebase(boolean end);

        void downloadInfotDeveloper(boolean end);
    }

    //Update user info and delete

    public interface FirebaseAdminUpdateAndDeleteUserListener {
        void updatePhotoInFirebase(boolean end);

        void deletePhotoInFirebase(boolean end);

        void updateNickNameInFirebase(boolean end);

        void updateFullNameInFirebase(boolean end);

        void deleteUserInFirebase(boolean end);

        //void updateEmailInFirebase(boolean end);

        //void updatePasswordInFirebase(boolean end);

    }

    //Chat

    public interface FirebaseAdimChatLisetener{

        void deleteMessageChat(boolean end);

        void donwloadUserDetails(boolean end,User userDetails);
    }

    //Download sport and nutrition data

    public interface FirebaseAdminDownloandFragmentData {
        void downloandCollectionSportSlimming(boolean end);

        void downloandCollectionSportToning(boolean end);

        void downloandCollectionSportGainVolume(boolean end);

        void downloandCollectionNutritionSlimming(boolean end);

        void downloandCollectionNutritionToning(boolean end);

        void downloandCollectionNutritionGainVolume(boolean end);
    }

    //Insert and download favorite sport and nutrition data

    public interface FirebaseAdminFavoriteSport{

        void inserSportFavoriteFirebase(boolean end);

        void downloandCollectionSportFavorite(boolean end);

        void emptyCollectionSportFavorite(boolean end);

        void deleteFavoriteSport(boolean end);

    }

    public interface FirebaseAdminFavoriteNutrition{
        void inserNutritionFavoriteFirebase(boolean end);

        void downloandCollectionNutritionFavorite(boolean end);

        void emptyCollectionNutritionFavorite(boolean end);

        void deleteFavoriteNutrition(boolean end);
    }

    public interface FirebaseAdminCreateShowPlanSport{
        void insertSportPlanFirebase(boolean end);

        void downloadSportPlanFirebase(boolean end);

        void emptySportPlanFirebase(boolean end);

        void deleteSportPlanFirebase(boolean end);

        void updateSportPlanFirebase(boolean end);
    }

    public interface FirebaseAdminCreateShowPlanNutrition{
        void insertNutritionPlanFirebase(boolean end);

        void downloadNutritionPlanFirebase(boolean end);

        void emptyNutritionPlanFirebase(boolean end);

        void deleteNutritionPlanFirebase(boolean end);

        void updateNutritionPlanFirebase(boolean end);
    }

    //Setters

    public void setFirebaseAdminInsertAndDownloandListener(FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener) {
        this.firebaseAdminInsertAndDownloandListener = firebaseAdminInsertAndDownloandListener;
    }

    public void setFirebaseAdminLoginAndRegisterListener(FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener) {
        this.firebaseAdminLoginAndRegisterListener = firebaseAdminLoginAndRegisterListener;
    }

    public void setFirebaseAdminUpdateUserListener(FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener) {
        this.firebaseAdminUpdateAndDeleteUserListener = firebaseAdminUpdateAndDeleteUserListener;
    }

    public void setFirebaseAdminDownloandFragmentData(FirebaseAdminDownloandFragmentData firebaseAdminDownloandFragmentData) {
        this.firebaseAdminDownloandFragmentData = firebaseAdminDownloandFragmentData;
    }

    public void setFirebaseAdminCreateShowPlanSport(FirebaseAdminCreateShowPlanSport firebaseAdminCreateShowPlanSport) {
        this.firebaseAdminCreateShowPlanSport = firebaseAdminCreateShowPlanSport;
    }

    public void setFirebaseAdminCreateShowPlanNutrition(FirebaseAdminCreateShowPlanNutrition firebaseAdminCreateShowPlanNutrition) {
        this.firebaseAdminCreateShowPlanNutrition = firebaseAdminCreateShowPlanNutrition;
    }

    public void setFirebaseAdminFavoriteSport(FirebaseAdminFavoriteSport firebaseAdminFavoriteSport) {
        this.firebaseAdminFavoriteSport = firebaseAdminFavoriteSport;
    }

    public void setFirebaseAdminFavoriteNutrition(FirebaseAdminFavoriteNutrition firebaseAdminFavoriteNutrition) {
        this.firebaseAdminFavoriteNutrition = firebaseAdminFavoriteNutrition;
    }

    public void setFirebaseAdimChatLisetener(FirebaseAdimChatLisetener firebaseAdimChatLisetener) {
        this.firebaseAdimChatLisetener = firebaseAdimChatLisetener;
    }

    //Login y registro

    public void registerWithEmailAndPassword(String email, String password) {
        if (firebaseAdminLoginAndRegisterListener != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
                            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
                            firebaseAdminLoginAndRegisterListener.registerWithEmailAndPassword(true);
                        } else {
                            firebaseAdminLoginAndRegisterListener.registerWithEmailAndPassword(false);
                        }
                    });
        }

    }

    public void singInWithEmailAndPassword(String email, String password) {
        if (firebaseAdminLoginAndRegisterListener != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
                            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
                            firebaseAdminLoginAndRegisterListener.singInWithEmailAndPassword(true);
                        } else {
                            firebaseAdminLoginAndRegisterListener.singInWithEmailAndPassword(false);
                        }
                    });
        }
    }

    //Insert user info

    public void addDataUserCouldFirestore() {
        if (firebaseAdminInsertAndDownloandListener != null) {
            Map<String, Object> user = new HashMap<>();
            uploadImage(SessionUser.getInstance().user.getImgUser());
            user.put("email", SessionUser.getInstance().user.getEmail());
            user.put("password", SessionUser.getInstance().user.getPassword());
            user.put("fullName", SessionUser.getInstance().user.getFullName());
            user.put("nickName", SessionUser.getInstance().user.getNickName());
            if (SessionUser.getInstance().user.getImgUser() != null) {
                user.put("imgUser", SessionUser.getInstance().user.getImgUser());
            } else {
                user.put("imgUser", "");
            }
            SessionUser.getInstance().user.setUid(mAuth.getUid());
            user.put("uid",mAuth.getUid());
            insertDataUserIntoFirebase(user);
        }
    }

    public void insertDataUserIntoFirebase(Map<String, Object> user) {
        firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(true))
                .addOnFailureListener(e -> firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(false));
    }

    public void uploadImage(final String image) {
        if(image!=null){
            Uri uri = Uri.parse(image);
                StorageReference ref = storageReference.child("images/" + currentUser.getUid());
                ref.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> SessionUser.getInstance().user.setImgUser(image));
        }
    }

    //Download user info

    public void dowloandDataUserFirebase() {
        DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
        if (firebaseAdminInsertAndDownloandListener != null) {
            myUserRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);
                }
                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    userDataFirebase = user;
                    try {
                        userDataFirebase.setPassword(UtilsEncryptDecryptAES.decrypt(user.getPassword()));
                        downloadPhoto();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);
                }
            });
        }
    }

    public void downloadPhoto() {
        storageReference.child("images/" + currentUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
            userDataFirebase.setImgUser(uri.toString());
            firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(true);
        }).addOnFailureListener(exception -> firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false));
    }

    //Delete chat message

    public void deleteMessageInChat(ChatMessage message) {
        if (firebaseAdimChatLisetener != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("").orderByChild("messageTime").equalTo(message.getMessageTime());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot chat : dataSnapshot.getChildren()) {
                            DatabaseReference refDelete = ref.child(chat.getKey());
                            refDelete.removeValue();
                            firebaseAdimChatLisetener.deleteMessageChat(true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    firebaseAdimChatLisetener.deleteMessageChat(false);
                }
            });

        }
    }

    //Download details user

    public void dowloandDetailsUserFirebase(ChatMessage message) {
        DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(message.getMessageUserId());
        if (firebaseAdimChatLisetener != null) {
            myUserRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    firebaseAdimChatLisetener.donwloadUserDetails(false,null);
                }
                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    userDetails = user;
                    try {
                        userDetails.setPassword(UtilsEncryptDecryptAES.decrypt(user.getPassword()));
                        downloadUserPhoto(message.getMessageUserId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    firebaseAdimChatLisetener.donwloadUserDetails(false,null);
                }
            });
        }
    }

    public void downloadUserPhoto(String uid) {
        storageReference.child("images/" + uid).getDownloadUrl().addOnSuccessListener(uri -> {
            userDetails.setImgUser(uri.toString());
            firebaseAdimChatLisetener.donwloadUserDetails(true,userDetails);
        }).addOnFailureListener(exception -> firebaseAdimChatLisetener.donwloadUserDetails(false,null));
    }

    //Update info user

    public void deletePhoto() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());
            desertRef.delete().addOnSuccessListener(aVoid -> {
                DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("imgUser", "");
                myUserRef.update(user)
                        .addOnSuccessListener(aVoid1 -> firebaseAdminUpdateAndDeleteUserListener.deletePhotoInFirebase(true))
                        .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.deletePhotoInFirebase(false));
            }).addOnFailureListener(exception -> firebaseAdminUpdateAndDeleteUserListener.deletePhotoInFirebase(false));
        }
    }

    public void updatePhotoUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());
            desertRef.delete().addOnSuccessListener(aVoid -> {
                uploadImage(userDataFirebase.getImgUser());
                DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("imgUser", userDataFirebase.getImgUser());
                myUserRef.update(user)
                        .addOnSuccessListener(aVoid1 -> firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true))
                        .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(false));
            }).addOnFailureListener(exception -> {
                uploadImage(userDataFirebase.getImgUser());
                firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true);
            });

        }
    }

    public void updateFullNameUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("fullName", userDataFirebase.getFullName());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid -> firebaseAdminUpdateAndDeleteUserListener.updateFullNameInFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updateFullNameInFirebase(false));
        }
    }

    public void updateNickNameUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("nickName", userDataFirebase.getNickName());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid -> firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(false));
        }
    }

    //TODO:NO IMPLEMENTADO (ACTUALIZAR EMAIL Y PASS)

    //Reauthenticate User update email

    public void updateEmailUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            reauthenticateUserUpdateEmail();
        }

    }

    private void reauthenticateUserUpdateEmail(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider
                .getCredential(currentUser.getEmail(), userDataFirebase.getPassword());
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> currentUser.updateEmail(userDataFirebase.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("email", userDataFirebase.getEmail());
                                    //myUserRef.update(user)
                                            //.addOnSuccessListener(aVoid -> firebaseAdminUpdateAndDeleteUserListener.updateEmailInFirebase(true))
                                            //.addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updateEmailInFirebase(false));
                                }
                            }
                        }));
    }

    //Reauthenticate User update password

    public void updatePasswordUserInFirebase(String oldPassword) {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = userDataFirebase.getPassword();
            reauthenticateUserUpdatePassword(oldPassword,newPassword);
        }
    }

    private void reauthenticateUserUpdatePassword(String oldPassword, final String newPassword){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider
                .getCredential(userDataFirebase.getEmail(), oldPassword);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> currentUser.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("password", userDataFirebase.getPassword());
                                    //myUserRef.update(user)
                                            //.addOnSuccessListener(aVoid -> firebaseAdminUpdateAndDeleteUserListener.updatePasswordInFirebase(true))
                                            //.addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updatePasswordInFirebase(false));
                                }
                            }
                        }));
    }

    //Delete user account

    public void deleteAccountInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if(userDataFirebase.getImgUser().equals("")){
                            reauthenticateUserDeleteAccount();
                        }else{
                            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());
                            desertRef.delete()
                                    .addOnSuccessListener(aVoid1 -> reauthenticateUserDeleteAccount())
                                    .addOnFailureListener(exception -> firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(false));
                        }
                    })
                    .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(false));
        }
    }

    //Reauthenticate User delete account

    private void reauthenticateUserDeleteAccount(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider
                .getCredential(userDataFirebase.getEmail(), userDataFirebase.getPassword());
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> currentUser.delete()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(true);
                            }
                        }));
    }

    //Download developer data

    public void dowloandDataDeveloperFirebase() {
        DocumentReference myDeveloperRef = firebaseFirestore.collection(COLLECTION_DEVELOPER_INFO_FIREBASE).document(DOCUMENT_DEVELOPER_INFO_FIREBASE);
        if (firebaseAdminInsertAndDownloandListener != null) {
            myDeveloperRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(false);
                }
                if (snapshot != null && snapshot.exists()) {
                    Developer developerData = snapshot.toObject(Developer.class);
                    developerInfo = developerData;
                    firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(true);
                } else {
                    firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(false);
                }
            });
        }
    }

    //Download Sport data

    public void downloadSlimmingSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_SLIMMING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionSportSlimming(false);
                }
                List<SportSlimming> sportSlimmingList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportSlimmingList.add(doc.toObject(SportSlimming.class));
                }
                sportSlimmingListSport = sportSlimmingList;
                firebaseAdminDownloandFragmentData.downloandCollectionSportSlimming(true);
            });
        }
    }

    public void downloadTiningSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_TONING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionSportToning(false);
                }
                List<SportToning> sportToningList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportToningList.add(doc.toObject(SportToning.class));
                }
                sportToningListSport = sportToningList;
                firebaseAdminDownloandFragmentData.downloandCollectionSportToning(true);
            });
        }
    }

    public void downloadGainVolumeSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_GAIN_VOLUME);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionSportGainVolume(false);
                }
                List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                }
                sportGainVolumeListSport = sportGainVolumes;
                firebaseAdminDownloandFragmentData.downloandCollectionSportGainVolume(true);
            });
        }
    }

    //Download nutrition data

    public void downloadSlimmingNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_SLIMMING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionSlimming(false);
                }
                List<NutritionSlimming> nutritionSlimmingList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionSlimmingList.add(doc.toObject(NutritionSlimming.class));
                }
                nutritionSlimmingListNutrition = nutritionSlimmingList;
                firebaseAdminDownloandFragmentData.downloandCollectionNutritionSlimming(true);
            });
        }
    }

    public void downloadTiningNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_TONING);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionToning(false);
                }
                List<NutritionToning> nutritionTonings = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionTonings.add(doc.toObject(NutritionToning.class));
                }
                nutritionToningsListNutrition = nutritionTonings;
                firebaseAdminDownloandFragmentData.downloandCollectionNutritionToning(true);
            });
        }
    }

    public void downloadGainVolumeNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_GAIN_VOLUME);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionGainVolume(false);
                }
                List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                }
                nutritionGainVolumeListNutrition = nutritionGainVolumes;
                firebaseAdminDownloandFragmentData.downloandCollectionNutritionGainVolume(true);
            });
        }
    }

    //Insert favorite Sport

    public void insertFavoriteSportSlimming(Map<String, Object> slimming) {
        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
        if (firebaseAdminFavoriteSport != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(slimming)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(false));
        }
    }

    public void addFavoriteSportSlimmingCouldFirestore(SportSlimming sportSlimming) {
        if (firebaseAdminFavoriteSport != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportSlimming.getName());
            slimmingMap.put("photo", sportSlimming.getPhoto());
            slimmingMap.put("video", sportSlimming.getVideo());
            slimmingMap.put("description", sportSlimming.getDescription());
            slimmingMap.put("type", "adelgazar");
            insertFavoriteSportSlimming(slimmingMap);
        }
    }

    public void insertFavoriteSportToning(Map<String, Object> toning) {
        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
        if (firebaseAdminFavoriteSport != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(toning)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(false));
        }
    }

    public void addFavoriteSportToningCouldFirestore(SportToning sportToning) {
        if (firebaseAdminFavoriteSport != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportToning.getName());
            slimmingMap.put("photo", sportToning.getPhoto());
            slimmingMap.put("video", sportToning.getVideo());
            slimmingMap.put("description", sportToning.getDescription());
            slimmingMap.put("type", "tonificar");
            insertFavoriteSportToning(slimmingMap);
        }
    }

    public void insertFavoriteSportGainVolume(Map<String, Object> gainVolume) {
        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
        if (firebaseAdminFavoriteSport != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(gainVolume)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteSport.inserSportFavoriteFirebase(false));
        }
    }

    public void addFavoriteSportGainVolumeCouldFirestore(SportGainVolume sportGainVolume) {
        if (firebaseAdminFavoriteSport != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportGainVolume.getName());
            slimmingMap.put("photo", sportGainVolume.getPhoto());
            slimmingMap.put("video", sportGainVolume.getVideo());
            slimmingMap.put("description", sportGainVolume.getDescription());
            slimmingMap.put("type", "ganarVolumen");
            insertFavoriteSportGainVolume(slimmingMap);
        }
    }

    //Delete Favorite Sport

    public void deleteFavoriteSportSlimming(SportSlimming sportSlimming){
        if(firebaseAdminFavoriteSport !=null){
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name",sportSlimming.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.deleteFavoriteSport(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteSport.deleteFavoriteSport(false));
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteSportToning(SportToning sportToning) {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name", sportToning.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.deleteFavoriteSport(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteSport.deleteFavoriteSport(false));
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteSportGainVolume(SportGainVolume sportGainVolume) {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name", sportGainVolume.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.deleteFavoriteSport(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteSport.deleteFavoriteSport(false));
                            }
                        }
                    });
        }
    }

    public void deleteDefaultSportFavorite(DefaultSport defaultSport){
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name", defaultSport.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteSport.deleteFavoriteSport(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteSport.deleteFavoriteSport(false));
                            }
                        }
                    });
        }
    }

    //Insert favorite Nutrition

    public void insertFavoriteNutritionnSlimming(Map<String, Object> slimming) {
        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
        if (firebaseAdminFavoriteNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(slimming)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(false));
        }
    }

    public void addFavoriteNutritionCouldFirestore(NutritionSlimming nutritionSlimming) {
        if (firebaseAdminFavoriteNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionSlimming.getName());
            slimmingMap.put("photo", nutritionSlimming.getPhoto());
            slimmingMap.put("url", nutritionSlimming.getUrl());
            slimmingMap.put("description", nutritionSlimming.getDescription());
            slimmingMap.put("type", "adelgazar");
            insertFavoriteNutritionnSlimming(slimmingMap);
        }
    }

    public void insertFavoriteNutritionToning(Map<String, Object> toning) {
        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
        if (firebaseAdminFavoriteNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(toning)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(false));
        }
    }

    public void addFavoriteNutritionToningCouldFirestore(NutritionToning nutritionToning) {
        if (firebaseAdminFavoriteNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionToning.getName());
            slimmingMap.put("photo", nutritionToning.getPhoto());
            slimmingMap.put("url", nutritionToning.getUrl());
            slimmingMap.put("description", nutritionToning.getDescription());
            slimmingMap.put("type", "tonificar");
            insertFavoriteNutritionToning(slimmingMap);
        }
    }

    public void insertFavoriteNutritionGainVolume(Map<String, Object> gainVolume) {
        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
        if (firebaseAdminFavoriteNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(gainVolume)
                    .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.inserNutritionFavoriteFirebase(false));
        }
    }

    public void addFavoriteNutritionGainVolumeCouldFirestore(NutritionGainVolume nutritionGainVolume) {
        if (firebaseAdminFavoriteNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionGainVolume.getName());
            slimmingMap.put("photo", nutritionGainVolume.getPhoto());
            slimmingMap.put("url", nutritionGainVolume.getUrl());
            slimmingMap.put("description", nutritionGainVolume.getDescription());
            slimmingMap.put("type", "ganarVolumen");
            insertFavoriteNutritionGainVolume(slimmingMap);
        }
    }

    //Delete Favorite Nutrition

    public void deleteFavoriteNutritionSlimming(NutritionSlimming nutritionSlimming){
        if(firebaseAdminFavoriteNutrition !=null){
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name",nutritionSlimming.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(false));
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteNutritionToning(NutritionToning nutritionToning) {
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name", nutritionToning.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(false));
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteNutritionGainVolume(NutritionGainVolume nutritionGainVolume) {
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name", nutritionGainVolume.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(false));
                            }
                        }
                    });
        }
    }

    public void deleteDefaultNutritionFavorite(DefaultNutrition defaultNutrition){
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name", defaultNutrition.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(true))
                                        .addOnFailureListener(e -> firebaseAdminFavoriteNutrition.deleteFavoriteNutrition(false));
                            }
                        }
                    });
        }
    }

    //Download favorite Sport by type

    public void downloadSlimmingSportFavorite() {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "adelgazar")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteSport.downloandCollectionSportFavorite(false);
                        }
                        List<SportSlimming> toningList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                toningList.add(doc.toObject(SportSlimming.class));
                            }
                        }
                        sportSlimmingListSportFavorite = toningList;
                        firebaseAdminFavoriteSport.downloandCollectionSportFavorite(true);
                    });
        }
    }

    public void downloadToningSportFavorite() {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "tonificar")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteSport.downloandCollectionSportFavorite(false);
                        }
                        List<SportToning> sportToningList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                sportToningList.add(doc.toObject(SportToning.class));
                            }
                        }
                        sportToningListSportFavorite = sportToningList;
                        firebaseAdminFavoriteSport.downloandCollectionSportFavorite(true);
                    });
        }
    }

    public void downloadGainVolumeSportFavorite() {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "ganarVolumen")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteSport.downloandCollectionSportFavorite(false);
                        }
                        List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                            }
                        }
                        sportGainVolumeListSportFavorite = sportGainVolumes;
                        firebaseAdminFavoriteSport.downloandCollectionSportFavorite(true);
                    });
        }
    }

    //Download all favorite sport

    public void downloadAllSportFavorite() {
        if (firebaseAdminFavoriteSport != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminFavoriteSport.downloandCollectionSportFavorite(false);
                }
                List<DefaultSport> defaultSports = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultSports.add(doc.toObject(DefaultSport.class));
                }
                allSportFavorite = defaultSports;
                if(allSportFavorite.size()==0){
                    firebaseAdminFavoriteSport.emptyCollectionSportFavorite(true);
                }else if(allSportFavorite.size()!=0){
                    firebaseAdminFavoriteSport.downloandCollectionSportFavorite(true);
                }
            });
        }

    }

    //Download favorite Nutrition

    public void downloadSlimmingNutritionFavorite() {
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "adelgazar")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(false);
                        }
                        List<NutritionSlimming> nutritionSlimmings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                nutritionSlimmings.add(doc.toObject(NutritionSlimming.class));
                            }
                        }
                        nutritionSlimmingListNutritionFavorite = nutritionSlimmings;
                        firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(true);
                    });
        }
    }

    public void downloadToningNutritionFavorite() {
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "tonificar")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(false);
                        }
                        List<NutritionToning> nutritionTonings = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                nutritionTonings.add(doc.toObject(NutritionToning.class));
                            }
                        }
                        nutritionToningListNutritionFavorite = nutritionTonings;
                        firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(true);
                    });
        }
    }

    public void downloadGainVolumeNutritionFavorite() {

        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "ganarVolumen")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(false);
                        }
                        List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("type") != null) {
                                nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                            }
                        }
                        nutritionGainVolumeListNutritionFavorite = nutritionGainVolumes;
                        firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(true);
                    });
        }

    }

    //Donwload all favorite nutrition

    public void downloadAllNutritionFavorite() {
        if (firebaseAdminFavoriteNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(false);
                }
                List<DefaultNutrition> defaultNutritions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    defaultNutritions.add(doc.toObject(DefaultNutrition.class));
                }
                allNutritionFavorite = defaultNutritions;
                if(allNutritionFavorite.size()==0){
                    firebaseAdminFavoriteNutrition.emptyCollectionNutritionFavorite(true);
                }else if(allNutritionFavorite.size()!=0){
                    firebaseAdminFavoriteNutrition.downloandCollectionNutritionFavorite(true);
                }
            });
        }

    }

    //Create sport and nutrtion plan

    public void dataCreateSportPlan(){
        Map<String, Object> planSport = new HashMap<>();
        planSport.put("name", SessionUser.getInstance().planSport.getName());
        planSport.put("photo", SessionUser.getInstance().planSport.getPhoto());
        planSport.put("timeStart", SessionUser.getInstance().planSport.getTimeStart());
        planSport.put("timeEnd", SessionUser.getInstance().planSport.getTimeEnd());
        planSport.put("isOk", SessionUser.getInstance().planSport.getIsOk());
        planSport.put("id",SessionUser.getInstance().planSport.getId());
        insertSportPlan(planSport,SessionUser.getInstance().planSport.getId());
    }

    private void insertSportPlan(final Map<String, Object> planSport,String id) {
        if(firebaseAdminCreateShowPlanSport!=null){
            COLLECTION_PLAN_SPORT_USER = "users/" + currentUser.getUid() + "/planesDeporte";
            firebaseFirestore.collection(COLLECTION_PLAN_SPORT_USER).document(id)
                    .set(planSport)
                    .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanSport.insertSportPlanFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminCreateShowPlanSport.insertSportPlanFirebase(false));
        }
    }

    public void dataCreateNutrtionPlan(){
        Map<String, Object> planNutrition = new HashMap<>();
        planNutrition.put("name", SessionUser.getInstance().planNutrition.getName());
        planNutrition.put("photo", SessionUser.getInstance().planNutrition.getPhoto());
        planNutrition.put("type", SessionUser.getInstance().planNutrition.getType());
        planNutrition.put("isOk", SessionUser.getInstance().planNutrition.getIsOk());
        planNutrition.put("id",SessionUser.getInstance().planNutrition.getId());
        insertNutrtionPlan(planNutrition,SessionUser.getInstance().planNutrition.getId());
    }

    private void insertNutrtionPlan(final Map<String, Object> planNutrition,String id) {
        if(firebaseAdminCreateShowPlanNutrition!=null){
            COLLECTION_PLAN_NUTRITION_USER = "users/" + currentUser.getUid() + "/planesNutricion";
            firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(id)
                    .set(planNutrition)
                    .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanNutrition.insertNutritionPlanFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminCreateShowPlanNutrition.insertNutritionPlanFirebase(false));
        }
    }

    //Download sport and nutrtion plan

    public void downloadAllSportPlanFavorite() {
        if (firebaseAdminCreateShowPlanSport != null) {
            COLLECTION_PLAN_SPORT_USER = "users/" + currentUser.getUid() + "/planesDeporte";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_PLAN_SPORT_USER);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminCreateShowPlanSport.downloadSportPlanFirebase(false);
                }
                List<PlanSport> planSports = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    planSports.add(doc.toObject(PlanSport.class));
                }
                allPlanSport = planSports;
                if(allPlanSport.size()==0){
                    firebaseAdminCreateShowPlanSport.emptySportPlanFirebase(true);
                }else if(allPlanSport.size()!=0){
                    firebaseAdminCreateShowPlanSport.downloadSportPlanFirebase(true);
                }
            });
        }
    }

    public void downloadAllNutrtionPlanFavorite() {
        if (firebaseAdminCreateShowPlanNutrition != null) {
            COLLECTION_PLAN_NUTRITION_USER = "users/" + currentUser.getUid() + "/planesNutricion";
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER);
            collectionReference.addSnapshotListener((queryDocumentSnapshots, e) -> {
                if (e != null) {
                    firebaseAdminCreateShowPlanNutrition.downloadNutritionPlanFirebase(false);
                }
                List<PlanNutrition> planNutritions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    planNutritions.add(doc.toObject(PlanNutrition.class));
                }
                allPlanNutrition = planNutritions;
                if(allPlanNutrition.size()==0){
                    firebaseAdminCreateShowPlanNutrition.emptyNutritionPlanFirebase(true);
                }else if(allPlanNutrition.size()!=0){
                    firebaseAdminCreateShowPlanNutrition.downloadNutritionPlanFirebase(true);
                }
            });
        }
    }

    //Delete One sport and nutrtion Plan

    public void deleteSportPlan(String namePlanSport){
        if(firebaseAdminCreateShowPlanSport!=null){
            COLLECTION_PLAN_SPORT_USER = "users/" + currentUser.getUid() + "/planesDeporte";
                firebaseFirestore.collection(COLLECTION_PLAN_SPORT_USER)
                        .whereEqualTo("name", namePlanSport)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_PLAN_SPORT_USER).document(id)
                                            .delete()
                                            .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanSport.deleteSportPlanFirebase(true))
                                            .addOnFailureListener(e -> firebaseAdminCreateShowPlanSport.deleteSportPlanFirebase(false));
                                }
                            }
                        });
        }
    }

    public void deleteNutritionPlan(String namePlanSport){
        if(firebaseAdminCreateShowPlanNutrition!=null){
            COLLECTION_PLAN_NUTRITION_USER = "users/" + currentUser.getUid() + "/planesNutricion";
            firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER)
                    .whereEqualTo("name", namePlanSport)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String id = documentSnapshot.getId();
                                firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(id)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanNutrition.deleteNutritionPlanFirebase(true))
                                        .addOnFailureListener(e -> firebaseAdminCreateShowPlanNutrition.deleteNutritionPlanFirebase(false));
                            }
                        }
                    });
        }
    }

    //Update plan sport and nutrition

    public void updatePlanSportFirebase(PlanSport planSport) {
        if (firebaseAdminCreateShowPlanSport != null) {
            COLLECTION_PLAN_SPORT_USER = "users/" + currentUser.getUid() + "/planesDeporte";
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_PLAN_SPORT_USER).document(planSport.getId());
            Map<String, Object> plan = new HashMap<>();
            plan.put("isOk", planSport.getIsOk());
            myUserRef.update(plan)
                    .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanSport.updateSportPlanFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminCreateShowPlanSport.updateSportPlanFirebase(false));
        }
    }

    public void updatePlanNutrtionFirebase(PlanNutrition planNutrition) {
        if (firebaseAdminCreateShowPlanNutrition != null) {
            COLLECTION_PLAN_NUTRITION_USER = "users/" + currentUser.getUid() + "/planesNutricion";
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_PLAN_NUTRITION_USER).document(planNutrition.getId());
            Map<String, Object> plan = new HashMap<>();
            plan.put("isOk", planNutrition.getIsOk());
            myUserRef.update(plan)
                    .addOnSuccessListener(aVoid -> firebaseAdminCreateShowPlanNutrition.updateNutritionPlanFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminCreateShowPlanNutrition.updateNutritionPlanFirebase(false));
        }
    }

}
