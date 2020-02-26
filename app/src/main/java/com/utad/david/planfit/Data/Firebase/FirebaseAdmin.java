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
    public FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener;

    public User userDataFirebase;
    public User userDetails;

    /********COLECCIONES DE FIREBASE********/

    private static String COLLECTION_USER_FIREBASE = "users";

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

    //Insert and download user info and developed info

    public interface FirebaseAdminInsertAndDownloandListener {
        void insertUserDataInFirebase(boolean end);

        void downloadUserDataInFirebase(boolean end);
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

    //Setters

    public void setFirebaseAdminInsertAndDownloandListener(FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener) {
        this.firebaseAdminInsertAndDownloandListener = firebaseAdminInsertAndDownloandListener;
    }

    public void setFirebaseAdminUpdateUserListener(FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener) {
        this.firebaseAdminUpdateAndDeleteUserListener = firebaseAdminUpdateAndDeleteUserListener;
    }

    //Insert user info

    public void addDataUserCouldFirestore() {
        this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
        firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(true))
                .addOnFailureListener(e -> firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(false));
    }

    public void uploadImage(final String image) {
        this.currentUser = this.mAuth.getCurrentUser();
        if(image!=null){
            Uri uri = Uri.parse(image);
                StorageReference ref = storageReference.child("images/" + currentUser.getUid());
                ref.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> SessionUser.getInstance().user.setImgUser(image));
        }
    }

    //Download user info

    public void dowloandDataUserFirebase() {
        this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
        storageReference.child("images/" + currentUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
            userDataFirebase.setImgUser(uri.toString());
            firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(true);
        }).addOnFailureListener(exception -> firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false));
    }

    //Update info user

    public void deletePhoto() {
        this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
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
            this.currentUser = this.mAuth.getCurrentUser();
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
        this.currentUser = this.mAuth.getCurrentUser();
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
}
