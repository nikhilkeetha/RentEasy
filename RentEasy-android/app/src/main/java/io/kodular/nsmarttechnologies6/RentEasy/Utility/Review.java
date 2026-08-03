package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class Review {
    private String des;
    private String Nm;
    private String city;
    private String url;

    public Review(String des, String Nm, String city,String url) {
        this.des=des;
        this.Nm=Nm;
        this.city=city;
        this.url=url;
    }

    public String getDes() {
        return des;
    }

    public String getNm() {
        return Nm;
    }

    public String getCity() {
        return city;
    }

    public String getUrl(){return url;}
}
