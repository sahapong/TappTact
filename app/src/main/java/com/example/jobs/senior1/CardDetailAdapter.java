package com.example.jobs.senior1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jobs on 12-Jan-17.
 */

public class CardDetailAdapter extends BaseAdapter {

    Context mContext;
    JSONObject json;
    int length;
    Iterator iterator;
    String[] key;

    public CardDetailAdapter(Context context, JSONObject json, int length) {
        this.mContext= context;
        this.json = json;
        this.length = length;
        iterator = json.keys();
        key= new String[length+1];
        int i=1;
        iterator.hasNext();
        while(iterator.hasNext()){
            key[i++]=(String)iterator.next();
        }


    }


    @Override
    public int getCount() {
        return length+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    public String getId(int position) {
//        return list.get(position).getId();
//    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.card_detail_row, parent, false);

        TextView value = (TextView)view.findViewById(R.id.value);
        TextView fieldName = (TextView)view.findViewById(R.id.field);
        View image = (View) view.findViewById(R.id.image);
        if(position==0){
            value.setVisibility(View.GONE);
            fieldName.setVisibility(View.GONE);

            image.setVisibility(View.VISIBLE);
        }
        else{

            fieldName.setText(key[position]);
            value.setText(json.optString(key[position]));
            Log.d("json",position+ key[position]);
        }


        return view;
    }
}
