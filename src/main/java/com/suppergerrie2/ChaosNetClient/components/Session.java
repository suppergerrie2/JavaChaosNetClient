package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.FileHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Session {

    @SerializedName("username")
    private String username;

    @SerializedName("namespace")
    private String namespace;

    @SerializedName("ttl")
    private long sessionEndTime;

    TrainingRoom trainingRoom;

    public static Session loadFromFile(TrainingRoom room, String username, FileHelper helper) {
        JsonObject sessions = null;
        try {
            sessions = helper.loadAsJson("sessions.json").getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (sessions.has(room.ownerName + "-" + room.namespace)) {
            JsonObject sessionsForRoom = sessions.getAsJsonObject(room.ownerName + "-" + room.namespace);

            if (sessionsForRoom.has(username)) {
                JsonObject session = sessionsForRoom.getAsJsonObject(username);
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(session, Session.class);
            }
        }

        return null;
    }


    public void saveToFile(FileHelper fileHelper) {
        JsonObject sessionsFile;
        try {
            sessionsFile = fileHelper.loadAsJson("sessions.json").getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            sessionsFile = new JsonObject();
        }

        if (!sessionsFile.has(trainingRoom.ownerName + "-" + trainingRoom.namespace)) {
            sessionsFile.add(trainingRoom.ownerName + "-" + trainingRoom.namespace, new JsonObject());
        }

        JsonObject sessionsForRoom = sessionsFile.getAsJsonObject(trainingRoom.ownerName + "-" + trainingRoom.namespace);

        Gson gson = new GsonBuilder().create();
        JsonObject sessionAsObject = gson.toJsonTree(this).getAsJsonObject();
        sessionsForRoom.add(username, sessionAsObject);
        sessionsFile.add(trainingRoom.ownerName + "-" + trainingRoom.namespace, sessionsForRoom);

        try {
            fileHelper.saveJson("sessions.json", sessionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getNamespace() {
        return namespace;
    }

    public long getSessionEndTime() {
        return sessionEndTime;
    }

    public void setTrainingRoom(TrainingRoom room) {
        trainingRoom = room;
    }

    public TrainingRoom getTrainingRoom() {
        return trainingRoom;
    }

    public boolean isValid() {
        return sessionEndTime > System.currentTimeMillis() / 1000L;
    }
}
