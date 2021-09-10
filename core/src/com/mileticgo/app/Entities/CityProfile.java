package com.mileticgo.app.Entities;

import java.util.ArrayList;
import java.util.List;

public class CityProfile {
    double lat = 45.2550458;
    double lng = 19.8447484;

    private List<CityPin> cityPins;

    public CityProfile(){
        CityPin p1 = new CityPin(45.24797381951175,19.828481122745778,"Staro katolicko groblje", true);
        CityPin p2 = new CityPin(45.24991796512109,19.828086628223925,"Futoski park", false);
        CityPin p3 = new CityPin(45.2474397019441,19.84313811108686,"Stadio Karadjordje", false);
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
