package com.mileticgo.app;

import android.app.Application;
import android.content.Intent;

import com.badlogic.gdx.backends.android.AndroidPreferences;

public class AndroidApplication extends Application {

    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        this.repository = new Repository(new AndroidPreferences(this.getSharedPreferences("data", 0)));
    }

    public Repository getRepository(){
        return this.repository;
    }

    public void callGame(){
        Intent intent = new Intent(this, GameLauncher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
