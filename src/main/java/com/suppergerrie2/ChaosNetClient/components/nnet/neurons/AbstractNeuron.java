package com.suppergerrie2.ChaosNetClient.components.nnet.neurons;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.Organism;
import com.suppergerrie2.ChaosNetClient.components.nnet.Connection;

public abstract class AbstractNeuron {

    @SerializedName("id")
    public String id;

    @SerializedName("$TYPE")
    String type;

    @SerializedName("_base_type")
    public String baseType;

    @SerializedName("dependencies")
    public Connection[] dependencies;

    Organism owner;

    public abstract double getValue();

    public final AbstractNeuron parseFromJson(JsonObject object, Organism organism) {

        this.owner = organism;

        return parseFromJson(object);
    }

    public abstract AbstractNeuron parseFromJson(JsonObject object);

    public Organism getOwner() {
        return owner;
    }

    double sigmoid(double in) {
        return (1) / (1 + Math.exp(-in));
    }
}
