package com.teknesya.jeevanbimacamp;

public class PlanRecyclearData {

    String plan,maturity,premium,sumassured, startdate,key;

    public PlanRecyclearData(String plan, String maturity, String premium, String sumassured, String startdate, String key) {
        this.plan = plan;
        this.maturity = maturity;
        this.premium = premium;
        this.sumassured = sumassured;
        this.startdate = startdate;
        this.key = key;
    }
    public PlanRecyclearData(){

    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getSumassured() {
        return sumassured;
    }

    public void setSumassured(String sumassured) {
        this.sumassured = sumassured;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



}
