package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class Sugession {
    private String name;
    private String zipcode;
    private String state;

    public Sugession(String name, String zipcode, String state) {
        this.name=name;
        this.zipcode=zipcode;
        this.state=state;
    }

    public String getName() {
        return name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getState() {
        return state;
    }
}