package com.example.jobs.senior1;

/**
 * Created by jobs on 09-Jan-17.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardListAdapter extends BaseAdapter {

    Context mContext;
    List<CardListResponse> list;

    public CardListAdapter(Context context, List<CardListResponse> list) {
        this.mContext= context;
        this.list = list;

    }


    @Override
    public int getCount() {
        return list.size();
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
        return list.get(position).getId();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater =
                (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.card_list_row, parent, false);

        ImageView iv = (ImageView)view.findViewById(R.id.smallCard);
        Picasso.with(mContext).load(list.get(position).getImage()).into(iv);
        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(list.get(position).getNote());
//
//        TextView comp = (TextView)view.findViewById(R.id.company);
//        comp.setText(list.get(position).getComp());

        return view;
    }
}
