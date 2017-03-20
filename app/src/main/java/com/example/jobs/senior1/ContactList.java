package com.example.jobs.senior1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jobs_dell on 12-Mar-17.
 */

public class ContactList {
    private static final String TAG = "ContactList";
//    private static ContactList cl;
    public static ArrayList<CardListResponse> cardList = null;
    protected ContactList(){

    }
    public static ArrayList<CardListResponse> getInstance(ArrayList<CardListResponse> temp){
        if(cardList==null){
//            cl = new ContactList();
//            Log.d(TAG, "Old CL: "+cardList.size());
            Log.d(TAG, "New CL Set: "+temp.size());
            cardList = temp;
            Collections.sort(cardList, new Comparator<CardListResponse>() {
                @Override
                public int compare(CardListResponse fruit1, CardListResponse fruit2)
                {

                    return  (fruit1.getFname()+fruit1.getLname()).compareTo(fruit2.getFname()+fruit2.getLname());
                }
            });
//            setCL(id);
        }
        Log.d(TAG, "Return: "+cardList.size());
        return cardList;
    }

    public static void destroy(){

        cardList = null;
//        cl=null;

    }
}
