package com.mileticgo.app;

import org.json.JSONObject;

import java.io.Serializable;

public class CityPin implements Serializable {

    private final String ID;
    private final double lat;
    private final double lng;
    private final String title;
    private final String description;

    private boolean unlocked;
    private boolean isNear;

    CityPin(JSONObject cityPinJson) {
        this.ID = cityPinJson.getString("id");
        this.lat = cityPinJson.getDouble("lat");
        this.lng = cityPinJson.getDouble("lng");
        this.title = cityPinJson.getString("title");
        this.description = cityPinJson.getString("description");
        this.unlocked = false;
        this.isNear = false;
    }

    public String getId() { return ID; }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean getUnlocked() {
        return unlocked;
    }
    // set unlock only with Repository.get().addPinToInventory(CityPin pin)
    void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isNear() {
        return isNear;
    }

    public void setNear(boolean near) {
        isNear = near;
    }

}
