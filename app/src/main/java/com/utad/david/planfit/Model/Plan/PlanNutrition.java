package com.utad.david.planfit.Model.Plan;

import android.os.Parcel;
import android.os.Parcelable;

public class PlanNutrition
        implements Comparable<PlanNutrition>,
        Parcelable {

    /******************************** VARIABLES *************************************+/
     *
     */

    private String name;
    private String photo;
    private int type;
    private String id;
    private String isOk;

    public PlanNutrition() {

    }

    public PlanNutrition(String name, String photo, int type, String id, String isOk) {
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.id = id;
        this.isOk = isOk;
    }

    /******************************** COMPARA POR TIEMPO DE EMPIECE *************************************+/
     *
     */

    @Override
    public int compareTo(PlanNutrition o) {
        if(type < o.type){
            return -1;
        }
        if(type > o.type){
            return 1;
        }
        return 0;
    }

    /******************************** GETTERS Y SETTERS *************************************+/
     *
     */

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsOk() {
        return isOk;
    }

    public void setIsOk(String isOk) {
        this.isOk = isOk;
    }

    /******************************** Parcelable *************************************+/
     *
     */

    protected PlanNutrition(Parcel in) {
        name = in.readString();
        photo = in.readString();
        type = in.readInt();
        id = in.readString();
        isOk = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeInt(type);
        dest.writeString(id);
        dest.writeString(isOk);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlanNutrition> CREATOR = new Creator<PlanNutrition>() {
        @Override
        public PlanNutrition createFromParcel(Parcel in) {
            return new PlanNutrition(in);
        }

        @Override
        public PlanNutrition[] newArray(int size) {
            return new PlanNutrition[size];
        }
    };
}
