package com.mileticgo.app;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class User {

    private String name;
    private String email;
    private String password;
    private String token;
    private boolean anonymous;
    private String activeCityProfileID;

    private CityProfile activeCityProfile;
    private UserInventory inventory;

    private UserInventory order;

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
        this.order = new UserInventory(new JSONArray());
    }

    void applyJson(JSONObject json) {
        this.name = json.getString("name");
        this.email = json.getString("email");
        this.password = "";
        this.token = json.getString("token");
        this.anonymous = json.getBoolean("anonymous");
        this.activeCityProfileID = json.getString("activeCityProfileID");
        this.inventory = new UserInventory(json.getJSONArray("inventory"));
        try {
            this.order = new UserInventory(json.getJSONArray("order"));
        }catch (Throwable war) {
            System.out.println(war.getMessage());
        }
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

    public UserInventory getOrder() {
        return order;
    }

    void addPinToInventory(CityPin pin){
        pin.setUnlocked(true);
        getInventory().addCityPinToInventory(pin, this.getActiveCityProfile());
    }

    void removePinFromInventory(CityPin pin){
        getInventory().removeCityPinFromInventory(pin, this.getActiveCityProfile());
    }

    void refreshInventory(ArrayList<CityProfile> cityProfiles) {
        inventory.refresh(cityProfiles, true);
        order.refresh(cityProfiles, false);
        if (order.getCityPins(getActiveCityProfile()).size() == 0) {
            randomizePins(order, cityProfiles);
        }
    }

    private void randomizePins(UserInventory ui, ArrayList<CityProfile> cpArr) {
        for (CityProfile cp: cpArr
             ) {
            List<CityPin> cityPins = cp.getCityPins();
            ArrayList<CityPin> pins = new ArrayList<>();
            for (CityPin pin: cityPins
                 ) {
                pins.add(pin);
            }
            while (pins.size() > 0) {
                CityPin pin = pins.remove((int) Math.round(Math.random()*(pins.size()-1)));
                ui.addCityPinToInventory(pin, cp);
            }
        }
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
        json.put("order", new JSONArray(order.toJson()));
        return json.toString();
    }
}
