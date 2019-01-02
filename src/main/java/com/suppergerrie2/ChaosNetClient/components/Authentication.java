package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.ChaosNetClient;
import com.suppergerrie2.ChaosNetClient.Constants;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Authentication {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("idToken")
    private String idToken;

    @SerializedName("expiration")
    private long authCodeExpiration;

    @SerializedName("issuedAt")
    private long authCodeIssuedAt;

    private ChaosNetClient client;

    private String username;

    public boolean isAuthenticated() {
        return accessToken != null;
    }

    public String getAccessToken() {

        if (accessToken == null || authCodeExpiration <= System.currentTimeMillis() / 1000L) {
            if (refreshToken == null) {
                //Error!
                return null;
            } else {
                return requestAuthToken(username, refreshToken);
            }
        }

        return accessToken;
    }

    public String requestAuthToken(String username, String refreshToken) {
        try {
            URL url = new URL(Constants.HOST + "/v0/auth/token");

            JsonObject body = new JsonObject();
            body.addProperty("username", username);
            body.addProperty("refreshToken", refreshToken);

            JsonObject object = client.doPostRequest(url, body, false).getAsJsonObject();
            this.accessToken = object.get("accessToken").getAsString();
            this.refreshToken = object.get("refreshToken").getAsString();
            this.authCodeExpiration = object.get("expiration").getAsLong();
            this.authCodeIssuedAt = object.get("issuedAt").getAsLong();
            this.idToken = object.get("idToken").getAsString();

            client.saveRefreshCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Add the Authorization header with the accessToken. Fails when not first authorized using {@link ChaosNetClient#authenticate(String, String, boolean)} )}
     *
     * @param con The connection to authorize
     * @author suppergerrie2
     */
    public void authenticateConnection(HttpURLConnection con) {
        if (!isAuthenticated()) {
            System.err.println("Can't make an authorized request without accessToken!");
            return;
        }

        con.setRequestProperty("Authorization", getAccessToken());
    }

    public Authentication setClient(ChaosNetClient chaosNetClient) {
        if (this.client != null) {
            System.err.println("Setting client but client is not null!");
        }
        this.client = chaosNetClient;

        return this;
    }

    public Authentication setUserName(String username) {
        this.username = username;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserName() {
        return username;
    }
}
