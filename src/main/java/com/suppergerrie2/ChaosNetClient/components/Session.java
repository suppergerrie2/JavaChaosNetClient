package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("username")
    private String username;

    @SerializedName("namespace")
    private String namespace;

    @SerializedName("ttl")
    private long sessionEndTime;

    TrainingRoom trainingRoom;

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
}
