package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class MyPostings {

    private String Rate;
    private String description;
    private String Image;
    private String id;
    private String pincodeBucket;

    public MyPostings(String Rate, String description, String Image, String pincodeBucket, String id) {
        this.Rate=Rate;
        this.description=description;
        this.Image=Image;
        this.pincodeBucket=pincodeBucket;
        this.id=id;
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

    public String getid() {
        return id;
    }

    public String getPincodeBucket() {
        return pincodeBucket;
    }
}
