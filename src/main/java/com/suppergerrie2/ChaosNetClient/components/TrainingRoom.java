package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class TrainingRoom {

    @SerializedName("name")
    public String roomName;

    @SerializedName("owner_username")
    public String ownerName;

    @SerializedName("namespace")
    public String namespace;

    @SerializedName("partitionNamespace")
    public String partitionNamespace;

    @SerializedName("config")
    private Config config;

    @SerializedName("simModelNamespace")
    public String simulationModelNamespace;

    transient HashMap<String, Double> fitnessRules;

    TrainingRoomStats stats = null;

    public TrainingRoom(String roomName, String namespace, String simulationModelNamespace) throws IllegalArgumentException {
        if(roomName==null||namespace==null||simulationModelNamespace==null) {
            throw new IllegalArgumentException("Argument can not be null!");
        }

        this.roomName = roomName;
        this.namespace = namespace;
        this.simulationModelNamespace = simulationModelNamespace;
    }

    public TrainingRoom partitionNamespace(String namespace) {
        this.partitionNamespace = namespace;
        return this;
    }

    public TrainingRoom ownerName(String name) {
        this.ownerName = name;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if(other == this) return true;

        if(other.getClass() != this.getClass()) {
            return false;
        }

        TrainingRoom otherRoom = (TrainingRoom)other;

        //Only check config if both objects have configs
        if(config!=null&&otherRoom.config!=null) {
            if(!config.equals(otherRoom.config)) {
                return false;
            }
        }

        return roomName.equals(otherRoom.roomName)
                && ownerName.equals(otherRoom.ownerName)
                && namespace.equals(otherRoom.namespace)
                && partitionNamespace.equals(otherRoom.partitionNamespace);

    }

    public void setStats(TrainingRoomStats stats) {
        this.stats = stats;
    }

    public TrainingRoomStats getStats() {
        return stats;
    }

    public double getScoreEffect(String event) {
        return this.fitnessRules.containsKey(event) ? this.fitnessRules.get(event) : 0;
    }

    public void parseFitnessRules(JsonArray fitnessRules) {
        if (fitnessRules == null) {
            System.out.println("Fitness rules is null!");
            return;
        }
        for (int i = 0; i < fitnessRules.size(); i++) {
            JsonObject fitnessRule = fitnessRules.get(i).getAsJsonObject();
            this.fitnessRules.put(fitnessRule.get("eventType").getAsString(), fitnessRule.get("scoreEffect").getAsDouble());
        }
    }
}

class Config {

    @SerializedName("topTRank")
    private String topTaxonomicRank;

    @SerializedName("minSpeciesCount")
    private int minSpeciesCount;

    @SerializedName("maxSurvivngSpeciesPerGeneration")
    private int maxSurvivingSpeciesPerGeneration;

    @SerializedName("minOrganisimsPerSpeciesPerGeneration")
    private int minOrganisimsPerSpeciesPerGeneration;

    @SerializedName("maxSurvivingOrganisimsPerSpeciesPerGeneration")
    private int maxSurvivingOrganisimsPerSpeciesPerGeneration;

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Config otherConfig = (Config) other;

        return minSpeciesCount == otherConfig.minSpeciesCount
                && maxSurvivingSpeciesPerGeneration == otherConfig.maxSurvivingSpeciesPerGeneration
                && minOrganisimsPerSpeciesPerGeneration == otherConfig.minOrganisimsPerSpeciesPerGeneration
                && maxSurvivingOrganisimsPerSpeciesPerGeneration == ((Config) other).maxSurvivingOrganisimsPerSpeciesPerGeneration
                && topTaxonomicRank.equals(otherConfig.topTaxonomicRank);
    }
}