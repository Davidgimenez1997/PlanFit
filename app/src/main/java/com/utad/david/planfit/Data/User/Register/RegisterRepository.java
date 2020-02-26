package com.utad.david.planfit.Data.User.Register;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    // Register Whit Email And Password

    public void registerWithEmailAndPassword(String email, String password) {
        if (this.getRegister != null) {
            this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            this.currentUser = firebaseAuth.getCurrentUser();
                            this.getRegister.registerWithEmailAndPassword(true);
                        } else {
                            this.getRegister.registerWithEmailAndPassword(false);
                        }
                    });
        }

    }
}
