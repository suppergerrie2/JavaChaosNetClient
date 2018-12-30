package com.suppergerrie2.ChaosNetClient.components.nnet;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.suppergerrie2.ChaosNetClient.components.Organism;

public class BasicNeuron {

    /*
        "type": "BlockPositionInput",
        "attributeId": "BLOCK_ID",
        "attributeValue": "3",
        "positionRange": {
            "minX": "-3",
            "maxX": "4",
            "minY": "-4",
            "maxY": "2",
            "minZ": "1",
            "maxZ": "-3"
        },
        "_base_type": "input",
        "dependencies": []
     */

    @SerializedName("id")
    public String id;

    @SerializedName("type")
    String type;

    @SerializedName("_base_type")
    String baseType;

    @SerializedName("dependencies")
    Connection[] dependencies;

    double value;

//    @Override
//    public String toString() {
//        StringBuilder s = new StringBuilder("Organism: { " +
//                "\n    type: " + type +
//                "\n    baseType: " + baseType);
//
//        s.append("\n    dependencies: {");
//        for(Connection dependency : dependencies) {
//            s.append("\n        Dependency: ").append(dependency);
//        }
//        s.append("\n    }");
//
//        s.append("\n}");
//        return s.toString();
//    }

    public double getValue(Organism owner) {
        if(type.equals("input")) {
            return value;
        }

        value = 0;

        for(Connection c : dependencies) {
            value += c.getValue(owner);
        }

        return sigmoid(value/=dependencies.length);
    }

    double sigmoid(double in) {
        return (1) / (1 + Math.exp(in));
    }

    public BasicNeuron parseFromJson(JsonObject object) {
        return new GsonBuilder().create().fromJson(object, getClass());
    }

    public String getType() {
        return type;
    }
}
