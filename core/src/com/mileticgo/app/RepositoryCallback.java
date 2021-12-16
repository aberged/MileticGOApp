package com.mileticgo.app;

public interface RepositoryCallback {
    void onResult(boolean ready, boolean updating, boolean error, String msg);
}
