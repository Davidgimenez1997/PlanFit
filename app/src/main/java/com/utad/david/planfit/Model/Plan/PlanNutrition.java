package com.utad.david.planfit.Model.Plan;

public class PlanNutrition implements Comparable<PlanNutrition>{

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
}