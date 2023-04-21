package com.renteasy.associates.Utility;

public class Booking {

    private String number;
    private String pin;
    private String id;
    private String date;
    private String requirement;
    private String key;
    private boolean isCall;

    public Booking(String number, String pin, String id, String date, String requirement,String key,boolean isCall) {
        this.number = number;
        this.pin = pin;
        this.id = id;
        this.date = date;
        this.requirement = requirement;
        this.key=key;
        this.isCall=isCall;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getKey() {return key;}

    public boolean getIsCall() {return isCall;}

}
