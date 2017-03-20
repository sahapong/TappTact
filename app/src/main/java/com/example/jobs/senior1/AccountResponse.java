package com.example.jobs.senior1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jobs on 09-Jan-17.
 */

public class AccountResponse {
    public AccountResponse(String id,String fname, String lname,String image){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.image = image;
    }
    @SerializedName("_id")
    private String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("Password")
    private String password;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @SerializedName("AccountEmail")
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("FirstName")
    private String fname;
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @SerializedName("LastName")
    private String lname;
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @SerializedName("AccountProfilePicture")
    private String image;
    public String getImage() {
        return image;
    }

    @SerializedName("Company")
    private String comp;
    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    @SerializedName("Website")
    private String site;
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    @SerializedName("Occupation")
    private String occu;
    public String getOccu() {
        return occu;
    }

    public void setOccu(String occu) {
        this.occu = occu;
    }

    @SerializedName("fireBaseToken")
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
