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

    private User user;


    public Repository(Preferences preferences) {
        this.net = new NetJavaImpl(16);
        this.preferences = preferences;
        this.user = new User("anonimus", "anonimus");
        CityProfile cityProfile = new CityProfile();
        this.user.setActiveCityProfile(cityProfile);
    }

    public void register(String name, String email, String password, RepositoryCallback callback) {
        this.user = new User(name, email);
        CityProfile cityProfile = new CityProfile();
        this.user.setActiveCityProfile(cityProfile);
        callback.onResult(true);
    }

    public void login(String email, String password, RepositoryCallback callback) {
        this.user = new User(email, email);
        CityProfile cityProfile = new CityProfile();
        this.user.setActiveCityProfile(cityProfile);
        callback.onResult(true);
    }

    public void logout(RepositoryCallback callback) {
        this.user = new User("anonimus", "anonimus");
        CityProfile cityProfile = new CityProfile();
        this.user.setActiveCityProfile(cityProfile);
        callback.onResult(true);
    }

    public User getUser(){
        return this.user;
    }

    public CityProfile getActiveCityProfile(){
        return getUser().getActiveCityProfile();
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

    public List<CityPin> getUserInventoryCityPins() { return getUserInventory().getCityPins(getActiveCityProfile()); }

    public List<CityProfile> getAvailableProfiles(){
        return null;
    }

}
