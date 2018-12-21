package com.utad.david.planfit.Data.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAdmin {

    public interface FirebaseAdminLisener{
        void singInWithEmailAndPassword(boolean end);
        void registerWithEmailAndPassword(boolean end);
    }

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAdmin.FirebaseAdminLisener adminLisener;

    public void setAdminLisener(FirebaseAdminLisener adminLisener) {
        this.adminLisener = adminLisener;
    }

    public FirebaseAdmin() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerWithEmailAndPassword(String email,String password){
        if(adminLisener!=null){
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

    public void singInWithEmailAndPassword(String email,String password){
        if(adminLisener!=null){
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
}
