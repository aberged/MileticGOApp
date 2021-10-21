package com.mileticgo.app;

import java.util.List;

public interface LeaderboardCallback {
    void result(List<TopScoreListItem> list);
    void error(String message);
}
