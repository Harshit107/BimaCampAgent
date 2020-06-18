package com.teknesya.jeevanbimacamp.model;


public class CustomerlListings {
    String name;
    String Image;
    String plan;
    String email;
    String nodeId;

    public CustomerlListings() {

    }

    public CustomerlListings(String name, String image,  String email,String plan, String nodeId) {
        this.name = name;
        this.Image = image;
        this.plan = plan;
        this.email = email;
        this.nodeId=nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
