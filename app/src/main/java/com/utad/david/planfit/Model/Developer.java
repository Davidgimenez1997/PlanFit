package com.utad.david.planfit.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Developer implements Parcelable {

    private String fullNameDeveloper;
    private String urlLinkedinDeveloper;
    private String emailDeveloper;

    public Developer() {
    }

    protected Developer(Parcel in) {
        fullNameDeveloper = in.readString();
        urlLinkedinDeveloper = in.readString();
        emailDeveloper = in.readString();
    }

    public static final Creator<Developer> CREATOR = new Creator<Developer>() {
        @Override
        public Developer createFromParcel(Parcel in) {
            return new Developer(in);
        }

        @Override
        public Developer[] newArray(int size) {
            return new Developer[size];
        }
    };

    public String getFullNameDeveloper() {
        return fullNameDeveloper;
    }

    public void setFullNameDeveloper(String fullNameDeveloper) {
        this.fullNameDeveloper = fullNameDeveloper;
    }

    public String getUrlLinkedinDeveloper() {
        return urlLinkedinDeveloper;
    }

    public void setUrlLinkedinDeveloper(String urlLinkedinDeveloper) {
        this.urlLinkedinDeveloper = urlLinkedinDeveloper;
    }

    public String getEmailDeveloper() {
        return emailDeveloper;
    }

    public void setEmailDeveloper(String emailDeveloper) {
        this.emailDeveloper = emailDeveloper;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullNameDeveloper);
        dest.writeString(urlLinkedinDeveloper);
        dest.writeString(emailDeveloper);
    }
}
