package com.mileticgo.app;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.net.NetJavaImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class Repository {


    public final static String USERSTORE = "user";
    public final static String PROFILESSTORE = "profiles";
    public final static String SYNCSTORE = "sync";
    public final static String apiBaseUrl = "https://mileticgoapi-west6-vscf42q7eq-oa.a.run.app/api/";//"https://hello-vscf42q7eq-ew.a.run.app/api/";//"http://10.0.2.2:8080/api/";//
    public final static String apiGetProfiles = apiBaseUrl + "cityprofile/";
    public final static String apiLogin = apiBaseUrl + "login";
    public final static String apiRegister = apiBaseUrl + "register";
    public final static String apiAddPinToInventory = apiBaseUrl + "addPinToInentory";
    public final static String apiLeaderboard = apiBaseUrl + "leaderboard";
    private static final int GET_ACTIVE_CITYPINS_PAGE = 5;

    private final NetJavaImpl net;
    private final Preferences preferences;

    private static Repository repository;

    private final User user = new User();
    private final ArrayList<CityProfile> cityProfiles = new ArrayList<>();

    private boolean ready = false;
    private boolean updating = false;

    private boolean sessionStart = true;

    private ArrayList<RepositoryCallback> callbacks = new ArrayList<>();

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
        Repository.repository.callbacks.add(callback);
        Repository.repository.setupRepository();
    }

    public void addRepositoryCallback(RepositoryCallback rc) {
        callbacks.add(rc);
    }

    public boolean removeRepositoryCallback(RepositoryCallback rc) {
        return callbacks.remove(rc);
    }

    public static Repository get() {
        return repository;
    }

    private void setupRepository() {
        try {
            if (this.preferences.contains(USERSTORE)) {
                try {
                    JSONObject json = new JSONObject(this.preferences.getString(USERSTORE));
                    this.user.applyJson(json);
                } catch (Throwable err) {
                    state(false, true, true, "SetupRepository parse user error - " + err.getMessage());
                    this.user.logout();
                }
            } else {
                this.preferences.putString(USERSTORE, this.user.toJson()).flush();
            }

            if (this.preferences.contains(PROFILESSTORE)) {
                this.cityProfiles.clear();
                JSONArray profilesJson = new JSONArray(this.preferences.getString(PROFILESSTORE));
                for (int i = 0; i < profilesJson.length(); i++) {
                    JSONObject cityProfile = profilesJson.getJSONObject(i);
                    CityProfile aCityProfile = new CityProfile(cityProfile);
                    this.cityProfiles.add(aCityProfile);
                    if (aCityProfile.getId().equals(user.getActiveCityProfileID())) {
                        this.user.setActiveCityProfile(aCityProfile);
                    }
                }
                this.user.refreshInventory(this.cityProfiles);
            } else {
                sessionStart = false;
                fetchCityData();
                return;
            }
            this.ready = true;
            state(true, false, false, "SetupRepository done");
            syncQueued();
            if (sessionStart) {
                sessionStart = false;
                fetchCityData();
            }
        }catch (Throwable err) {
            state(true, false, true, "SetupRepository ERROR - " + err.getMessage() + "; err.toString - " + err.toString());
        }
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
                    try { callback.onResult(true, false, false, "Register ok");
                    } catch (Throwable err) {
                        System.out.println(err.getMessage());
                    }
                    state(true, false, false, "Register ok");
                } else {
                    setupRepository();
                    try { callback.onResult(true, false, true, "Register error");
                    } catch (Throwable err) {
                        System.out.println(err.getMessage());
                    }
                    state(true, false, true, "Register error");
                }
            }
            @Override
            public void failed(Throwable t) {
                setupRepository();
                try { callback.onResult(true, false, true, "Register error");
                } catch (Throwable err) {
                    System.out.println(err.getMessage());
                }
                state(true, false, true, "Register error");
            }
            @Override
            public void cancelled() {
                setupRepository();
                try { callback.onResult(true, false, true, "Register cancelled");
                } catch (Throwable err) { System.out.println(err.getMessage()); }
                state(true, false, true, "Register cancelled");
            }
        });
    }

    private void state(boolean ready, boolean updating, boolean error, String msg) {
        this.ready = ready;
        this.updating = updating;
        for (RepositoryCallback cb: this.callbacks) {
            if (cb != null) {
                try { cb.onResult(ready, updating, error, msg);
                } catch (Throwable err) {
                    System.out.println(err.getMessage());
                }
            }
        }
    }

    public void login(String email, String password, final RepositoryCallback callback) {
        state(false, true, false, "Login started");
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
                    try { callback.onResult(true, false, false, "Login ok");
                    } catch (Throwable err) { System.out.println(err.getMessage()); }
                    state(true, false, false,"Login ok");
                } else {
                    state(true, false, true, "Login error");
                    try { callback.onResult(true, false, true, "Login error");
                    } catch (Throwable err) { System.out.println(err.getMessage()); }
                }
            }
            @Override
            public void failed(Throwable t) {
                state(true, false, true,"Login error");
                try { callback.onResult(true, false, true,"Login error");
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
            @Override
            public void cancelled() {
                state(true, false, true,"Login cancelled");
                try { callback.onResult(true, false, true,"Login cancelled");
                } catch (Throwable err) { System.out.println(err.getMessage()); }
            }
        });
    }

    public void logout(RepositoryCallback callback) {
        this.user.logout();
        this.preferences.putString(USERSTORE, this.user.toJson()).flush();
        this.user.setActiveCityProfile(findCityProfileByID(user.getActiveCityProfileID()));
        setupRepository();
        callback.onResult(true, false, false,"Logout ok");
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
        if (user.getOrder() == null || user.getActiveCityProfile() == null || user.getOrder().getCityPins(user.getActiveCityProfile()) == null) {
            return Collections.emptyList();
        } else {
            int page = (int) Math.floor(user.getInventory().getCityPins(user.getActiveCityProfile()).size() / GET_ACTIVE_CITYPINS_PAGE);
            int totalSize = user.getOrder().getCityPins(user.getActiveCityProfile()).size();
            ArrayList<CityPin> retList = new ArrayList<>();
            for (int i=page*GET_ACTIVE_CITYPINS_PAGE; i < (page+1)*GET_ACTIVE_CITYPINS_PAGE && i < totalSize; i++)
                retList.add(user.getOrder().getCityPins(user.getActiveCityProfile()).get(i));
            return retList;
        }

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

    public boolean isUpdating() {
        return updating;
    }

    public void getLeaderboard(final LeaderboardCallback callback) {
        Net.HttpRequest leaderboardReq = new Net.HttpRequest(Net.HttpMethods.GET);
        leaderboardReq.setUrl(apiLeaderboard +
                "?cityID=" + getUser().getActiveCityProfileID() +
                (
                    Repository.get().getUser().isAnonymous() ?
                        "" :
                        "&email=" + Repository.get().getUser().getEmail()
                )
        );
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
        state(false, true, false, "FetchCityData init");
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
                    state(true, false, false, "FetchCityData ok");
                } else {
                    state(true, false, true, "FetchCityData error");
                }
            }
            @Override
            public void failed(Throwable t) {
                state(true, false, true, "FetchCityData error");
            }
            @Override
            public void cancelled() {
                state(true, false, true, "FetchCityData cancelled");
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
        item.put("order", new JSONArray(getUser().getOrder().toJson()));
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
                    JSONArray arr = getSyncQueue();
                    if (arr.length() > 0) arr.remove(0);
                    flushSYNCSTORE(arr.toString());
                    String res = httpResponse.getResultAsString();
                    User dbUser = new User();
                    dbUser.applyJson(new JSONObject(res));
                    List<CityPin> dbActiveCityProfileCityPins = dbUser.getInventory().getCityPins(user.getActiveCityProfile());
                    List<CityPin> myActiveCityProfilePins = Repository.get().getUserInventoryCityPinsForActiveCityProfile();
                    for (CityPin pin: dbActiveCityProfileCityPins
                         ) {
                        if (!user.getInventory().hasCityPinInInventory(user.getActiveCityProfile(), pin.getId())) {
                            for (CityPin p: myActiveCityProfilePins
                                 ) {
                                if (p.getId().equals(pin.getId())) {
                                    user.addPinToInventory(p);
                                    break;
                                }
                            }
                        }
                    }
                }
                syncQueued();
            }
            @Override
            public void failed(Throwable t) {
                syncQueued();
            }
            @Override
            public void cancelled() {
                syncQueued();
            }
        });
    }

    synchronized void flushSYNCSTORE(String string) {
        this.preferences.putString(SYNCSTORE, string).flush();
    }

    private JSONArray getSyncQueue() {
        JSONArray syncItems = new JSONArray();
        if (this.preferences.contains(SYNCSTORE)) {
            System.out.println("SyncQueuesJSON - " + this.preferences.getString(SYNCSTORE));
            try {
                syncItems = new JSONArray(this.preferences.getString(SYNCSTORE));
            }catch (Throwable err) {
                System.out.println("SyncQueuesJSON ERROR - " + err.getMessage());
                syncItems = new JSONArray();
            }
        }
        return syncItems;
    }

    public CityPin addRandomPinToInventory() {
        if (this.ready) {
            List<CityPin> inventoryPins = this.getUserInventoryCityPins(getActiveCityProfile());
            List<CityPin> activeCityPins = getActiveCityPins();
            int attempt = 0; boolean found = false;
            List<CityPin> candidates = new ArrayList<>();
            for (CityPin p: activeCityPins
                 ) {
                boolean there = false;
                for (CityPin ip: inventoryPins
                     ) {
                    if (ip.getId().equals(p.getId())) {
                        there = true; break;
                    }
                    if (there) break;
                }
                if (!there) {
                    addPinToInventory(p);
                    return p;
                }
            }
        }
        return null;
    }
}
