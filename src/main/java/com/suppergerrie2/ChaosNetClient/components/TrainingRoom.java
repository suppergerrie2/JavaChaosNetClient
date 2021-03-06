package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    transient List<FitnessRule> fitnessRules = new ArrayList<>();

    TrainingRoomStats stats = null;

    public TrainingRoom(String roomName, String namespace, String simulationModelNamespace) throws IllegalArgumentException {
        if (roomName == null || namespace == null || simulationModelNamespace == null) {
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
        if (other == this) return true;

        if (other.getClass() != this.getClass()) {
            return false;
        }

        TrainingRoom otherRoom = (TrainingRoom) other;

        //Only check config if both objects have configs
        if (config != null && otherRoom.config != null) {
            if (!config.equals(otherRoom.config)) {
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

    public List<FitnessRule> getFitnessRules(String event) {

        List<FitnessRule> validRules = new ArrayList<>();
        for (FitnessRule fitnessRule : this.fitnessRules) {
            if (fitnessRule.eventType.equals(event)) {
                validRules.add(fitnessRule);
            }
        }

        return validRules;
    }

    public void parseFitnessRules(JsonArray fitnessRules) {
        if (fitnessRules == null) {
            System.out.println("Fitness rules is null!");
            return;
        }

        if (this.fitnessRules == null) {
            this.fitnessRules = new ArrayList<>();
        } else {
            this.fitnessRules.clear();
        }

        for (int i = 0; i < fitnessRules.size(); i++) {
            JsonObject fitnessRule = fitnessRules.get(i).getAsJsonObject();

            Gson gson = new GsonBuilder().create();

            this.fitnessRules.add(gson.fromJson(fitnessRule, FitnessRule.class));
        }
    }

    public TrainingRoom update(TrainingRoom otherRoom) throws IllegalArgumentException {

        if (!otherRoom.namespace.equals(this.namespace) || !otherRoom.ownerName.equals(this.ownerName)) {
            throw new IllegalArgumentException("Room cannot be updated by room with different namespace!");
        }

        if (otherRoom.roomName != null) {
            this.roomName = otherRoom.roomName;
        }

        if (otherRoom.partitionNamespace != null) {
            this.partitionNamespace = otherRoom.partitionNamespace;
        }

        if (otherRoom.config != null) {
            this.config = otherRoom.config;
        }

        if (otherRoom.simulationModelNamespace != null) {
            this.simulationModelNamespace = simulationModelNamespace;
        }

        if (otherRoom.fitnessRules != null) {
            this.fitnessRules = otherRoom.fitnessRules;
        }

        if (otherRoom.stats != null) {
            this.stats = otherRoom.stats;
        }


        return this;
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