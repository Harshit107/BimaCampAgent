package com.teknesya.jeevanbimacamp;

public class UserProfileData {

    String name,Image,email;
    public UserProfileData()
    {

    }

    public UserProfileData(String name, String image, String email) {
        this.name = name;
        Image = image;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
