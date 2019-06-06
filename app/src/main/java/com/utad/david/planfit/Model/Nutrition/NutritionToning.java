package com.utad.david.planfit.Model.Nutrition;

import android.os.Parcel;
import android.os.Parcelable;

public class NutritionToning
        implements Parcelable,
        Comparable<NutritionToning>{

    /******************************** VARIABLES *************************************+/
     *
     */

    private String name;
    private String photo;
    private String url;
    private String description;
    private String type;

    public NutritionToning() {

    }

    /******************************** COMPARA POR NOMBRE ALFABETICAMENTE *************************************+/
     *
     */

    @Override
    public int compareTo(NutritionToning o) {
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

    /******************************** Parcelable *************************************+/
     *
     */

    protected NutritionToning(Parcel in) {
        name = in.readString();
        photo = in.readString();
        url = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(url);
        dest.writeString(description);
    }
}
