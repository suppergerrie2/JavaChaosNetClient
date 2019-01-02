package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.AbstractNeuron;

public class Connection {

    @SerializedName("neuronId")
    String in;

    AbstractNeuron neuron;

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
