package com.mileticgo.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserInventory {

    private final Map<CityProfile, List<CityPin>> map;

    private JSONArray jsonInventory;

    private UserInventory() {
        map = new HashMap<>();
    }

    UserInventory(JSONArray json) {
        this();
        jsonInventory = json;
    }

    public List<CityPin> getCityPins(CityProfile cityProfile){
        if (!map.containsKey(cityProfile)) {
            map.put(cityProfile, new ArrayList<CityPin>());
        }
        return map.get(cityProfile);
    }

    void addCityPinToInventory(CityPin pin, CityProfile cityProfile) {
        addCityPinToInventory(pin, cityProfile, true);
    }

    private void addCityPinToInventory(CityPin pin, CityProfile cityProfile, boolean refresh) {
        if (!map.containsKey(cityProfile)) {
            map.put(cityProfile, new ArrayList<CityPin>());
        }
        List<CityPin> pins = map.get(cityProfile);
        if (!pins.contains(pin)) {
            pins.add(pin);
            pin.setUnlocked(true);
            if (refresh) refreshJsonInventory();
        }
    }
    void removeCityPinFromInventory(CityPin pin, CityProfile cityProfile){
        removeCityPinFromInventory(pin, cityProfile, true);
    }

    private void removeCityPinFromInventory(CityPin pin, CityProfile cityProfile, boolean refresh){
        if (!map.containsKey(cityProfile)) return;
        List<CityPin> pins = map.get(cityProfile);
        pins.remove(pin);
        pin.setUnlocked(false);
        if (refresh) refreshJsonInventory();
    }
    private void refreshJsonInventory() {
        this.jsonInventory = new JSONArray(this.toJson());
    }

    void refresh(ArrayList<CityProfile> cityProfiles) {
        for (int i=0;i<jsonInventory.length();i++) {
            JSONObject jsonCity = jsonInventory.getJSONObject(i);
            for (int j=0;j<cityProfiles.size();j++) {
                CityProfile cityProfile = cityProfiles.get(j);
                if (cityProfile.getId().equals(jsonCity.getString("id"))) {
                    JSONArray jsonCityPins = jsonCity.getJSONArray("pins");
                    List<CityPin> cityPins = cityProfile.getCityPins();
                    for (int k=0;k<jsonCityPins.length();k++) {
                        JSONObject jsonPin = jsonCityPins.getJSONObject(k);
                        for (int l=0;l<cityPins.size();l++) {
                            CityPin cityPin = cityPins.get(l);
                            if (cityPin.getId().equals(jsonPin.getString("id"))) {
                                addCityPinToInventory(cityPin, cityProfile, false);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
        refreshJsonInventory();
    }

    public String toJson() {
        JSONArray json = new JSONArray();
        Set<CityProfile> keys = map.keySet();
        for (CityProfile cityProfile : keys) {
            JSONObject cityItem = new JSONObject();
            cityItem.put("id", cityProfile.getId());
            JSONArray pins = new JSONArray();
            for (CityPin cityPin : map.get(cityProfile)) {
                JSONObject pin = new JSONObject();
                pin.put("id", cityPin.getId());
                pins.put(pin);
            }
            cityItem.put("pins", pins);
            json.put(cityItem);
        }
        return json.toString();
    }
}
