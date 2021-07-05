package com.mileticgo.app.Entities;

import java.util.List;

public class UserInventory {
    CityProfile forCityProfile;
    List<CityPin> cityPins;

    public UserInventory addCityPinToInventory(CityPin pin) {
        cityPins.add(pin);
        return this;
    }

    public UserInventory removeCityPinFromInventory(CityPin pin){
        cityPins.remove(pin);
        return this;
    }

}
