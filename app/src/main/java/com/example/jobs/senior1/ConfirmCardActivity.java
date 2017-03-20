package com.example.jobs.senior1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.id.list;

public class ConfirmCardActivity extends AppCompatActivity {


    private static final String TAG = "ConfirmCardActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_card);
        loadCardList();
        Button btnAccept = (Button) findViewById(R.id.accept);
        btnAccept.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                confirmCard("Accept");
            }
        });
        Button btnReject = (Button) findViewById(R.id.decline);
        btnReject.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                confirmCard("Reject");
            }
        });


    }
    private void loadCardList(){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Intent intent = getIntent();
        Call<CardListResponse> call = apiService.getCardDetail2(intent.getStringExtra("cardId"));
        call.enqueue(new Callback<CardListResponse>() {
            @Override
            public void onResponse(Call<CardListResponse>call, Response<CardListResponse> response) {
//                final CardListResponse> list = response.body();
                CardListResponse card = response.body();
                Log.d(TAG, "Fname: " +card.getFname());
                Log.d(TAG, "Lname: " +card.getLname());
                TextView fname = (TextView) findViewById(R.id.fname);
                TextView lname = (TextView) findViewById(R.id.lname);
                TextView company = (TextView) findViewById(R.id.company);
                TextView position = (TextView) findViewById(R.id.postion);

                String imgPath = card.getpImage();

                ImageView pf = (ImageView) findViewById(R.id.pf);
                Picasso.with(getApplicationContext()).cancelRequest(pf);
                Picasso.with(getApplicationContext()).load(imgPath).transform(new CircleTransform()).into(pf);
                ImageView iv = (ImageView) findViewById(R.id.cardImage);
                Picasso.with(getApplicationContext()).load(card.getImage()).into(iv);
//                final CardListAdapter adapter = new CardListAdapter(getApplicationContext(), list);
//                ListView listView = (ListView) findViewById(R.id.listView1);
//                listView.setAdapter(adapter);
                fname.setText(card.getFname());
                lname.setText(card.getLname());
                position.setText(card.getPosition());
                company.setText(card.getComp());

            }

            @Override
            public void onFailure(Call<CardListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
    private void confirmCard(final String status){
        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        Log.d(TAG, "done");
        Intent intent = getIntent();
        Map<String, Object> data = new HashMap<>();
        EditText hashTag = (EditText) findViewById(R.id.hashtag) ;
        EditText note = (EditText) findViewById(R.id.note);

//        String[] x = (hashTag.getText()+"").split(",");
        data.put("targetAccId", sp.getString("accId","0"));
        data.put("bizCard", intent.getStringExtra("cardId"));
        data.put("confirm", status);
        data.put("note", note.getText()+"");
        data.put("hashtag",(hashTag.getText()+""));
//        for(int i = 0 ;i<x.length;i++)
//            Log.d(TAG, x[i]);
//                data.put("targetAccId", mPasswordView.getText().toString());
        ApiInterface apiService2 =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call2 = apiService2.confirmCard(data);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
//
                try {
                    Log.d(TAG,response.body().string()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText( getApplicationContext(), status , Toast.LENGTH_SHORT);
                toast.show();
                ContactList.destroy();
                Intent intent = new Intent(ConfirmCardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());


                Toast toast = Toast.makeText( getApplicationContext(), status +"Failed" , Toast.LENGTH_SHORT);
                toast.show();
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
}
