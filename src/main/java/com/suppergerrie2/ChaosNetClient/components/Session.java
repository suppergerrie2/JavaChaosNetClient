package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;

public class Session {

    @SerializedName("trainingRoom")
    private TrainingRoom room;

    @SerializedName("username")
    private String username;

    @SerializedName("namespace")
    private String namespace;

    @SerializedName("ttl")
    private long sessionEndTime;

    public TrainingRoom getTrainingRoom() {
        return room;
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
}
