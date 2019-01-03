package com.suppergerrie2.ChaosNetClient.components.nnet.neurons;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.suppergerrie2.ChaosNetClient.components.nnet.Connection;

public class OutputNeuron extends AbstractNeuron {

    public double value;

    @Override
    public double getValue() {
        value = 0;

        for(Connection c : dependencies) {
            value += c.getValue();
        }

        value = sigmoid(value/=dependencies.length);
        return value;
    }

    @Override
    public AbstractNeuron parseFromJson(JsonObject object) {
        return new GsonBuilder().create().fromJson(object, getClass());
    }

}
