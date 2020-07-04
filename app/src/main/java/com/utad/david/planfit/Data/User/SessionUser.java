package com.utad.david.planfit.Data.User;

import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Model.User.Credentials;

public class SessionUser {

    private static SessionUser instance = new SessionUser();

    private User user;
    private Credentials credentials;

    private SessionUser() {
        this.credentials = new Credentials();
        this.user = new User();
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

    public void removeUser(){
        this.user.setEmail(null);
        this.user.setPassword(null);
        this.user.setImgUser(null);
        this.user.setNickName(null);
        this.user.setFullName(null);
    }

    public void setCredentials(String email, String password) {
        this.user.setEmail(email);
        this.user.setPassword(password);
        this.credentials.setEmail(email);
        this.credentials.setPassword(password);
    }

    public void setPhoto(String photo) {
        this.user.setImgUser(photo);
    }

    public void deletePhoto() {
        this.user.setImgUser(null);
    }

    public boolean checkImg () {
        return this.user.getImgUser() != null;
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public void setDataUser(String name, String nick, String img) {
        this.user.setNickName(nick);
        this.user.setFullName(name);
        this.user.setImgUser(img);
    }

    public void setUidUser (String uid) {
        this.user.setUid(uid);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
