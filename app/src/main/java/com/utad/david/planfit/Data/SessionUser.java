package com.utad.david.planfit.Data;

import android.content.Context;
import android.content.SharedPreferences;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Model.User;

public class SessionUser {

    private static SessionUser instance = new SessionUser();

    public FirebaseAdmin firebaseAdmin;
    public User user;

    private SessionUser() {
        firebaseAdmin = new FirebaseAdmin();
        user = new User();
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
