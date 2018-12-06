package com.suppergerrie2.ChaosNetClient;

import com.google.gson.*;
import com.suppergerrie2.ChaosNetClient.components.TrainingRoom;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The ChaosNetClient which will hide all of the magic so you can for example call {@link #createTrainingRoom(TrainingRoom)} to create room.
 *
 * @author suppergerrie2
 */
public class ChaosNetClient {

    String accessToken;
    String refreshToken;

    /**
     * Authorizes with the chaosnet server so this client can make authorized requests.
     *
     * @author suppergerrie2
     * @param username The username to authorize with
     * @param password The password to authorize with
     * @throws IOException Thrown when the URL is not valid (Eg host {@link Constants#HOST} is invalid). Or when the post request fails.
     */
    public void Authorize(String username, String password) throws IOException {

        if (accessToken != null || refreshToken != null) {
            System.out.println("Authorizing already authorized client!");
        }

        URL url = new URL(Constants.HOST + "/v0/auth/login");


        JsonObject object = new JsonObject();
        object.addProperty("username", username);
        object.addProperty("password", password);

        JsonObject response = doPostRequest(url, object).getAsJsonObject();
        accessToken = response.get("accessToken").getAsString();
        refreshToken = response.get("refreshToken").getAsString();
    }

    /**
     * Creates a trainingRoom based on the trainingroom passed in. Needs the client to be authorized using {@link #Authorize} first.
     *
     * @author suppergerrie2
     * @param trainingRoom The trainingroom to create
     * @return If successful true will be returned. (No check is made to guarantee it succeeded)
     */
    @SuppressWarnings("WeakerAccess")
    public boolean createTrainingRoom(TrainingRoom trainingRoom) {
        try {
            URL url = new URL(Constants.HOST + "/v0/trainingrooms");

            JsonObject body = new JsonObject();
            body.addProperty("name", trainingRoom.roomName);
            body.addProperty("namespace", trainingRoom.namespace);

            if(trainingRoom.partitionNamespace!=null) {
                body.addProperty("partitionNamespace", trainingRoom.partitionNamespace);
            }

            if(trainingRoom.ownerName!=null) {
                body.addProperty("owner_username", trainingRoom.ownerName);
            }

            doPostRequest(url, body, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all of the trainingrooms from the chaosnet server.
     *
     * @author suppergerrie2
     * @return An array of {@link TrainingRoom} with all of the trainingrooms found.
     */
    public TrainingRoom[] getTrainingRooms() {
        try {
            JsonArray result = doGetRequest(new URL(Constants.HOST + "/v0/trainingrooms"), false).getAsJsonArray();

            TrainingRoom[] trainingRooms = new TrainingRoom[result.size()];

            Gson gson = new GsonBuilder().create();

            for(int i = 0; i < result.size(); i++) {
                trainingRooms[i] = gson.fromJson(result.get(i).getAsJsonObject(), TrainingRoom.class);
            }

            return trainingRooms;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Writes the json object to the given {@link HttpURLConnection} body
     *
     * @author suppergerrie2
     * @param object The object to be written to the body.
     * @param con The connection to be written to
     * @throws IOException
     */
    private void writeToConnectionBody(JsonObject object, HttpURLConnection con) throws IOException {
        Gson gson = new GsonBuilder().create();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(gson.toJson(object));
        writer.close();
    }

    /**
     * Do a get request to the given {@link URL} and return the result as an {@link JsonObject}
     *
     * @author suppergerrie2
     * @param url The url to do the get request to.
     * @param authorized Whether this request should use the accesstoken.
     * @return The result is wrapped in a jsonelement.
     * @throws IOException
     */
    private JsonElement doGetRequest(URL url, boolean authorized) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if(authorized) {
            authorizeConnection(con);
        }

        return new JsonParser().parse(new InputStreamReader(con.getInputStream()));
    }

    /**
     * Add the Authorization header with the accessToken. Fails when not first authorized using {@link #Authorize(String, String)}
     *
     * @author suppergerrie2
     * @param con The connection to authorize
     */
    private void authorizeConnection(HttpURLConnection con) {
        if (accessToken == null) {
            System.err.println("Can't make an authorized request without accessToken!");
            return;
        }

        con.setRequestProperty("Authorization", accessToken);
    }

    /**
     * Do a post request to the given {@link URL} with the given {@link JsonObject} as body
     *
     * @author suppergerrie2
     * @param url The URL to do the post request to
     * @param body The body to send with the post request.
     * @return The result of the post request as a {@link JsonElement}
     * @throws IOException
     */
    private JsonElement doPostRequest(URL url, JsonObject body) throws IOException {
        return doPostRequest(url, body, accessToken!=null);
    }

    /**
     * Do a post request to the given {@link URL} with the given {@link JsonObject} as body
     *
     * @author suppergerrie2
     * @param url The URL to do the post request to
     * @param body The body to send with the post request.
     * @param authorized If true this request will be authorized using {@link #authorizeConnection(HttpURLConnection)}
     * @return The result of the post request as a {@link JsonElement}
     * @throws IOException
     */
    private  JsonElement doPostRequest(URL url, JsonObject body, boolean authorized) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        if(authorized) {
            authorizeConnection(con);
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        writeToConnectionBody(body, con);

        return new JsonParser().parse(new InputStreamReader(con.getInputStream()));
    }

}
