package com.utad.david.planfit.Data.Firebase;

import android.net.Uri;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User.User;

import java.util.HashMap;
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

    public FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener;

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

    //Update user info and delete

    public interface FirebaseAdminUpdateAndDeleteUserListener {
        void updatePhotoInFirebase(boolean end);

        void deletePhotoInFirebase(boolean end);

        void updateNickNameInFirebase(boolean end);

        void updateFullNameInFirebase(boolean end);

        void deleteUserInFirebase(boolean end);

    }

    public void setFirebaseAdminUpdateUserListener(FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener) {
        this.firebaseAdminUpdateAndDeleteUserListener = firebaseAdminUpdateAndDeleteUserListener;
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
                addImage(UserRepository.getInstance().getUser().getImgUser());
                DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put("imgUser", UserRepository.getInstance().getUser().getImgUser());
                myUserRef.update(user)
                        .addOnSuccessListener(aVoid1 -> firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true))
                        .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(false));
            }).addOnFailureListener(exception -> {
                addImage(UserRepository.getInstance().getUser().getImgUser());
                firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true);
            });

        }
    }

    private void addImage(String image) {
        this.currentUser = this.mAuth.getCurrentUser();
        if(image!=null){
            Uri uri = Uri.parse(image);
            StorageReference ref = storageReference.child(Constants.CollectionsNames.IMAGES + currentUser.getUid());
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> SessionUser.getInstance().getUser().setImgUser(image));
        }
    }

    public void updateFullNameUserInFirebase() {
        this.currentUser = this.mAuth.getCurrentUser();
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("fullName", UserRepository.getInstance().getUser().getFullName());
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
            user.put("nickName", UserRepository.getInstance().getUser().getNickName());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid -> firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(true))
                    .addOnFailureListener(e -> firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(false));
        }
    }

    //Delete user account

    public void deleteAccountInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            this.currentUser = this.mAuth.getCurrentUser();
            firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if(UserRepository.getInstance().getUser().getImgUser().equals("")){
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
                .getCredential(UserRepository.getInstance().getUser().getEmail(), UserRepository.getInstance().getUser().getPassword());
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> currentUser.delete()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(true);
                            }
                        }));
    }
}
