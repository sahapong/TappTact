package com.example.jobs.senior1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticActivity extends AppCompatActivity {
    private String id;
    private String imgPath;
    private static final String TAG = "StatisticActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        matchView();
    }
    void matchView(){

        final Intent intent = getIntent();
        id = intent.getStringExtra("cardId");
        imgPath = intent.getStringExtra("img");
        ImageView iv = (ImageView)findViewById(R.id.cardImage);
        Picasso.with(getApplicationContext()).load(imgPath).into(iv);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        final TextView viewCountTotal = (TextView) findViewById(R.id.totalView);
        final TextView viewCount = (TextView) findViewById(R.id.weeklyView);
        final TextView sentCount = (TextView) findViewById(R.id.weeklySent);
        final TextView sentCountTotal = (TextView) findViewById(R.id.totalSent);
        Call<ViewResponse> call2 = apiService.getView(id);

        // Log.d("AccountResponse", "email: " +email.getText().toString());
        call2.enqueue(new Callback<ViewResponse>() {
            @Override
            public void onResponse(Call<ViewResponse>call, Response<ViewResponse> response) {

                ViewResponse viewResponses = response.body();
                Log.d(TAG,"View: "+response.body().getTotalSent());
                viewCountTotal.setText(viewResponses.getTotalView()+"");
//                viewCount.setText(viewResponses.getWeeklyView()+"");
//                sentCount.setText(viewResponses.getWeeklySent()+"");
                viewCount.setText("5");
                sentCount.setText("2");
                sentCountTotal.setText(viewResponses.getTotalSent()+"");

            }

            @Override
            public void onFailure(Call<ViewResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "err Rest:"+t.toString());
                Log.e(TAG, "Reload");
                //loadCard();

                Toast toast = Toast.makeText( getApplicationContext(), "Sever don't response ... Please wait", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int idd = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(idd == android.R.id.home){
            //NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
