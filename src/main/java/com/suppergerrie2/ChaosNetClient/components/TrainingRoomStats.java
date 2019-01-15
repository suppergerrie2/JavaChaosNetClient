package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;

public class TrainingRoomStats {

    @SerializedName("totalOrgsPerGen")
    int totalOrganismsPerGeneration = 0;

    @SerializedName("orgsSpawnedSoFar")
    int organismsSpawnedSoFar;

    @SerializedName("genProgress")
    double generationProgress; //Percentage

    @Override
    public String toString() {
        return "TotalOrganismsPerGeneration: " + totalOrganismsPerGeneration + " organismsSpawnedSoFar " + organismsSpawnedSoFar + " progress " + generationProgress + " %";
    }

    public int getTotalOrganismsPerGeneration() {
        return totalOrganismsPerGeneration;
    }

    public int getOrganismsSpawnedSoFar() {
        return organismsSpawnedSoFar;
    }

    public double getGenerationProgress() {
        return generationProgress;
    }
}
