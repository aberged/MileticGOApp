package com.mileticgo.app.Entities;

import java.io.Serializable;

public class CityPin implements Serializable {

    double lat;
    double lng;
    boolean unlocked;
    boolean isNear;

    String title;
    String description;

    public CityPin(double lat, double lng, String title, String description, boolean unlocked, boolean isNear) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
        this.isNear = isNear;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getUnlocked() {
        return unlocked;
    }

    public boolean isNear() {
        return isNear;
    }

    public void setNear(boolean near) {
        isNear = near;
    }
}
