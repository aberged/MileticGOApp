package com.mileticgo.app.Entities;

import java.util.ArrayList;
import java.util.List;

public class CityProfile {
    double lat = 45.2550458;
    double lng = 19.8447484;

    private List<CityPin> cityPins;

    public CityProfile(){
        CityPin p1 = new CityPin(1,2,"t1");
        CityPin p2 = new CityPin(1,2,"t2");
        CityPin p3 = new CityPin(1,2,"t3");
        cityPins = new ArrayList<>();
        cityPins.add(p1);
        cityPins.add(p2);
        cityPins.add(p3);
    }

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
