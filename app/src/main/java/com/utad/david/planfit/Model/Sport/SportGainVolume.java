package com.utad.david.planfit.Model.Sport;

import android.os.Parcel;
import android.os.Parcelable;

public class SportGainVolume
        implements Parcelable,
        Comparable<SportGainVolume> {

    /******************************** VARIABLES *************************************+/
     *
     */

    private String name;
    private String photo;
    private String video;
    private String description;
    private String type;

    public SportGainVolume() {

    }

    /******************************** COMPARA POR NOMBRE ALFABETICAMENTE *************************************+/
     *
     */

    @Override
    public int compareTo(SportGainVolume o) {
        return this.getName().compareTo(o.getName());
    }


    /******************************** GETTERS Y SETTERS *************************************+/
     *
     */

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

    /******************************** Parcelable *************************************+/
     *
     */

    protected SportGainVolume(Parcel in) {
        name = in.readString();
        photo = in.readString();
        video = in.readString();
        description = in.readString();
    }

    public static final Creator<SportGainVolume> CREATOR = new Creator<SportGainVolume>() {
        @Override
        public SportGainVolume createFromParcel(Parcel in) {
            return new SportGainVolume(in);
        }

        @Override
        public SportGainVolume[] newArray(int size) {
            return new SportGainVolume[size];
        }
    };

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
