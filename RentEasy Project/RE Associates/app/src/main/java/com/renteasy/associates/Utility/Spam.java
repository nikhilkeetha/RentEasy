package com.renteasy.associates.Utility;

public class Spam {

    private String reason;
    private String Pincode;
    private String Id;
    private String key;

    public Spam(String reason, String Pincode, String Id,String key) {
        this.reason=reason;
        this.Pincode=Pincode;
        this.Id=Id;
        this.key=key;

    }

    public String getReason() {return reason;}
    public String getPincode() {return Pincode;}
    public String getId() {return Id;}
    public String getKey() {return key;}

}
