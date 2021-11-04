package com.mileticgo.app;

import org.json.JSONObject;

public class TopScoreListItem {

    private final String userName;
    private final int userPoints;
    private int position;

    public TopScoreListItem(JSONObject topScoreItemJson) {
        userName = topScoreItemJson.getString("name");
        userPoints = topScoreItemJson.getInt("score");
        position = 0;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    @Override
    public String toString() {
        return "Position: " + position + "\nUser name: " + userName + "\nuser points: " + userPoints;
    }
}
