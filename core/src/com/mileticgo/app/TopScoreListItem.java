package com.mileticgo.app;

import org.json.JSONObject;

public class TopScoreListItem {

    private final String userName;
    private final int userRank;
    private final int userPoints;

    public TopScoreListItem(JSONObject topScoreItemJson) {
        userName = topScoreItemJson.getString("userName");
        userRank = topScoreItemJson.getInt("userRank");
        userPoints = topScoreItemJson.getInt("userPoints");
    }

    public String getUserName() {
        return userName;
    }

    public int getUserRank() {
        return userRank;
    }

    public int getUserPoints() {
        return userPoints;
    }
}
