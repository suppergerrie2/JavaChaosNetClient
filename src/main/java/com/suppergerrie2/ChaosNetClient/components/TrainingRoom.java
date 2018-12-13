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

    @SerializedName("config")
    private Config config;

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