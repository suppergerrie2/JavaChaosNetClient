package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.annotations.SerializedName;

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

    public double getValue() {
        return neuron.getValue()*weight;
    }
}
