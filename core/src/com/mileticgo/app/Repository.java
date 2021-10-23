package com.mileticgo.app;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.net.NetJavaImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Repository {

    private final NetJavaImpl net;
    private final Preferences preferences;

    public final static String USERSTORE = "user";
    public final static String PROFILESSTORE = "profiles";
    public final static String SYNCSTORE = "sync";
    public final static String apiBaseUrl = "https://hello-vscf42q7eq-ew.a.run.app/api/";//"http://10.0.2.2:8080/api/";//
    public final static String apiGetProfiles = apiBaseUrl + "cityprofile/";
    public final static String apiLogin = apiBaseUrl + "login";
    public final static String apiRegister = apiBaseUrl + "register";
    public final static String apiAddPinToInventory = apiBaseUrl + "addPinToInventory";
    public final static String apiLeaderboard = apiBaseUrl + "leaderboard";

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
            fetchCityData();
            return;
        }
        this.ready = true;
        if (callback != null) {
            try { callback.onResult(true);
            } catch (Throwable err) {
                System.out.println(err.getMessage());
            }
        }
        syncQueued();
    }

    public void register(String name, String email, String password, final RepositoryCallback callback) {
        getUser().setName(name);
        getUser().setEmail(email);
        getUser().setPassword(password);
        Net.HttpRequest registerReq = new Net.HttpRequest(Net.HttpMethods.POST);
        registerReq.setUrl(apiRegister);
        registerReq.setHeader("Content-Type", "application/json");
        registerReq.setContent(getUser().toJson());
        this.net.sendHttpRequest(registerReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode()==200) {
                    String res = httpResponse.getResultAsString();
                    Repository.this.preferences.putString(USERSTORE, res).flush();
                    setupRepository();
                    try { callback.onResult(true);
                    } catch (Throwable err) {
                        System.out.println(err.getMessage());
                    }
                } else {
                    setupRepository();
                    try { callback.onResult(false);
                    } catch (Throwable err) {
                        System.out.println(err.getMessage());
                    }
                }
            }
            @Override
            public void failed(Throwable t) {
                setupRepository();
                try { callback.onResult(false);
                } catch (Throwable err) {
                    System.out.println(err.getMessage());
                }
            }
            @Override
            public void cancelled() {
                setupRepository();
                try { callback.onResult(false);
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
        });
    }

    public void login(String email, String password, final RepositoryCallback callback) {
        Net.HttpRequest loginReq = new Net.HttpRequest(Net.HttpMethods.POST);
        loginReq.setUrl(apiLogin);
        loginReq.setHeader("Content-Type", "application/json");
        loginReq.setContent("{\"email\": \""+email+"\", \"password\":\""+password+"\"}");
        this.net.sendHttpRequest(loginReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode()==200) {
                    String res = httpResponse.getResultAsString();
                    Repository.this.preferences.putString(USERSTORE, res).flush();
                    setupRepository();
                    try { callback.onResult(true);
                    } catch (Throwable err) { System.out.println(err.getMessage()); }
                } else {
                    try { callback.onResult(false);
                    } catch (Throwable err) { System.out.println(err.getMessage()); }
                }
            }
            @Override
            public void failed(Throwable t) {
                try { callback.onResult(false);
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
            @Override
            public void cancelled() {
                try { callback.onResult(false);
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
        });
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
        addPinToSyncQueue(getUser().getActiveCityProfileID(), pin.getId());
        syncQueued();
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

    public void getLeaderboard(final LeaderboardCallback callback) {
        Net.HttpRequest leaderboardReq = new Net.HttpRequest(Net.HttpMethods.GET);
        leaderboardReq.setUrl(apiLeaderboard + "?cityID=" + getUser().getActiveCityProfileID());
        this.net.sendHttpRequest(leaderboardReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode()==200) {
                    ArrayList<TopScoreListItem> list = new ArrayList<>();
                    try {
                        String res = httpResponse.getResultAsString();
                        JSONArray arr = new JSONArray(res);
                        for (int i = 0; i < arr.length(); i++) {
                            list.add(new TopScoreListItem(arr.getJSONObject(i)));
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    callback.result(list);
                } else {
                    callback.error("Nesto poslo je po zlu. Proveri vezu sa internetom.");
                }
            }
            @Override
            public void failed(Throwable t) {
                callback.error("Nesto poslo je po zlu. Proveri vezu sa internetom.");
            }
            @Override
            public void cancelled() {
                callback.error("Nesto poslo je po zlu. Proveri vezu sa internetom.");
            }
        });
    }

    private void fetchCityData() {
        Net.HttpRequest cityProfilesReq = new Net.HttpRequest(Net.HttpMethods.GET);
        cityProfilesReq.setUrl(apiGetProfiles);
        this.net.sendHttpRequest(cityProfilesReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode()==200) {
                    String res = httpResponse.getResultAsString();
                    preferences.putString(PROFILESSTORE, res).flush();
                    setupRepository();
                } else {
                    try { callback.onResult(false);
                    } catch (Throwable err) { System.out.println(err.getMessage()); }
                }
            }
            @Override
            public void failed(Throwable t) {
                try { callback.onResult(false);
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
            @Override
            public void cancelled() {
                try { callback.onResult(false);
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
        });
    }

    private void addPinToSyncQueue(String cityProfileID, String pinID) {
        if (getUser().isAnonymous()) return;
        JSONArray syncItems = getSyncQueue();
        JSONObject item = new JSONObject();
        item.put("cityID", cityProfileID);
        item.put("pinID", pinID);
        item.put("email", getUser().getEmail());
        item.put("token", getUser().getToken());
        item.put("timestamp", new Date().getTime());
        syncItems.put(item);
        flushSYNCSTORE(syncItems.toString());
    }

    private void syncQueued() {
        JSONArray syncItems = getSyncQueue();
        if (syncItems.length()==0) return;
        JSONObject item = syncItems.getJSONObject(0);
        Net.HttpRequest loginReq = new Net.HttpRequest(Net.HttpMethods.POST);
        loginReq.setUrl(apiAddPinToInventory);
        loginReq.setHeader("Content-Type", "application/json");
        loginReq.setContent(item.toString());
        this.net.sendHttpRequest(loginReq, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                HttpStatus status = httpResponse.getStatus();
                if (status.getStatusCode()==200) {
                    flushSYNCSTORE(getSyncQueue().remove(0).toString());
                    syncQueued();
                }
            }
            @Override
            public void failed(Throwable t) { }
            @Override
            public void cancelled() { }
        });
    }

    synchronized void flushSYNCSTORE(String string) {
        this.preferences.putString(SYNCSTORE, string).flush();
    }

    private JSONArray getSyncQueue() {
        JSONArray syncItems = new JSONArray();
        if (this.preferences.contains(SYNCSTORE)) {
            syncItems = new JSONArray(this.preferences.getString(SYNCSTORE));
        }
        return syncItems;
    }

}
