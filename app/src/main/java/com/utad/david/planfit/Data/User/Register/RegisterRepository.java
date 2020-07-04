package com.utad.david.planfit.Data.User.Register;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utad.david.planfit.Model.User.Credentials;

public class RegisterRepository {

    private static RegisterRepository instance = new RegisterRepository();

    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    private GetRegister getRegister;

    private RegisterRepository() { this.firebaseAuth = FirebaseAuth.getInstance(); }

    public static RegisterRepository getInstance() {
        if (instance == null){
            synchronized (RegisterRepository.class){
                if (instance == null) {
                    instance = new RegisterRepository();
                }
            }
        }
        return instance;
    }

    public void setGetRegister(GetRegister getRegister) {
        this.getRegister = getRegister;
    }

    /**
     * Register user whit email and password
     * @param credentials using for register
     */
    public void registerWithEmailAndPassword(Credentials credentials) {
        if (this.getRegister != null) {
            this.firebaseAuth.createUserWithEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                    .addOnSuccessListener(authResult -> {
                        this.currentUser = firebaseAuth.getCurrentUser();
                        this.getRegister.registerWithEmailAndPassword(true);
                    }).addOnFailureListener(e -> {
                this.getRegister.registerWithEmailAndPassword(false);

            });
        }
    }
}
