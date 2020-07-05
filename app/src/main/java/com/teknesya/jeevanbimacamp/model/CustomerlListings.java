package com.teknesya.jeevanbimacamp.model;


public class CustomerlListings {
    String name;
    String Image;
    String plan;
    String email;
    String nodeId;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public CustomerlListings(String name, String image, String plan, String email, String nodeId, String date, String phone) {
        this.name = name;
        Image = image;
        this.plan = plan;
        this.email = email;
        this.nodeId = nodeId;
        this.date = date;
        Phone = phone;
    }

    String Phone;

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
