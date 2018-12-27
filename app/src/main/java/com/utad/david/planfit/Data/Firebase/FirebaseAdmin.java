package com.utad.david.planfit.Data.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utad.david.planfit.Data.SessionUser;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAdmin {

    public interface FirebaseAdminLisener {
        void singInWithEmailAndPassword(boolean end);
        void registerWithEmailAndPassword(boolean end);
        void insertUserDataInFirebase(boolean end);
    }

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseAdmin.FirebaseAdminLisener adminLisener;
    public FirebaseFirestore firebaseFirestore;

    public void setAdminLisener(FirebaseAdminLisener adminLisener) {
        this.adminLisener = adminLisener;
    }

    public FirebaseAdmin() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void registerWithEmailAndPassword(String email, String password) {
        if (adminLisener != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FirebaseAdmin", "createUserWithEmail:success");
                                currentUser = mAuth.getCurrentUser();
                                adminLisener.registerWithEmailAndPassword(true);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FirebaseAdmin", "createUserWithEmail:failure", task.getException());
                                adminLisener.registerWithEmailAndPassword(false);
                            }
                        }
                    });
        }

    }

    public void singInWithEmailAndPassword(String email, String password) {
        if (adminLisener != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FirebaseAdmin", "signInWithEmail:success");
                                currentUser = mAuth.getCurrentUser();
                                adminLisener.singInWithEmailAndPassword(true);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FirebaseAdmin", "signInWithEmail:failure", task.getException());
                                adminLisener.singInWithEmailAndPassword(false);
                            }
                        }
                    });
        }
    }

    public void addDataCouldFirestore(){
        if(adminLisener!=null){
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("email", SessionUser.getInstance().user.getEmail());
            user.put("fullName", SessionUser.getInstance().user.getFullName());
            user.put("nickName", SessionUser.getInstance().user.getNickName());
            if(SessionUser.getInstance().user.getImgUser()!=null){
                user.put("imgUser",SessionUser.getInstance().user.getImgUser());
            }else{
                user.put("imgUser","");
            }

            // Add a new document with a generated ID
            firebaseFirestore.collection("users").document(currentUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            adminLisener.insertUserDataInFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            adminLisener.insertUserDataInFirebase(false);
                        }
                    });
        }
    }
}
