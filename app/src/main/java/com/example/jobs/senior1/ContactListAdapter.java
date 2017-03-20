package com.example.jobs.senior1;

/**
 * Created by jobs on 09-Jan-17.
 */

import android.accounts.Account;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactListAdapter extends BaseAdapter {

    Context mContext;
    //    List<ContactListResponse> list;
    private static final String TAG = "ContactListAdapter";
    private static ArrayList<CardListResponse> card;

    public ContactListAdapter(Context context, ArrayList<CardListResponse> card) {
        this.mContext = context;
//        this.list = list;
        this.card = card;
        Log.d(TAG, "Contact size:" + card.size());
    }


    @Override
    public int getCount() {
        return card.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getId(int position) {
        return card.get(position).getId();
    }

    public View getView(int position, View view, ViewGroup parent) {
        Log.d(TAG,"Index:"+position+" "+card.get(position).getFname());
        if(position!=0&&card.get(position-1).getFname().charAt(0)==card.get(position).getFname().charAt(0)) {
            LayoutInflater mInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (view == null)
                view = mInflater.inflate(R.layout.contact_list_row, parent, false);


            return getCard(position, view);
        }
        else{
            LayoutInflater mInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            if (view == null)
                view = mInflater.inflate(R.layout.contact_list_section, parent, false);
            TextView header = (TextView) view.findViewById(R.id.header);
            header.setText((""+card.get(position).getFname().charAt(0)).toUpperCase());

            return getCard(position, view);
        }
    }

    private View getCard(final int position, final View view) {
        String fname = card.get(position).getFname();
        String lname = card.get(position).getLname();
        String companyName = card.get(position).getComp();
        String note = card.get(position).getNote();
        ImageView iv = (ImageView) view.findViewById(R.id.profile);
//
        Picasso.with(mContext).cancelRequest(iv);
        Picasso.with(mContext).load(R.drawable.default_profile).transform(new CircleTransform()).into(iv);
        Picasso.with(mContext).load(card.get(position).getpImage()).transform(new CircleTransform()).into(iv);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(fname + " " + lname);
//
        TextView comp = (TextView) view.findViewById(R.id.company);
        comp.setText(companyName);

        TextView noteV = (TextView) view.findViewById(R.id.note);
        if(note=="") noteV.setVisibility(View.GONE);
        else noteV.setText(note);
        return view;
    }
    @Override
    public void notifyDataSetChanged() {
        //do your sorting here

        super.notifyDataSetChanged();
    }
}
