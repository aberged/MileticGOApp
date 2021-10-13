package com.mileticgo.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityProfile {

    private final String id;
    private double lat;
    private double lng;
    private final String name;

    private final List<CityPin> cityPins;

    CityProfile(JSONObject cityProfile) {
        this.lat = cityProfile.getDouble("lat");
        this.lng = cityProfile.getDouble("lng");
        this.id = cityProfile.getString("id");
        this.name = cityProfile.getString("name");
        JSONArray cityPins = cityProfile.getJSONArray("cityPins");
        this.cityPins = new ArrayList<>();
        for (int j = 0; j < cityPins.length(); j++) {
            JSONObject cityPinJson = cityPins.getJSONObject(j);
            CityPin cityPin = new CityPin(cityPinJson);
            this.cityPins.add(cityPin);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
