package com.example.jobs.senior1;

import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jobs on 09-Jan-17.
 */

public class CardListResponse {

    @SerializedName("_id")
    private String id;

    public String getId() {
        return id;
    }

    @SerializedName("Note")
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @SerializedName("CompanyName")
    private String comp;

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    @SerializedName("AccountID")
    private String accId;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @SerializedName("BusinessCardFname")
    private String fname;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    @SerializedName("BusinessCardLname")
    private String lname;

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    @SerializedName("Occupation")
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @SerializedName("Expand")
    private JsonArray Expand;

    public JsonArray getExpand() {
        return Expand;
    }

    public void setExpand(String JsonArray) {
        this.Expand = Expand;
    }

    @SerializedName("ExchangeLocation")
    private String ExchangeLocation;

    public String getExchangeLocation() {
        return ExchangeLocation;
    }

    public void setExchangeLocation(String ExchangeLocation) {
        this.ExchangeLocation = ExchangeLocation;
    }

    @SerializedName("TelephoneNumber")
    private String TelephoneNumber;

    public String getTelephoneNumber() {
        return TelephoneNumber;
    }

    public void setTelephoneNumber(String TelephoneNumber) {
        this.TelephoneNumber = TelephoneNumber;
    }

    @SerializedName("HashTags")
    private String[] hashTag;

    public List<String> getHashTags() {
        List<String> list = Arrays.asList(hashTag);
        return list;
    }

    public void setHashTag(String[] hashTag) {
        this.hashTag = hashTag;
    }


    @SerializedName("CardImage")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("AccountProfilePicture")
    private String pImage;

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    @SerializedName("Website")
    private String Website;

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    @SerializedName("BusinessCardEmail")
    private String BusinessCardEmail;

    public String getEmail() {
        return BusinessCardEmail;
    }

    public void setEmail(String BusinessCardEmail) {
        this.BusinessCardEmail = BusinessCardEmail;
    }

}
