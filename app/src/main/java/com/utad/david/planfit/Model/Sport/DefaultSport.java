package com.utad.david.planfit.Model.Sport;

import android.os.Parcel;
import android.os.Parcelable;

public class DefaultSport implements Parcelable,Comparable<DefaultSport> {

    private String name;
    private String photo;
    private String video;
    private String description;
    private String type;

    public DefaultSport() {
    }

    protected DefaultSport(Parcel in) {
        name = in.readString();
        photo = in.readString();
        video = in.readString();
        description = in.readString();
    }

    public static final Creator<DefaultSport> CREATOR = new Creator<DefaultSport>() {
        @Override
        public DefaultSport createFromParcel(Parcel in) {
            return new DefaultSport(in);
        }

        @Override
        public DefaultSport[] newArray(int size) {
            return new DefaultSport[size];
        }
    };

    @Override
    public int compareTo(DefaultSport o) {
        return this.getName().compareTo(o.getName());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
