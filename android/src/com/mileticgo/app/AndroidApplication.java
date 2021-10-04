package com.mileticgo.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.mileticgo.app.Entities.CityPin;

import java.util.List;

public class AndroidApplication extends Application {

    private Repository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        this.repository = new Repository(new AndroidPreferences(this.getSharedPreferences("data", 0)));

        // #################### test trace slobodno brisi  ######################
        List<CityPin> pins = repository.getUserInventoryCityPins();
        Log.d("pins1", (pins.size()==1) + pins.toString());
        repository.addPinToInventory(repository.getActiveCityPins().get(1));
        Log.d("pins2", (pins.size()==2) + pins.toString());
        repository.removePinFromInventory(repository.getActiveCityPins().get(1));
        Log.d("pins1", (pins.size()==1) + pins.toString());
        // ######################################################################
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
