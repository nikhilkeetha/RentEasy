package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class HomeItems {
    private String Rate;
    private String title;
    private String Image;
    private String area;
    private String pincode;
    private String id;
    private String city;

    public HomeItems(String Rate, String title, String Image, String pincode, String id,String area,String city) {
        this.Rate=Rate;
        this.title=title;
        this.Image=Image;
        this.pincode=pincode;
        this.id=id;
        this.area=area;
        this.city=city;
    }
    public String getRate() {
        return Rate;
    }

    public String getTitle() {
        return title;
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

    public String getArea() {return area;}

    public String getCity() {return city;}
}
