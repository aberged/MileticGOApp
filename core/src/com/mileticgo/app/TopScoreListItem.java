package com.mileticgo.app;

import org.json.JSONObject;

public class TopScoreListItem {

    private final String userName;
    private final int userPoints;

    public TopScoreListItem(JSONObject topScoreItemJson) {
        userName = topScoreItemJson.getString("name");
        userPoints = topScoreItemJson.getInt("score");
    }

    public String getUserName() {
        return userName;
    }

    public int getUserPoints() {
        return userPoints;
    }
}
