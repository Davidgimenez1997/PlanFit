package com.utad.david.planfit.Model.Plan;

import android.os.Parcel;
import android.os.Parcelable;
import com.utad.david.planfit.Model.Sport.DefaultSport;

public class PlanSport
        implements Comparable<PlanSport>,
        Parcelable{

    /******************************** VARIABLES *************************************+/
     *
     */

    private String name;
    private String photo;
    private double timeStart;
    private double timeEnd;
    private String id;
    private String isOk;

    public PlanSport() {

    }

    public PlanSport(String name, String photo, double timeStart, double timeEnd, String id, String isOk) {
        this.name = name;
        this.photo = photo;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.id = id;
        this.isOk = isOk;
    }

    /******************************** COMPARA POR TIEMPO DE EMPIECE *************************************+/
     *
     */

    @Override
    public int compareTo(PlanSport o) {
        if(timeStart < o.timeStart){
            return -1;
        }
        if(timeStart > o.timeStart){
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

    public double getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(double timeStart) {
        this.timeStart = timeStart;
    }

    public double getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(double timeEnd) {
        this.timeEnd = timeEnd;
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

    protected PlanSport(Parcel in) {
        name = in.readString();
        photo = in.readString();
        timeStart = in.readInt();
        timeEnd = in.readInt();
        id = in.readString();
        isOk = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeDouble(timeStart);
        dest.writeDouble(timeEnd);
        dest.writeString(id);
        dest.writeString(isOk);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlanSport> CREATOR = new Creator<PlanSport>() {
        @Override
        public PlanSport createFromParcel(Parcel in) {
            return new PlanSport(in);
        }

        @Override
        public PlanSport[] newArray(int size) {
            return new PlanSport[size];
        }
    };

}
