package com.mileticgo.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidPreferences;

import java.util.List;

public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.init(new AndroidPreferences(this.getSharedPreferences("data", 0)), (ready, updating, error, msg) -> {
            Log.d("MileticGO Repository - " +
                    "ready: ", Boolean.toString(ready) +
                    "; updating: " + Boolean.toString(updating) +
                    "; error: " + Boolean.toString(error) +
                    "; msg: " + msg);
        });
    }

    public Repository getRepository(){
        return Repository.get();
    }

    public void callGame(){
        Intent intent = new Intent(this, GameLauncher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
