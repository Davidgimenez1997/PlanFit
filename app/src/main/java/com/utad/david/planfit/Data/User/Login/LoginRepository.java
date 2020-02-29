package com.utad.david.planfit.Data.User.Login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utad.david.planfit.Model.User.UserCredentials;

public class LoginRepository {

    private static LoginRepository instance = new LoginRepository();

    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseUser currentUser;
    private GetLogin getLogin;

    private LoginRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static LoginRepository getInstance() {
        if (instance == null){
            synchronized (LoginRepository.class){
                if (instance == null) {
                    instance = new LoginRepository();
                }
            }
        }
        return instance;
    }

    public void setGetLogin(GetLogin getLogin) {
        this.getLogin = getLogin;
    }

    // Login Whit Email And Password

    public void loginWithEmailAndPassword(UserCredentials userCredentials) {
        if (this.getLogin != null) {
            this.firebaseAuth.signInWithEmailAndPassword(userCredentials.getEmail(), userCredentials.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            this.currentUser = this.firebaseAuth.getCurrentUser();
                            this.getLogin.loginWhitEmailAndPassword(true);
                        } else {
                            this.getLogin.loginWhitEmailAndPassword(false);
                        }
                    });
        }
    }
}