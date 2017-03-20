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

public class EditCardAdapter extends BaseAdapter {

    Context mContext;
    JSONObject json;
    int length;
    Iterator iterator;
    String[] key;
    View v;

    public EditCardAdapter(Context context, JSONObject json, int length) {
        this.mContext= context;
        this.json = json;
        this.length = length;
        iterator = json.keys();
        key= new String[length];
        int i=0;
       // iterator.hasNext();
        while(iterator.hasNext()){
            key[i++]=(String)iterator.next();
        }


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

//    public String getId(int position) {
//        return list.get(position).getId();
//    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.edit_card_detail_row, parent, false);
       // v=view;
        EditText value = (EditText)view.findViewById(R.id.txtEdit);
        TextView fieldName = (TextView)view.findViewById(R.id.txtVw);
        View image = (View) view.findViewById(R.id.image);
//        if(position==0){
//            value.setVisibility(View.GONE);
//            fieldName.setVisibility(View.GONE);
//
//            image.setVisibility(View.VISIBLE);
//        }
//        else{
//
            fieldName.setText(key[position]);
            value.setText(json.optString(key[position]));
            Log.d("json",position+ key[position]+" "+json.optString(key[position]));
//        }


        return view;
    }
}
