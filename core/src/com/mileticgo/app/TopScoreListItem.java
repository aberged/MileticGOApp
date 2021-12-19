package com.mileticgo.app;

import org.json.JSONObject;

public class TopScoreListItem {

    private final String userName;
    private final int userPoints;
    private final String email;
    private int position;

    public TopScoreListItem(JSONObject topScoreItemJson) {
        userName = topScoreItemJson.getString("name");
        userPoints = topScoreItemJson.getInt("score");
        String mail = "";
        try {
            mail = topScoreItemJson.getString("email");
        }catch (Throwable err) {
            mail = "";
        }
        email = mail;
        position = 0;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public String getEmail() {
        return email;
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
