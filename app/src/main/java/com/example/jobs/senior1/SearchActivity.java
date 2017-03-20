package com.example.jobs.senior1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    public ArrayList<ContactListResponse> list = new ArrayList<ContactListResponse>();
    ArrayList<String> card = new ArrayList<String>();
    ArrayList<String> acc = new ArrayList<String>();
    ArrayList<String> list2;
    private static final String TAG = "SearchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search);
        Log.d(TAG,"Search size: "+ContactList.cardList.size());

        findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
        final EditText editText = (EditText) findViewById(R.id.keyword);
        findViewById(R.id.searchBtn).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                final String keyword= (editText.getText()+"").toLowerCase();
                Log.d(TAG,"Search");
                Log.d(TAG,keyword);

                ArrayList<CardListResponse> finalList = new ArrayList<CardListResponse>();
                for(int i =0;i<ContactList.cardList.size();i++){
                    StringBuilder builder = new StringBuilder();
                    for(String s : ContactList.cardList.get(i).getHashTags()) {
                        builder.append(s+" ");
                    }
                    if(ContactList.cardList.get(i).getComp().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getFname().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getLname().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getPosition().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getEmail().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getNote().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getExchangeLocation().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getExpand().toString().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            ContactList.cardList.get(i).getEmail().toLowerCase().indexOf(keyword.toLowerCase())>-1 ||
                            builder.indexOf(keyword.toLowerCase())>-1||
                            ContactList.cardList.get(i).getTelephoneNumber().toLowerCase().indexOf(keyword.toLowerCase())>-1
                            ){
                        finalList.add(ContactList.cardList.get(i));
                        Log.d(TAG,"Add"+ContactList.cardList.get(i).getHashTags().toString() );
                    }
                }
                final ContactListAdapter adapter = new ContactListAdapter(getApplicationContext(), finalList);
                ListView listView = (ListView) findViewById(R.id.listView1);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                        Intent intent = new Intent(SearchActivity.this, CardDetailActivity.class);
                        intent.putExtra("cardId", adapter.getId(arg2));
                        intent.putExtra("from", "contact");
                        Log.d(TAG,arg2+""+adapter.getId(arg2));
                        startActivity(intent);
                    }
                });
                Log.d(TAG, "done");

                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

            }});
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("manage", "true");
        startActivity(intent);
    }
}
