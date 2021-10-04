package com.mileticgo.app.Entities;

import java.util.List;

public class User {

    String name;
    String email;

    CityProfile activeCityProfile;
    UserInventory inventory;

    public User(String name, String email){
        this.name = name;
        this.email = email;
        this.inventory = new UserInventory();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public CityProfile getActiveCityProfile(){
        return activeCityProfile;
    }

    public CityProfile setActiveCityProfile(CityProfile profile){
        this.activeCityProfile = profile;
        inventory.addCityPinToInventory(profile.getCityPins().get(0), profile);
        //inventory.addCityPinToInventory(profile.getCityPins().get(1), profile);
        return activeCityProfile;
    }

    public List<CityProfile> getAvailableCityProfiles(){
        return null;
    }

    public UserInventory getUserInventory(){
        return inventory;
    }

    public UserInventory addPinToInventory(CityPin pin){
        return getUserInventory().addCityPinToInventory(pin, this.getActiveCityProfile());
    }

    public UserInventory removePinFromInventory(CityPin pin){
        return getUserInventory().removeCityPinFromInventory(pin, this.getActiveCityProfile());
    }

    public List<CityPin> getUserInventoryCityPins() { return inventory.getCityPins(getActiveCityProfile()); }

    @Override
    public String toString() {
        return super.toString() + "[name=" + this.name + "]";
    }
}
