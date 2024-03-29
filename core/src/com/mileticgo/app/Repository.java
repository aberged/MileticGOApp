package com.mileticgo.app;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.NetJavaImpl;
import com.mileticgo.app.Entities.CityPin;
import com.mileticgo.app.Entities.CityProfile;
import com.mileticgo.app.Entities.User;
import com.mileticgo.app.Entities.UserInventory;

import java.util.List;

public class Repository {

    private final NetJavaImpl net;
    private final Preferences preferences;


    public Repository(Preferences preferences) {
        this.net = new NetJavaImpl(16);
        this.preferences = preferences;
    }

    public User getUser(){
        return new User("asd");
    }

    public CityProfile getActiveCityProfile(){
        return getUser().getActiveCityProfile();
    }

    public List<CityProfile> getAvailableProfiles(){
        return null;
    }

    public List<CityPin> getActiveCityPins(){
        return getActiveCityProfile().getCityPins();
    }

    public UserInventory getUserInventory(){
        return getUser().getUserInventory();
    }

    public UserInventory addPinToInventory(CityPin pin){
        return getUser().addPinToInventory(pin);
    }

    public UserInventory removePinFromInventory(CityPin pin){
        return getUser().removePinFromInventory(pin);
    }

}
