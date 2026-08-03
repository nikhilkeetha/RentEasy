package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class SearchList {

    private String Rate;
    private String description;
    private String Image;
    private String pincode;
    private String id;
    private boolean isVerified;
    private boolean isAll;

    public SearchList(String Rate, String description, String Image, String pincode, String id, boolean isVerified,boolean isAll) {
        this.Rate=Rate;
        this.description=description;
        this.Image=Image;
        this.pincode=pincode;
        this.id=id;
        this.isVerified =isVerified;
        this.isAll=isAll;
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

    public String getPincode() { return pincode; }

    public String getid() {
        return id;
    }

    public boolean getIsVerified() {return isVerified;}

    public boolean getIsAll() {return isAll;}
}
