package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class HomeProperty {

    private String Rate;
    private String description;
    private String Image;
    private String pincode;
    private String id;
    private String HorS;


    public HomeProperty(String Rate, String description, String Image, String pincode, String id, String HorS) {
        this.Rate=Rate;
        this.description=description;
        this.Image=Image;
        this.pincode=pincode;
        this.id=id;
        this.HorS=HorS;
    }

    public String getRate() {
        return Rate;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return Image;
    }

    public String getPincode() {
        return pincode;
    }

    public String getid() {
        return id;
    }

    public String getHorS() {
        return HorS;
    }
}
