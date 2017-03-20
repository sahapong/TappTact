package com.example.jobs.senior1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseCardActivity extends AppCompatActivity {
    String accIdTarget;
    String token;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);
        loadCardList();

        Intent intent = getIntent();
        accIdTarget = intent.getStringExtra("targetAccId");
        token = intent.getStringExtra("token");
        name = intent.getStringExtra("name");
        TextView txt = (TextView) findViewById(R.id.sendto);
        txt.setText("Selected "+name);
    }
    private void loadCardList(){
        final SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<CardListResponse>> call = apiService.getCardList(sp.getString("accId","0"));
        call.enqueue(new Callback<List<CardListResponse>>() {
            @Override
            public void onResponse(Call<List<CardListResponse>>call, Response<List<CardListResponse>> response) {
                final List<CardListResponse> list = response.body();
                Log.d("rest", "cardList: " +list.size());

                final CardListAdapter adapter = new CardListAdapter(getApplicationContext(), list);
                ListView listView = (ListView) findViewById(R.id.listView1);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {

                        Log.d("Sending", accIdTarget+" "+adapter.getId(arg2));
                        ApiInterface apiService2 =
                                ApiClient.getClient().create(ApiInterface.class);

                        Map<String, String> data = new HashMap<>();

                        String id= accIdTarget.replaceAll("-","");
                        id = id.substring(8);
                        data.put("targetAccId",id);
                        data.put("token",token);
                        data.put("bizCard", adapter.getId(arg2));
                        data.put("accId",sp.getString("accId","0"));
                        data.put("name",sp.getString("fname","0")+" "+sp.getString("lname","0"));
                        data.put("image",sp.getString("image","0"));
                        Log.d("Sending",accIdTarget+" "+ adapter.getId(arg2)+" "+sp.getString("accId","0") );
                        Call<ResponseBody> call2 = apiService2.sendCard(data);
                        call2.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                                Log.d("Sending", response.body()+"");
                                ExchangeActivity.beaconTransmitter.stopAdvertising();
                                Toast toast = Toast.makeText( getApplicationContext(), "Send card to "+name+" Successful", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(ChooseCardActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody>call, Throwable t) {
                                // Log error here since request failed
                                Log.d("Sending", "Insert");
                                ExchangeActivity.beaconTransmitter.stopAdvertising();
                                Toast toast = Toast.makeText( getApplicationContext(), "Send card to "+name+" Fail. Please try again later", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(ChooseCardActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
                Log.d("rest", "done");
            }

            @Override
            public void onFailure(Call<List<CardListResponse>>call, Throwable t) {
                // Log error here since request failed
                Log.e("rest", t.toString());
            }
        });
    }
}
