package com.teknesya.jeevanbimacamp;

public class ItemLead {
    String key,value;
    String nodeId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ItemLead(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public ItemLead(String key, String value, String nodeID) {
        this.key = key;
        this.value = value;
        this.nodeId = nodeID;
    }
}
