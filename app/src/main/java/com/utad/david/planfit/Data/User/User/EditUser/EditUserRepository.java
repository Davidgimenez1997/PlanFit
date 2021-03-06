package com.utad.david.planfit.Data.User.User.EditUser;

import android.net.Uri;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class EditUserRepository {

    private static EditUserRepository instance = new EditUserRepository();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private GetEditUser getEditUser;
    private AuthCredential credential;

    private EditUserRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static EditUserRepository getInstance() {
        if (instance == null) {
            synchronized (EditUserRepository.class) {
                if (instance == null) {
                    instance = new EditUserRepository();
                }
            }
        }
        return instance;
    }

    public void setGetEditUser(GetEditUser getEditUser) {
        this.getEditUser = getEditUser;
    }

    /**
     * Delete photo user
     */
    public void deletePhoto() {
        if (this.getEditUser != null) {
            this.currentUser = this.firebaseAuth.getCurrentUser();
            StorageReference userRef = this.storageReference.child(Constants.CollectionsNames.IMAGES + this.currentUser.getUid());
            userRef.delete().addOnSuccessListener(aVoid -> {
                DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put(Constants.ModelUser.IMG, "");
                myUserRef.update(user)
                        .addOnSuccessListener(aVoid1 -> this.getEditUser.deletePhoto(true))
                        .addOnFailureListener(e -> this.getEditUser.deletePhoto(false));
            }).addOnFailureListener(exception -> this.getEditUser.deletePhoto(false));
        }
    }

    /**
     * Update photo user
     */
    public void updatePhoto() {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if (this.getEditUser != null) {
            StorageReference userRef = this.storageReference.child(Constants.CollectionsNames.IMAGES + this.currentUser.getUid());
            userRef.delete().addOnSuccessListener(aVoid -> {
                DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid());
                Map<String, Object> user = new HashMap<>();
                user.put(Constants.ModelUser.IMG, UserRepository.getInstance().getUser().getImgUser());
                myUserRef.update(user)
                        .addOnSuccessListener(aVoid1 ->  {
                            this.updateImage(UserRepository.getInstance().getUser().getImgUser());
                        })
                        .addOnFailureListener(e -> this.getEditUser.updatePhoto(false));
            }).addOnFailureListener(exception -> {
                this.updateImage(UserRepository.getInstance().getUser().getImgUser());
            });

        }
    }

    /**
     * Update  imagen  user
     * @param image update
     */
    private void updateImage(String image) {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if(image!=null){
            Uri uri = Uri.parse(image);
            StorageReference ref = this.storageReference.child(Constants.CollectionsNames.IMAGES + this.currentUser.getUid());
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot ->   {
                        SessionUser.getInstance().getUser().setImgUser(image);
                        this.addPhotoUser();
                    }).addOnFailureListener(e -> {
                        this.getEditUser.updatePhoto(false);
            });
        }
    }

    /**
     * Add photo user
     */
    private void addPhotoUser() {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if (this.getEditUser != null) {
            DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put(Constants.ModelUser.IMG, UserRepository.getInstance().getUser().getImgUser());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid1 ->  {
                        this.updateImage(UserRepository.getInstance().getUser().getImgUser());
                        this.getEditUser.updatePhoto(true);
                    })
                    .addOnFailureListener(e -> this.getEditUser.updatePhoto(false));
        }
    }

    /**
     * Update full name user
     */
    public void updateFullName() {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if (this.getEditUser != null) {
            DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put(Constants.ModelUser.NAME, UserRepository.getInstance().getUser().getFullName());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid -> this.getEditUser.updateFullName(true))
                    .addOnFailureListener(e -> this.getEditUser.updateFullName(false));
        }
    }

    /**
     * Update nick user
     */
    public void updateNickName() {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if (this.getEditUser != null) {
            DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put(Constants.ModelUser.NICK, UserRepository.getInstance().getUser().getNickName());
            myUserRef.update(user)
                    .addOnSuccessListener(aVoid -> this.getEditUser.updateNickName(true))
                    .addOnFailureListener(e -> this.getEditUser.updateNickName(false));
        }
    }

    /**
     * Delete user
     */
    public void deleteAccount() {
        if (this.getEditUser != null) {
            this.currentUser = this.firebaseAuth.getCurrentUser();
            firebaseFirestore.collection(Constants.CollectionsNames.USER).document(this.currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        if (UserRepository.getInstance().getUser().getImgUser().equals("")) {
                            reauthenticateUserDeleteAccount();
                        } else {
                            StorageReference userRef = this.storageReference.child(Constants.CollectionsNames.IMAGES + currentUser.getUid());
                            userRef.delete()
                                    .addOnSuccessListener(aVoid1 -> this.reauthenticateUserDeleteAccount())
                                    .addOnFailureListener(exception -> this.getEditUser.deleteUser(false));
                        }
                    })
                    .addOnFailureListener(e -> this.getEditUser.deleteUser(false));
        }
    }

    /**
     * Reauthenticate user
     */
    private void reauthenticateUserDeleteAccount(){
        this.currentUser = this.firebaseAuth.getCurrentUser();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.credential = EmailAuthProvider
                .getCredential(UserRepository.getInstance().getUser().getEmail(), UserRepository.getInstance().getUser().getPassword());
        this.currentUser.reauthenticate(this.credential)
                .addOnCompleteListener(task -> this.currentUser.delete()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                this.getEditUser.deleteUser(true);
                            }
                        }));
    }

}
