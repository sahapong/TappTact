package com.example.jobs.senior1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by jobs on 09-Jan-17.
 */

public interface  ApiInterface {

    @GET("/isAccount")
    Call<ResponseBody> isAccount(@Query("email") String email);
    @GET("/cardList")
    Call<List<CardListResponse>> getCardList(@Query("accId") String accId);

    @GET("/login")
    Call<AccountResponse> getAccount(@QueryMap Map<String, String> options);
    @GET("/updateAccount")
    Call<ResponseBody> updateAccount(@QueryMap Map<String, Object> options);
    @GET("/profile")
    Call<AccountResponse> getProfileImg(@Query("accId") String accId);

    @GET("/cardDetail")
    Call<ResponseBody> getCardDetail(@QueryMap Map<String, String> options);

    @GET("/contactList")
    Call<ArrayList<CardListResponse>> getContactList(@Query("accId") String accId);

    @GET("/inbox")
    Call<ArrayList<CardListResponse>> getInbox(@Query("accId") String accId);


    @GET("/sendCard")
    Call<ResponseBody> sendCard(@QueryMap Map<String, String> options);

    @GET("/updateCard")
    Call<ResponseBody> updateCard(@QueryMap Map<String, Object> options);

    @GET("/deleteCard")
    Call<ResponseBody> deleteCard(@Query("bizCardId") String bizCardId);


    @GET("/deleteContact")
    Call<ResponseBody> deleteContact(@QueryMap Map<String, String> options);

    @GET("/createCard")
    Call<ResponseBody> createCard(@QueryMap Map<String, Object> options);
    @GET("/createAccount")
    Call<ResponseBody> createAccount(@QueryMap Map<String, Object> options);


    @GET("/cardDetail2")
    Call<CardListResponse> getCardDetail2(@Query("bizCard") String cardId);

    @GET("/getView")
    Call<ViewResponse>getView(@Query("cardId") String cardId);


    @GET("/confirmCard")
    Call<ResponseBody> confirmCard(@QueryMap Map<String, Object> options);
}
