package com.utad.david.planfit.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User
        implements Parcelable,
        Comparable<User>{

    /******************************** VARIABLES *************************************+/
     *
     */

    private String email;
    private String password;
    private String fullName;
    private String nickName;
    private String imgUser;
    private String uid;

    public User() {

    }
    /******************************** COMPARA POR NOMBRE ALFABETICAMENTE *************************************+/
     *
     */

    @Override
    public int compareTo(User o) {
        return this.getNickName().compareTo(o.getNickName());
    }

    /******************************** GETTERS Y SETTERS *************************************+/
     *
     */

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /******************************** Parcelable *************************************+/
     *
     */

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        fullName = in.readString();
        nickName = in.readString();
        imgUser = in.readString();
        uid = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(fullName);
        dest.writeString(nickName);
        dest.writeString(imgUser);
        dest.writeString(uid);
    }
}
