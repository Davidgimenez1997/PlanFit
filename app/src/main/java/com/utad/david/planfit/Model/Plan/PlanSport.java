package com.utad.david.planfit.Model.Plan;

import android.os.Parcel;
import android.os.Parcelable;
import com.utad.david.planfit.Model.Sport.DefaultSport;

public class PlanSport implements Parcelable {

    private String name;
    private String photo;
    private String timeStart;
    private String timeEnd;

    public PlanSport() {
    }

    public PlanSport(DefaultSport defaultSport,String timeStart,String timeEnd){
        this.name = defaultSport.getName();
        this.photo = defaultSport.getPhoto();
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
    }

    protected PlanSport(Parcel in) {
        name = in.readString();
        photo = in.readString();
        timeStart = in.readString();
        timeEnd = in.readString();
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

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(timeStart);
        dest.writeString(timeEnd);
    }
}
