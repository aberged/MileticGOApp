package com.mileticgo.app.Entities;

import java.util.List;

public class User {

    String name;
    String email;

    CityProfile activeCityProfile;

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public CityProfile getActiveCityProfile(){
        return activeCityProfile;
    }

    public CityProfile setActiveCityProfile(CityProfile profile){
        return this.activeCityProfile = profile;
    }

    public List<CityProfile> getAvailableCityProfiles(){
        return null;
    }

    public UserInventory getUserInventory(){
        return getUserInventoryForCityProfile(activeCityProfile);
    }

    public UserInventory getUserInventoryForCityProfile(CityProfile cityProfile){
        return null;
    }

    public UserInventory addPinToInventory(CityPin pin){
        return getUserInventory().addCityPinToInventory(pin);
    }

    public UserInventory removePinFromInventory(CityPin pin){
        return getUserInventory().removeCityPinFromInventory(pin);
    }

    @Override
    public String toString() {
        return super.toString() + "[name=" + this.name + "]";
    }
}
