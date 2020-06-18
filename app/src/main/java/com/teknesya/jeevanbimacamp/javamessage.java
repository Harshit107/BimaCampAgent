package com.teknesya.jeevanbimacamp;

public class javamessage
{
    private String date,message,name,time,from,type,message_id;

    public javamessage()
    {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public javamessage(String date, String message, String name, String time, String from, String type, String message_id) {
        this.date = date;
        this.message = message;
        this.name = name;
        this.time = time;
        this.message_id = message_id;
        this.from = from;
        this.type = type;

    }
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
