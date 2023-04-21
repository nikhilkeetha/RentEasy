package com.renteasy.associates.Utility;

public class Promote {

    private String Name;
    private String Pincode;
    private String Id;
    private String PhoneNo;
    private String Date;
    private String key;

    public Promote(String Name, String Pincode, String Id, String PhoneNo, String Date,String key) {

        this.Name=Name;
        this.Pincode=Pincode;
        this.Id=Id;
        this.PhoneNo=PhoneNo;
        this.Date=Date;
        this.key=key;

    }

    public String getName() {return Name;}
    public String getPincode() {return Pincode;}
    public String getId() {return Id;}
    public String getPhoneNo() {return PhoneNo;}
    public String getDate() {return Date;}
    public String getKey() {return key;}

}
