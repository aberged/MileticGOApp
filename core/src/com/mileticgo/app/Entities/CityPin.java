package com.mileticgo.app.Entities;

public class CityPin {

    double lat;
    double lng;

    String text;

    public CityPin(double lat, double lng, String text) {
        this.lat = lat;
        this.lng = lng;
        this.text = text;
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
}
