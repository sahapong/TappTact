package com.example.jobs.senior1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxActivity extends AppCompatActivity {


    ArrayList<CardListResponse> cardList;
    private static final String TAG = "InboxActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadCardList();
    }

    private void loadCardList() {
        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        cardList = new ArrayList<>();
        Call<ArrayList<CardListResponse>> call = apiService.getInbox(sp.getString("accId", "0"));
        call.enqueue(new Callback<ArrayList<CardListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CardListResponse>> call, Response<ArrayList<CardListResponse>> response) {
                try {
                    cardList = (response.body());
                    final ContactListAdapter adapter = new ContactListAdapter(getApplicationContext(), cardList);
                    ListView listView = (ListView) findViewById(R.id.listView1);
                    listView.setAdapter(adapter);

                    findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);


                    if (cardList.size() == 0)
                        findViewById(R.id.noInbox).setVisibility(View.VISIBLE);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                            Intent intent = new Intent(InboxActivity.this, ConfirmCardActivity.class);
                            intent.putExtra("cardId", adapter.getId(arg2));
                            intent.putExtra("from", "contact");
                            Log.d(TAG, arg2 + "" + adapter.getId(arg2));
                            startActivity(intent);
                        }
                    });
                    Log.d(TAG, "done");
                }
                catch (Exception e){

                    Log.e(TAG, "Reload: "+TAG);
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.server_fail, Toast.LENGTH_SHORT);
                    toast.show();
                    loadCardList();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CardListResponse>> call, Throwable t) {
                Log.e(TAG, "Reload: "+TAG);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.server_fail, Toast.LENGTH_SHORT);
                toast.show();
                loadCardList();
            }
        });
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
        Intent intent = new Intent(InboxActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra("manage", "true");
        startActivity(intent);
    }
}
