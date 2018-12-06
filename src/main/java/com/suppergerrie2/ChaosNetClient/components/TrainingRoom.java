package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;

public class TrainingRoom {

    @SerializedName("name")
    public String roomName;

    @SerializedName("owner_username")
    public String ownerName;

    @SerializedName("namespace")
    public String namespace;

    @SerializedName("partitionNamespace")
    public String partitionNamespace;

    public TrainingRoom(String roomName, String namespace) {
        this.roomName = roomName;
        this.namespace = namespace;
    }

    public TrainingRoom partitionNamespace(String namespace) {
        this.partitionNamespace = namespace;
        return this;
    }

    public TrainingRoom ownerName(String name) {
        this.ownerName = name;
        return this;
    }
}
