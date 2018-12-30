package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.Organism;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NeuralNetwork {

    @SerializedName("neurons")
    HashMap<String, BasicNeuron> neurons = new HashMap<>();

    List<BasicNeuron> outputs = new ArrayList<>();
    List<BasicNeuron> inputs = new ArrayList<>();

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

    public void addNeuron(BasicNeuron neuron) {
        this.neurons.put(neuron.id, neuron);
    }

    public void buildStructure() {
        for(BasicNeuron neuron : neurons.values()) {
            for(Connection neuronDependency : neuron.dependencies) {
                neuronDependency.neuron = this.neurons.get(neuronDependency.in);
            }

            if(neuron.baseType.equals("output")) {
                outputs.add(neuron);
            } else if (neuron.baseType.equals("input")) {
                inputs.add(neuron);
            }
        }
    }

    public Output[] evaluate(Organism owner) {

        Output[] outputs = new Output[this.outputs.size()];

        for(int i = 0; i < this.outputs.size(); i++) {
            outputs[i] = new Output(this.outputs.get(i).type, this.outputs.get(i).getValue(owner));
        }

        return outputs;
    }

    public class Output {
        public final String type;
        public final double value;

        Output(String type, double value) {
            this.type = type;
            this.value = value;
        }
    }
}

