package com.example.jobs.senior1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDetailActivity extends AppCompatActivity {

    private String id;
    private JSONObject json;
    TableLayout table;
    private String img;
    private String isUpdate;


    String[] name;
    String[] opt = {"BusinessCardFname", "BusinessCardLname", "CompanyName", "Occupation","TelephoneNumber","BusinessCardEmail","Note","HashTags"};
    private static final String TAG = "CardDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        name = new String[]{"First Name", "Last Name", "Company Name", "Occupation", getString(R.string.mobile), "Email","Note","HashTags"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadCard();
    }

    private void loadCard() {
        Log.d(TAG, "loadCard");
//        ImageView verify = (ImageView) findViewById(R.id.veri);
//        verify.setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN);
        final Intent intent = getIntent();
        id = intent.getStringExtra("cardId");
        isUpdate = intent.getStringExtra("update");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> data = new HashMap<>();

        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        String v;
        if(intent.getStringExtra("view")==null){
            v="true";
        }
        else v=intent.getStringExtra("view");
        data.put("cardId", id);
        data.put("accId", sp.getString("accId", "0"));
        data.put("view",v );
        Call<ResponseBody> call = apiService.getCardDetail(data);

        // Log.d("AccountResponse", "email: " +email.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    Log.d(TAG, "onResponse");
                    String x = response.body().string();
                    Log.d(TAG, x);
                    table = (TableLayout) findViewById(R.id.myTableLayout);
//                    Log.d("CardDetailRes", x );

                    json = new JSONObject(x.substring(1, x.length() - 1));
                    final Intent intent = getIntent();
                    String isOwner = intent.getStringExtra("isOwner");

                    if (isOwner != null) {
                        getSupportActionBar().setTitle("");
                    } else
                        getSupportActionBar().setTitle(json.optString(opt[0]) + " " + json.optString(opt[1]));
                    String imgPath = json.optString("AccountProfilePicture");

                    ImageView pf = (ImageView) findViewById(R.id.pf);
                    Picasso.with(getApplicationContext()).cancelRequest(pf);
                    Picasso.with(getApplicationContext()).load(imgPath).transform(new CircleTransform()).into(pf);
                    ImageView iv = (ImageView) findViewById(R.id.cardImage);
                    img =json.optString("CardImage");
                    Picasso.with(getApplicationContext()).load(json.optString("CardImage")).into(iv);
                    for (int i = 0; i < name.length; i++) {
                        if (name[i].compareTo(getString(R.string.mobile)) == 0)
                            setPhone("TelephoneNumber");
                        else if (name[i].compareTo("Email") == 0)
                            setLink("Email", "BusinessCardEmail", "mailto:", R.drawable.email);
                        else {
                            TextView txtView = new TextView(getApplicationContext());
                            txtView.setText(name[i]);
                            TextView edtView = new TextView(getApplicationContext());
                            String valueJson = json.optString(opt[i]);
                            edtView.setText("" + valueJson);
                            edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            edtView.setTextColor(getResources().getColor(R.color.grey));
                            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            txtView.setTextColor(getResources().getColor(R.color.colorAccent));
                            TableRow row = new TableRow(getApplicationContext());
                            row.addView(txtView);
                            table.addView(row);
                            row = new TableRow(getApplicationContext());
                            row.addView(edtView);
                            table.addView(row);
                            row = new TableRow(getApplicationContext());
                            View view = new View(getApplicationContext());
                            view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                            view.setBackground(getResources().getDrawable(R.color.grey));
//                            row.addView(view);
                            table.addView(view);
                        }
                    }
                    //Phone


                    JSONArray the_json_array = json.getJSONArray("Expand");

                    table = (TableLayout) findViewById(R.id.myTableLayout);
                    try{
                        String y= json.optString("Verify");
                        if(y!="")
                        findViewById(R.id.verify).setVisibility(View.VISIBLE);
                    }
                    catch (Exception e){

                    }
                    x = json.optString("Expand") + "";
                    x = "{" + x.substring(1, x.length() - 1).replace("{", "").replace("}", "") + "}";
                    json = new JSONObject(x);
                    Iterator iterator = json.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (key.compareTo(getString(R.string.mobile)) == 0)
                            setPhone("Phone");
                        else if (key.compareTo("Website") == 0)
                            setLink("Website", "Website", "", R.drawable.browser);
                        else if (key.compareTo("Email") == 0)
                            setLink("Email", "BusinessCardEmail", "mailto:", R.drawable.email);
                        else if (key.compareTo("Line") == 0)
                            setLink("Line", "Line", "http://line.me/ti/p/~", R.drawable.line);
                        else if (key.compareTo("Facebook") == 0)
                            setLink("Facebook", "Facebook", "https://www.facebook.com/", R.drawable.facebook);
                        else if (key.compareTo("Twitter") == 0)
                            setLink("Twitter", "Twitter", "https://twitter.com/", R.drawable.twitter);
                        else {
                            TableRow row = new TableRow(getApplicationContext());
                            String valueJson = json.optString(key);
                            TextView txtView = new TextView(getApplicationContext());
//                        txtView.setTextColor(getResources().getColor(R.color.black));

                            txtView.setText("" + key);
                            TextView edtView = new TextView(getApplicationContext());
                            edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            edtView.setTextColor(getResources().getColor(R.color.grey));
                            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                        txtView.setAutoLinkMask();
                            txtView.setTextColor(getResources().getColor(R.color.colorAccent));
                            edtView.setText("" + valueJson);
                            row.addView(txtView);
                            table.addView(row);
                            row = new TableRow(getApplicationContext());
                            row.addView(edtView);
                            table.addView(row);
                            row = new TableRow(getApplicationContext());
                            View view = new View(getApplicationContext());
                            view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                            view.setBackground(getResources().getDrawable(R.color.grey));
//                            row.addView(view);
                            table.addView(view);
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "err IO: " + e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "err JSON:" + e.getMessage());
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, "err Rest:" + t.toString());
                Log.e(TAG, "Reload");
                loadCard();

                Toast toast = Toast.makeText(getApplicationContext(), "Sever don't response ... Please wait", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    private void setPhone(String phone) throws JSONException {


        TextView txtView = new TextView(getApplicationContext());
        txtView.setText(getString(R.string.mobile));
        TextView edtView = new TextView(getApplicationContext());
        final String valueJson = json.optString(phone);
        edtView.setText("" + valueJson);
        edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        edtView.setTextColor(getResources().getColor(R.color.grey));
        txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtView.setTextColor(getResources().getColor(R.color.colorAccent));

        TableRow row = new TableRow(getApplicationContext());


        row.addView(txtView);
//        row.addView(iv,vg);

        table.addView(row);
        row = new TableRow(getApplicationContext());
        row.addView(edtView);
        ;
        row.setBackground(getResources().getDrawable(R.drawable.background));
        row.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + valueJson));
                startActivity(callIntent);
            }
        });
        ImageView iv = new ImageView(getApplicationContext());
        iv.setBackground(getResources().getDrawable(R.drawable.phone));
        row.addView(iv);
        table.addView(row);
        row = new TableRow(getApplicationContext());
        View view = new View(getApplicationContext());
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
        view.setBackground(getResources().getDrawable(R.color.grey));
