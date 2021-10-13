package com.mileticgo.app;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.NetJavaImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class Repository {

    private final NetJavaImpl net;
    private final Preferences preferences;

    public final static String USERSTORE = "user";
    public final static String PROFILESSTORE = "profiles";
    public final static String apiBaseUrl = "https://hello-vscf42q7eq-ew.a.run.app/api/";
    public final static String apiGetProfiles = apiBaseUrl + "cityprofile/";

    private final User user = new User();
    private final ArrayList<CityProfile> cityProfiles = new ArrayList<>();

    private static Repository repository;

    private boolean ready = false;

    private RepositoryCallback callback;

    private Repository(Preferences preferences) {
        if (Repository.repository != null) throw new InstantiationError();
        this.net = new NetJavaImpl(16);
        this.preferences = preferences;
    }

    public static Repository init(Preferences preferences) {
        if (Repository.repository != null) return Repository.repository;
        Repository.repository = new Repository(preferences);
        Repository.repository.setupRepository();
        return Repository.repository;
    }

    public static void init(Preferences preferences, final RepositoryCallback callback) {
        if (Repository.repository != null) return;
        Repository.repository = new Repository(preferences);
        Repository.repository.callback = callback;
        Repository.repository.setupRepository();
    }

    public static Repository get() {
        return repository;
    }

    private void setupRepository() {
        if (this.preferences.contains(USERSTORE)) {
            JSONObject json = new JSONObject(this.preferences.getString(USERSTORE));
            this.user.applyJson(json);
        } else {
            this.preferences.putString(USERSTORE, this.user.toJson()).flush();
        }

        if (this.preferences.contains(PROFILESSTORE)) {
            this.cityProfiles.clear();
            JSONArray profilesJson = new JSONArray(this.preferences.getString(PROFILESSTORE));
            for (int i=0; i<profilesJson.length(); i++){
                JSONObject cityProfile = profilesJson.getJSONObject(i);
                CityProfile aCityProfile = new CityProfile(cityProfile);
                this.cityProfiles.add(aCityProfile);
                if (aCityProfile.getId().equals(user.getActiveCityProfileID())) {
                    this.user.setActiveCityProfile(aCityProfile);
                }
            }
            this.user.refreshInventory(this.cityProfiles);
        } else {
            fetchData();
            return;
        }
        this.ready = true;
        if (callback != null) {
            callback.onResult(true);
        }
    }

    public void register(String name, String email, String password, final RepositoryCallback callback) {
        this.preferences.putString(USERSTORE,
        "{\"name\": \"" + name + "\",\"email\": \"" + email + "\",\"token\": \"\",\"anonymous\": false,\"activeCityProfileID\": \"0\",\"inventory\": [ {\"id\": \"0\",\"pins\": [] }] }"
        ).flush();
        // TODO inherit inventory and activeCityProfile from anonymous
        setupRepository();
        callback.onResult(true);
    }

    public void login(String email, String password, final RepositoryCallback callback) {
        this.preferences.putString(USERSTORE,
        "{\"name\": \"" + email + "\",\"email\": \"" + email + "\",\"token\": \"\",\"anonymous\": false,\"activeCityProfileID\": \"0\",\"inventory\": [ {\"id\": \"0\",\"pins\": [ {\"id\": \"1\" }, {\"id\": \"2\" }, {\"id\": \"3\" }] }] }"
        ).flush();
        setupRepository();
        callback.onResult(true);
    }

    public void logout(RepositoryCallback callback) {
        this.user.logout();
        this.preferences.putString(USERSTORE, this.user.toJson()).flush();
        this.user.setActiveCityProfile(findCityProfileByID(user.getActiveCityProfileID()));
        setupRepository();
        callback.onResult(true);
    }

    public User getUser(){
        return this.user;
    }

    public List<CityProfile> getCityProfiles() {
        return this.cityProfiles;
    }

    public CityProfile getActiveCityProfile(){
        return getUser().getActiveCityProfile();
    }

    public List<CityPin> getActiveCityPins(){
        return getActiveCityProfile().getCityPins();
    }
    public UserInventory getUserInventory(){
        return getUser().getInventory();
    }

    public List<CityPin> getUserInventoryCityPinsForActiveCityProfile() {
        return getUserInventory().getCityPins(getActiveCityProfile());
    }

    public List<CityPin> getUserInventoryCityPins(CityProfile cityProfile) {
        return getUserInventory().getCityPins(cityProfile);
    }

    public void addPinToInventory(CityPin pin){
        getUser().addPinToInventory(pin);
        this.preferences.putString(USERSTORE, this.user.toJson()).flush();
    }

    public void removePinFromInventory(CityPin pin){
        getUser().removePinFromInventory(pin);
        this.preferences.putString(USERSTORE, this.user.toJson()).flush();
    }

    public CityProfile findCityProfileByID(String activeCityProfileID) {
        for (int i=0;i<cityProfiles.size();i++) {
            CityProfile aCityProfile= cityProfiles.get(i);
            if (aCityProfile.getId().equals(activeCityProfileID)) {
                return aCityProfile;
            }
        }
        return null;
    }

    public boolean isReady() {
        return ready;
    }

    private void fetchData() {
        Net.HttpRequest profilesReq = new Net.HttpRequest(Net.HttpMethods.GET);
        profilesReq.setUrl(apiGetProfiles);
        this.net.sendHttpRequest(profilesReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String res = httpResponse.getResultAsString();
                preferences.putString(PROFILESSTORE, res).flush();
                setupRepository();
            }
            @Override
            public void failed(Throwable t) { }
            @Override
            public void cancelled() { }
        });
    }

}
