package com.suppergerrie2.ChaosNetClient.components;

import com.google.gson.annotations.SerializedName;

public class FitnessRule {

    @SerializedName("eventType")
    String eventType;

    @SerializedName("scoreEffect")
    double scoreEffect;

    @SerializedName("lifeEffect")
    double liveEffect;

    @SerializedName("attribueId")
    String attributeID;

    @SerializedName("attributeValue")
    String attributeValue;

    public String getEventType() {
        return eventType;
    }

    public double getScoreEffect() {
        return scoreEffect;
    }

    public double getLiveEffect() {
        return liveEffect;
    }

    public String getAttributeID() {
        return attributeID;
    }

    public String getAttributeValue() {
        return attributeValue;
    }
}
