package com.utad.david.planfit.Model.Nutrition;

import android.os.Parcel;
import android.os.Parcelable;

public class NutritionSlimming implements Parcelable {

    private String name;
    private String photo;
    private String video;
    private String description;
    private String type;

    public NutritionSlimming() {
    }

    protected NutritionSlimming(Parcel in) {
        name = in.readString();
        photo = in.readString();
        video = in.readString();
        description = in.readString();
    }

    public static final Creator<NutritionSlimming> CREATOR = new Creator<NutritionSlimming>() {
        @Override
        public NutritionSlimming createFromParcel(Parcel in) {
            return new NutritionSlimming(in);
        }

        @Override
        public NutritionSlimming[] newArray(int size) {
            return new NutritionSlimming[size];
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
