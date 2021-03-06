package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.nnet.NeuralNetwork;
import com.suppergerrie2.ChaosNetClient.components.nnet.neurons.OutputNeuron;

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
    private String namespace;

    @SerializedName("name")
    private String name;

    @SerializedName("generation")
    private int generation;

    @SerializedName("owner_username")
    private String ownerUsername;

    @SerializedName("speciesNamespace")
    private String speciesNamespace;

    private transient NeuralNetwork neuralNetwork;

    @SerializedName("score")
    private double score;

    //Amount of time for the simulation to run, not to be confused with tileToLive, which is the time this organism lives in the DB
    public double liveLeft;

    @SerializedName("ttl")
    private double timeToLive;

    public Organism(String trainingRoomNamespace, String namespace, String name, int generation, String ownerUsername, String speciesNamespace, NeuralNetwork neuralNetwork, double score, double timeToLive, double liveLeft) {
        this.trainingRoomNamespace = trainingRoomNamespace;
        this.namespace = namespace;
        this.name = name;
        this.generation = generation;
        this.ownerUsername = ownerUsername;
        this.speciesNamespace = speciesNamespace;
        this.neuralNetwork = neuralNetwork;
        this.score = score;
        this.timeToLive = timeToLive;
        this.liveLeft = liveLeft;
    }

    public Organism() {

    }

    public void setNetwork(NeuralNetwork neuralNetwork) {
        if(this.neuralNetwork!=null) {
            System.out.println("Override neuralnetwork, this is probably a bug!");
        }

        this.neuralNetwork = neuralNetwork;
    }

    public void parseBiologyFromJson(JsonObject object) { }

    public OutputNeuron[] evaluate() {
        return neuralNetwork.evaluate();
    }

    public String getTrainingRoomNamespace() {
        return trainingRoomNamespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public int getGeneration() {
        return generation;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getSpeciesNamespace() {
        return speciesNamespace;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public double getScore() {
        return score;
    }

    /**
     * Calculates how long the organism has left to live. If it is negative the organisms should die
     *
     * @return The amount of life left
     */
    public double getLiveLeft() {
        return timeToLive - System.currentTimeMillis() / 1000D;
    }

    public double getTimeToLive() {
        return timeToLive;
    }

    public void increaseScore(double amount) {
        score += amount;
    }

    public void increaseLive(double amount) {
        liveLeft += amount;
    }
}
