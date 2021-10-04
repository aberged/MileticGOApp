package com.mileticgo.app.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInventory {
    final Map<CityProfile, List<CityPin>> map;

    public UserInventory() {
        map = new HashMap<>();
    }

    public UserInventory addCityPinToInventory(CityPin pin, CityProfile cityProfile) {
        if (!map.containsKey(cityProfile)) {
            map.put(cityProfile, new ArrayList<CityPin>());
        }
        List<CityPin> pins = map.get(cityProfile);
        if (!pins.contains(pin))
            pins.add(pin);
        return this;
    }

    public UserInventory removeCityPinFromInventory(CityPin pin, CityProfile cityProfile){
        if (!map.containsKey(cityProfile)) return this;
        List<CityPin> pins = map.get(cityProfile);
        pins.remove(pin);
        return this;
    }

    public List<CityPin> getCityPins(CityProfile cityProfile){
        if (!map.containsKey(cityProfile)) return null;
        return map.get(cityProfile);
    }

}