//        row.addView(view);
        table.addView(view);
    }

    private void setLink(String name, String value, final String link, int icon) throws JSONException {
        TextView txtView = new TextView(getApplicationContext());
        txtView.setText(name);
        TextView edtView = new TextView(getApplicationContext());
        final String valueJson = json.optString(value);
        edtView.setText("" + valueJson);
        edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        edtView.setTextColor(getResources().getColor(R.color.grey));
        txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        txtView.setTextColor(getResources().getColor(R.color.colorAccent));

        TableRow row = new TableRow(getApplicationContext());
        row.addView(txtView);
        table.addView(row);
        row = new TableRow(getApplicationContext());
        row.setBackground(getResources().getDrawable(R.drawable.background));
        row.addView(edtView);

//        ImageView line = new ImageView(getApplicationContext());
//        line.setBackground(getResources().getDrawable(R.color.grey));
//        row.addView(line);

        ImageView iv = new ImageView(getApplicationContext());
        iv.setBackground(getResources().getDrawable(icon));
//        TableRow.LayoutParams vg = new TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT ,TableRow.LayoutParams.WRAP_CONTENT);
        row.addView(iv);
        ;
        row.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_WEB_SEARCH);
//                callIntent.setData(Uri.parse(valueJson));
                callIntent.putExtra(SearchManager.QUERY, link + valueJson);
                startActivity(callIntent);
            }
        });
        table.addView(row);
        row = new TableRow(getApplicationContext());
        View view = new View(getApplicationContext());
        view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
        view.setBackground(getResources().getDrawable(R.color.grey));
//        row.addView(view);
        table.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        String isOwner;
        final Intent intent = getIntent();
        isOwner = intent.getStringExtra("isOwner");

        if (isOwner != null) {
            inflater.inflate(R.menu.manage, menu);
        } else inflater.inflate(R.menu.no_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int idd = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idd == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            onBackPressed();
            return true;
        }
        if (idd == R.id.stat) {
            // refresh();

            Intent intent = new Intent(CardDetailActivity.this, StatisticActivity.class);
            intent.putExtra("cardId", id);
            intent.putExtra("img", img);
            startActivity(intent);
            return true;
        }
        if (idd == R.id.share) {
            // refresh();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "You can view the card at http://www.tapptact.com/ViewCardDetail.php?cardId=" + id);
            sendIntent.setType("text/plain");
            if (sendIntent.resolveActivity(getPackageManager()) != null)
                startActivity(Intent.createChooser(sendIntent, "Share"));
            else startActivity(sendIntent);
            return true;
        }
        if (idd == R.id.edit) {
            // refresh();

            Intent intent = new Intent(CardDetailActivity.this, EditDetailActivity.class);
            intent.putExtra("cardId", id);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isUpdate==null)
        super.onBackPressed();
else {
            Intent intent = new Intent(CardDetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("manage", "true");
            startActivity(intent);
        }
    }
}
