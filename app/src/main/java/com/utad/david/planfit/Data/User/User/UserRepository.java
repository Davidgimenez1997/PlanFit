package com.utad.david.planfit.Data.User.User;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static UserRepository instance = new UserRepository();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private User user;
    private GetUser getUser;

    private UserRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
    }

    public static UserRepository getInstance() {
        if (instance == null){
            synchronized (UserRepository.class){
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    public void setGetUser(GetUser getUser) {
        this.getUser = getUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    // Add User Info

    public void addUserData() {
        if (this.getUser != null) {
            this.currentUser = this.firebaseAuth.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            this.addImage(SessionUser.getInstance().getUser().getImgUser());
            map.put(Constants.ModelUser.EMAIL, SessionUser.getInstance().getUser().getEmail());
            map.put(Constants.ModelUser.PASSWORD, SessionUser.getInstance().getUser().getPassword());
            map.put(Constants.ModelUser.NAME, SessionUser.getInstance().getUser().getFullName());
            map.put(Constants.ModelUser.NICK, SessionUser.getInstance().getUser().getNickName());
            if (SessionUser.getInstance().checkImg()) {
                map.put(Constants.ModelUser.IMG, SessionUser.getInstance().getUser().getImgUser());
            } else {
                map.put(Constants.ModelUser.IMG, "");
            }
            SessionUser.getInstance().setUidUser(this.firebaseAuth.getUid());
            map.put(Constants.ModelUser.UID,SessionUser.getInstance().getUser().getUid());
            firebaseFirestore.collection(Constants.CollectionsNames.USER).document(currentUser.getUid())
                    .set(map)
                    .addOnSuccessListener(aVoid -> this.getUser.addUserData(true))
                    .addOnFailureListener(e -> this.getUser.addUserData(false));
        }
    }

    private void addImage(String image) {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        if(image!=null){
            Uri uri = Uri.parse(image);
            StorageReference ref = storageReference.child(Constants.CollectionsNames.IMAGES + currentUser.getUid());
            ref.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> SessionUser.getInstance().getUser().setImgUser(image));
        }
    }

    // Get User

    public void getUserData() {
        if (this.getUser != null) {
            this.currentUser = this.firebaseAuth.getCurrentUser();
            DocumentReference myUserRef = firebaseFirestore.collection(Constants.CollectionsNames.USER).document(currentUser.getUid());
            myUserRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    this.getUser.getUserData(false);
                }
                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    this.user = user;
                    try {
                        this.user.setPassword(UtilsEncryptDecryptAES.decrypt(user.getPassword()));
                        this.getPhoto();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    this.getUser.getUserData(false);
                }
            });
        }
    }

    public void getPhoto() {
        this.currentUser = this.firebaseAuth.getCurrentUser();
        this.storageReference.child(Constants.CollectionsNames.IMAGES + currentUser.getUid()).getDownloadUrl().addOnSuccessListener(uri -> {
            this.user.setImgUser(uri.toString());
            this.getUser.getUserData(true);
        }).addOnFailureListener(exception -> this.getUser.getUserData(false));
    }

}
