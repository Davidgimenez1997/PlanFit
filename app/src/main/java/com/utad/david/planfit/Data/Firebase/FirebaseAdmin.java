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

}
