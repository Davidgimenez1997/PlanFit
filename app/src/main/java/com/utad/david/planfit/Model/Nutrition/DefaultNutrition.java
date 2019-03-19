package com.utad.david.planfit.Model.Nutrition;

import android.os.Parcel;
import android.os.Parcelable;

public class DefaultNutrition implements Parcelable {

    private String name;
    private String photo;
    private String url;
    private String description;
    private String type;

    public DefaultNutrition() {
    }

    protected DefaultNutrition(Parcel in) {
        name = in.readString();
        photo = in.readString();
        url = in.readString();
        description = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DefaultNutrition> CREATOR = new Creator<DefaultNutrition>() {
        @Override
        public DefaultNutrition createFromParcel(Parcel in) {
            return new DefaultNutrition(in);
        }

        @Override
        public DefaultNutrition[] newArray(int size) {
            return new DefaultNutrition[size];
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
