package com.suppergerrie2.ChaosNetClient.components.nnet.neurons;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class HiddenNeuron extends AbstractNeuron {

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public AbstractNeuron parseFromJson(JsonObject object) {
        return new GsonBuilder().create().fromJson(object, getClass());
    }
}
