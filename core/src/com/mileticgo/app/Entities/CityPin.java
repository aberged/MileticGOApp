package com.mileticgo.app.Entities;

public class CityPin {

    double lat;
    double lng;
    boolean unlocked;

    String text;

    public CityPin(double lat, double lng, String text, boolean unlocked) {
        this.lat = lat;
        this.lng = lng;
        this.text = text;
        this.unlocked = unlocked;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getText() {
        return text;
    }

    public boolean getUnlocked() {
        return unlocked;
    }
}
