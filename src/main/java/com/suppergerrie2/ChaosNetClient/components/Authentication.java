package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.ChaosNetClient;

import java.net.HttpURLConnection;

public class Authentication {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("idToken")
    private String idToken;

    @SerializedName("username")
    private String username;

    private ChaosNetClient client;
    private boolean saveRefreshToken;

    public boolean isAuthenticated() {
        return accessToken!=null;
    }

    public String getAccessToken() {

        if(accessToken==null) {
            if(refreshToken==null) {
                //Error!
                return null;
            } else {
                //Get new accessToken
            }
        }

        return  accessToken;
    }

    /**
     * Add the Authorization header with the accessToken. Fails when not first authorized using {@link ChaosNetClient#authenticate(String, String, boolean)} )}
     *
     * @author suppergerrie2
     * @param con The connection to authorize
     */
    public void authenticateConnection(HttpURLConnection con) {
        if (!isAuthenticated()) {
            System.err.println("Can't make an authorized request without accessToken!");
            return;
        }

        con.setRequestProperty("Authorization", getAccessToken());
    }

    public Authentication setClient(ChaosNetClient chaosNetClient) {
        if(this.client!=null) {
            System.err.println("Setting client but client is not null!");
        }
        this.client = chaosNetClient;

        return this;
    }

    public Authentication setSaveRefreshToken(boolean save) {
        saveRefreshToken = save;
        return this;
    }
}
