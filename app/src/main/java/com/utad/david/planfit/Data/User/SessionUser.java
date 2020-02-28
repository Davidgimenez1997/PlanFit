package com.utad.david.planfit.Data.User;

import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Model.User.UserCredentials;

public class SessionUser {

    private static SessionUser instance = new SessionUser();

    private User user;
    private UserCredentials userCredentials;

    private SessionUser() {
        this.userCredentials = new UserCredentials();
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
        this.userCredentials.setEmail(email);
        this.userCredentials.setPassword(password);
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

    public UserCredentials getUserCredentials() {
        return this.userCredentials;
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
        return user;
    }
}
