package io.kodular.nsmarttechnologies6.RentEasy.Utility;

public class Property {

    private String Rate;
    private String description;
    private String ownerNm;
    private String Image;
    private String PhoneNm;
    private String pincode;
    private String H_or_S;
    private String id;

    public Property(String Rate, String description, String ownerNm, String Image, String PhoneNm, String pincode, String H_or_S, String id) {
        this.Rate=Rate;
        this.description=description;
        this.ownerNm=ownerNm;
        this.Image=Image;
        this.PhoneNm=PhoneNm;
        this.pincode=pincode;
        this.H_or_S=H_or_S;
        this.id=id;
    }

    public String getRate() {
        return Rate;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerNm() {
        return ownerNm;
    }

    public String getImage() {
        return Image;
    }

    public String getPhoneNm() {
        return PhoneNm;
    }

    public String getPincode() { return pincode; }

    public String getH_or_S() {
        return H_or_S;
    }

    public String getid() {
        return id;
    }
}
