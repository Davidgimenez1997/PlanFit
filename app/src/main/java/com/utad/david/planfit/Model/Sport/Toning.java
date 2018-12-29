package com.utad.david.planfit.Model.Sport;

import android.os.Parcel;
import android.os.Parcelable;

public class Toning implements Parcelable {

    private String name;
    private String photo;
    private String video;
    private String description;

    public Toning() {
    }

    protected Toning(Parcel in) {
        name = in.readString();
        photo = in.readString();
        video = in.readString();
        description = in.readString();
    }

    public static final Creator<Toning> CREATOR = new Creator<Toning>() {
        @Override
        public Toning createFromParcel(Parcel in) {
            return new Toning(in);
        }

        @Override
        public Toning[] newArray(int size) {
            return new Toning[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(video);
        dest.writeString(description);
    }
}
