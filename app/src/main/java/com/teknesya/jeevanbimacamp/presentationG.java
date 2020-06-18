package com.teknesya.jeevanbimacamp;

public class presentationG {
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    String name, date, nodeID, remaning;
    String string;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getRemaning() {
        return remaning;
    }

    public void setRemaning(String remaning) {
        this.remaning = remaning;
    }

    public presentationG(String name, String date, String nodeID, String remaning, String string) {
        this.name = name;
        this.date = date;
        this.nodeID = nodeID;
        this.remaning = remaning;
       this. string = string;
    }


}