package com.example.jobs.senior1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.altbeacon.beacon.Beacon;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jobs on 12-Jan-17.
 */

public class ExchangeListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Beacon> beacons = new ArrayList<Beacon>();
    int length;
    ArrayList<AccountResponse> accList;
    Iterator iterator;
    String[] key;
    View v;

    public ExchangeListAdapter(Context context, ArrayList<AccountResponse> accList,ArrayList<Beacon> beacons2) {
        this.mContext= context;
//        this.beacons.addAll(x);
        this.beacons = beacons2;
        this.accList=accList;
        length = accList.size();
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        for(int i=0;i<beacons.size();i++){
//            String id = beacons.get(i).getId1()+"";
//            id= id.replaceAll("-","");
//            id = id.substring(8);
//            Call<AccountResponse> call = apiService.getProfileImg(id);
//            call.enqueue(new Callback<AccountResponse>() {
//                @Override
//                public void onResponse(Call<AccountResponse>call, Response<AccountResponse> response) {
//                    accList.add(response.body());
//                }
//
//                @Override
//                public void onFailure(Call<AccountResponse>call, Throwable t) {
//                    // Log error here since request failed
//                }
//            });
//        }

        Log.d("ExchangeListAdapter",accList.size()+" beacons ja" +
                "...");
    }


    @Override
    public int getCount() {
        return length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
//    public String getField(){
//        TextView fieldName = (TextView)v.findViewById(R.id.field);
//        return fieldName.getText()+"";
//    }
//    public String getValue(){
//        EditText value = (EditText)v.findViewById(R.id.value);
//        return value.getText()+"";
//    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getId(int position) {
        return beacons.get(position).getId1()+"";
    }
    public String getName(int position) {
        return accList.get(position).getFname()+" "+accList.get(position).getLname();
    }
    public String getToken(int position) {
        return accList.get(position).getToken();
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.exchange_detail_row, parent, false);
        final ImageView iv = (ImageView)view.findViewById(R.id.profile);
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//        String id = beacons.get(position).getId1()+"";
//        id= id.replaceAll("-","");
//        id = id.substring(8);
//        Log.d("MonitoringActivity",id);
//        Call<AccountResponse> call = apiService.getProfileImg(id);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView comp = (TextView) view.findViewById(R.id.company);
//        call.enqueue(new Callback<AccountResponse>() {
//            @Override
//            public void onResponse(Call<AccountResponse>call, Response<AccountResponse> response) {
//
//                Log.e("MonitoringActivity",response.raw()+"");
////                Log.d("imageProfile",response.body().getImage());
                Picasso.with(mContext).load(accList.get(position).getImage()).transform(new CircleTransform()).into(iv);
//                Log.d("MonitoringActivity","Lname:"+response.body().getLname());
                name.setText(accList.get(position).getFname()+" "+accList.get(position).getLname());
                comp.setText(accList.get(position).getComp());
//
//            }
//
//            @Override
//            public void onFailure(Call<AccountResponse>call, Throwable t) {
//                // Log error here since request failed
//                Log.e("restImage", t.toString());
//            }
//        });
        return view;
    }
}
