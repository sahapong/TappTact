package com.example.jobs.senior1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jobs on 12-Jan-17.
 */

public class ContactListResponse {
    public ContactListResponse(String bizCard,String accId){
       this.bizCard = bizCard;
        this.accId = accId;
    }
    @SerializedName("TargetBusinessCard")
    private String bizCard;
    public String getBizCard() {
        return bizCard;
    }

    @SerializedName("AccountID")
    private String accId;
    public String getAccId() {
        return accId;
    }
}
