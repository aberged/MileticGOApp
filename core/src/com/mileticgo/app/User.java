package com.mileticgo.app;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class User {

    private String name;
    private String email;
    private String password;
    private String token;
    private boolean anonymous;
    private String activeCityProfileID;

    private CityProfile activeCityProfile;
    private UserInventory inventory;

    User(){
        logout();
    }

    void logout() {
        this.name = "anonymous";
        this.email = "";
        this.token = "";
        this.password = "";
        this.activeCityProfileID = "0";
        this.anonymous = true;
        this.inventory = new UserInventory(new JSONArray());
    }

    void applyJson(JSONObject json) {
        this.name = json.getString("name");
        this.email = json.getString("email");
        this.password = "";
        this.token = json.getString("token");
        this.anonymous = json.getBoolean("anonymous");
        this.activeCityProfileID = json.getString("activeCityProfileID");
        this.inventory = new UserInventory(json.getJSONArray("inventory"));
    }

    public String getName() {
        return name;
    }

    void setName(String name) { this.name = name; }

    void setPassword(String pass) { this.password = pass; }

    public String getEmail() {
        return email;
    }

    void setEmail(String mail) { this.email = mail; }

    public String getToken() {
        return token;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public String getActiveCityProfileID() {
        return activeCityProfileID;
    }

    public CityProfile getActiveCityProfile(){
        return activeCityProfile;
    }

    void setActiveCityProfile(CityProfile profile){
        if (profile == null) return;
        this.activeCityProfile = profile;
        activeCityProfileID = profile.getId();
    }

    public UserInventory getInventory(){
        return inventory;
    }

    void addPinToInventory(CityPin pin){
        getInventory().addCityPinToInventory(pin, this.getActiveCityProfile());
    }

    void removePinFromInventory(CityPin pin){
        getInventory().removeCityPinFromInventory(pin, this.getActiveCityProfile());
    }

    void refreshInventory(ArrayList<CityProfile> cityProfiles) {
        inventory.refresh(cityProfiles);
    }

    @Override
    public String toString() {
        return super.toString() + "[name=" + this.name + "]";
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("email", email);
        json.put("password", password);
        json.put("token", token);
        json.put("anonymous", anonymous);
        json.put("activeCityProfileID", activeCityProfileID);
        json.put("inventory", new JSONArray(inventory.toJson()));
        return json.toString();
    }
}
