package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.nnet.BasicNeuron;
import com.suppergerrie2.ChaosNetClient.components.nnet.NeuralNetwork;

public class Organism {
    /*

     {
            "trainingRoomNamespace": "supper-debug",
            "namespace": "species-suppergerrie2-arlena-----0001-89373--3968-31ey-0-mozell-BrP4",
            "name": "mozell",
            "generation": "0",
            "owner_username": "suppergerrie2",
            "speciesNamespace": "species-suppergerrie2-arlena-----0001-89373--3968-31ey",
            "nNet": "{\"neurons\":{\"neuron-0\":{\"type\":\"BlockPositionInput\",\"attributeId\":\"BLOCK_ID\",\"attributeValue\":\"2\",\"positionRange\":{\"minX\":\"1\",\"maxX\":\"-1\",\"minY\":\"-1\",\"maxY\":\"3\",\"minZ\":\"-1\",\"maxZ\":\"-3\"},\"_base_type\":\"input\",\"dependencies\":[]},\"neuron-1\":{\"type\":\"JumpOutput\",\"_base_type\":\"output\",\"dependencies\":[{\"neuronId\":\"neuron-0\",\"weight\":-0.94}]},\"neuron-2\":{\"type\":\"BlockPositionInput\",\"attributeId\":\"BLOCK_ID\",\"attributeValue\":\"3\",\"positionRange\":{\"minX\":\"-2\",\"maxX\":\"2\",\"minY\":\"-4\",\"maxY\":\"3\",\"minZ\":\"0\",\"maxZ\":\"-4\"},\"_base_type\":\"input\",\"dependencies\":[]},\"neuron-3\":{\"type\":\"WalkForwardOutput\",\"_base_type\":\"output\",\"dependencies\":[{\"neuronId\":\"neuron-2\",\"weight\":0.95}]},\"neuron-4\":{\"type\":\"BlockPositionInput\",\"attributeId\":\"BLOCK_ID\",\"attributeValue\":\"3\",\"positionRange\":{\"minX\":\"-3\",\"maxX\":\"1\",\"minY\":\"-4\",\"maxY\":\"5\",\"minZ\":\"-4\",\"maxZ\":\"4\"},\"_base_type\":\"input\",\"dependencies\":[]},\"neuron-5\":{\"type\":\"JumpOutput\",\"_base_type\":\"output\",\"dependencies\":[{\"neuronId\":\"neuron-4\",\"weight\":0.74}]}}}",
            "score": {},
            "ttl": "1546266523.262"
        }

     */

    @SerializedName("trainingRoomNamespace")
    String trainingRoomNamespace;

    @SerializedName("namespace")
    String namespace;

    @SerializedName("name")
    String name;

    @SerializedName("generation")
    int generation;

    @SerializedName("owner_username")
    String ownerUsername;

    @SerializedName("speciesNamespace")
    String speciesNamespace;

    transient NeuralNetwork neuralNetwork;

    @SerializedName("score")
    Object score; //What type will this be?

    @SerializedName("ttl")
    double timeToLive;

    public void setNetwork(NeuralNetwork neuralNetwork) {
        if(this.neuralNetwork!=null) {
            System.out.println("Override neuralnetwork, this is probably a bug!");
        }

        this.neuralNetwork = neuralNetwork;
    }

    public BasicNeuron[] evaluate() {
        return neuralNetwork.evaluate();
    }

    @Override
    public String toString() {
        return "Organism: { \n    TrainingRoomNamepsace: " + trainingRoomNamespace +
                "\n    namespace: " + namespace +
                "\n    name: " + name +
                "\n    generation: " + generation +
                "\n    ownerUsername: " + ownerUsername +
                "\n    speciesNamespace: " + speciesNamespace +
                "\n    neuralNetwork: " + neuralNetwork +
                "\n    score: " + score +
                "\n    timeToLive: " + timeToLive +
                "\n}";
    }
}
