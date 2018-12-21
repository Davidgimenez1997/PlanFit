package com.utad.david.planfit.Data;

import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;

public class SessionUser {

    private static SessionUser instance = new SessionUser();

    public FirebaseAdmin firebaseAdmin;

    private SessionUser() {
        firebaseAdmin = new FirebaseAdmin();
    }

    public static SessionUser getInstance() {
        if (instance == null){
            synchronized (SessionUser.class){
                if (instance == null) {
                    instance = new SessionUser();
                }
            }
        }
        return instance;
    }
}
