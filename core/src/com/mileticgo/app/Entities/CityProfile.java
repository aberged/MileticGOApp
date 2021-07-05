package com.mileticgo.app.Entities;

import java.util.List;

public class CityProfile {
    double lat;
    double lng;

    List<CityPin> cityPins;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public List<CityPin> getCityPins() {
        return cityPins;
    }
}
