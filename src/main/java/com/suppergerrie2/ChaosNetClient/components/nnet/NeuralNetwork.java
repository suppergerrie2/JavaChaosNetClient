package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.AbstractNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.InputNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.OutputNeuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NeuralNetwork {

    @SerializedName("neurons")
    HashMap<String, AbstractNeuron> neurons = new HashMap<>();

    List<OutputNeuron> outputs = new ArrayList<>();
    List<InputNeuron> inputs = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");

        for(String name : neurons.keySet()) {
            builder.append("    ").append(name).append(" : ").append(neurons.get(name)).append("\n");
        }

        builder.append("}");

        return builder.toString();
    }

    public void addNeuron(AbstractNeuron neuron) {
        this.neurons.put(neuron.id, neuron);
    }

    public void buildStructure() {
        for(AbstractNeuron neuron : neurons.values()) {
            for(Connection neuronDependency : neuron.dependencies) {
                neuronDependency.neuron = this.neurons.get(neuronDependency.in);
            }

            if(neuron instanceof OutputNeuron) {
                outputs.add((OutputNeuron) neuron);
            } else if (neuron instanceof InputNeuron) {
                inputs.add((InputNeuron) neuron);
            }
        }
    }

    public OutputNeuron[] evaluate() {

        for (OutputNeuron output : this.outputs) {
            output.getValue();
        }

        return outputs.toArray(new OutputNeuron[0]);
    }

    public static class Output {
        public final String type;
        public final double value;

        public HashMap<String, Object> extraData = new HashMap<>();

        Output(String type, double value) {
            this.type = type;
            this.value = value;
        }

        Object getExtraData(String key) {
            return extraData.getOrDefault(key, null);
        }
    }
}

