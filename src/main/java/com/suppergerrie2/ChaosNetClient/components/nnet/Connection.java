package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.Organism;

public class Connection {

    @SerializedName("neuronId")
    String in;

    BasicNeuron neuron;

    @SerializedName("weight")
    double weight;

    @Override
    public String toString() {
        return "{"+
                "    In: " + in +
                "    Weight: " + weight +
                "}";
    }

    public double getValue(Organism owner) {
        return neuron.getValue(owner)*weight;
    }
}
