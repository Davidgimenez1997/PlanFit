package com.utad.david.planfit.Model.Nutrition;

import android.os.Parcel;
import android.os.Parcelable;

public class NutritionToning implements Parcelable {

    private String name;
    private String photo;
    private String video;
    private String description;
    private String type;

    public NutritionToning() {
    }

    protected NutritionToning(Parcel in) {
        name = in.readString();
        photo = in.readString();
        video = in.readString();
        description = in.readString();
    }

    public static final Creator<NutritionToning> CREATOR = new Creator<NutritionToning>() {
        @Override
        public NutritionToning createFromParcel(Parcel in) {
            return new NutritionToning(in);
        }

        @Override
        public NutritionToning[] newArray(int size) {
            return new NutritionToning[size];
        }
    };

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
