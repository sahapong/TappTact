package com.example.jobs.senior1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jobs on 16-Jan-17.
 */

public class EditDetailActivity extends AppCompatActivity {
    private ListView listView;
    private String id,FilePath;
    private String imgPath;
    private String query;
    TableLayout table;
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=mixtio;AccountKey=UtfZZRfSiPqJ6pD18tYVvzn8+fTFJWdAvwV8mefzDmgCsoQkusXJUtIox5MSLGEtIay7kXm/USCIIDe38ApfmQ==;";

    private static final int SELECTED_IMAGE=1;
    String[] name;
    String[] opt = {"BusinessCardFname","BusinessCardLname","BusinessCardEmail","CompanyName","Occupation","TelephoneNumber","Note"};
    final Context context = this;
    private boolean isUpload = true;
    private static final String TAG = "EditDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        name =  new String[]{"First Name","Last Name","Email","Company Name","Occupation",getString(R.string.mobile),"Purpose"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        FloatingActionButton btnadd = (FloatingActionButton) findViewById(R.id.btnAdd);
//        btnUpdate.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                //Do stuff here
//
//                if(isUpload)
//                    uploadPicture();
//                else try {
//                    updateQuery();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        btnadd.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater li = LayoutInflater.from(context);
                final View promptsView = li.inflate(R.layout.dialog_add_card_info, null);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText value1 = (EditText) promptsView
                        .findViewById(R.id.value1);
                final EditText value2 = (EditText) promptsView
                        .findViewById(R.id.value2);
                Spinner dropdown = (Spinner)promptsView.findViewById(R.id.spinner1);
                final String[] items = new String[]{getString(R.string.mobile),"Email","Website","Facebook", "Twitter", "Line", "Other"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        Object item = adapterView.getItemAtPosition(position);
                        switch (position) {
                            case 0:
                                value1.setText(items[0]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the first item gets selected
                                break;
                            case 1:
                                value1.setText(items[1]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the second item gets selected
                                break;
                            case 2:
                                value1.setText(items[2]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the thrid item gets selected
                                break;
                            case 3:
                                value1.setText(items[3]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the second item gets selected
                                break;
                            case 4:
                                value1.setText(items[4]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the thrid item gets selected
                                break;
                            case 5:
                                value1.setText(items[5]);
                                promptsView.findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
                                value1.setVisibility(View.INVISIBLE);
                                // Whatever you want to happen when the thrid item gets selected
                                break;
                            case 6:
                                value1.setText("");
                                // Whatever you want to happen when the thrid item gets selected
                                promptsView.findViewById(R.id.textView1).setVisibility(View.VISIBLE);
                                value1.setVisibility(View.VISIBLE);
                                break;


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // TODO Auto-generated method stub

                        value1.setHint("Type you field name");
                    }
                });

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Add information")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        TextView txtView = new TextView(getApplicationContext());
                                        txtView.setText(value1.getText());
                                        txtView.setTextColor(getResources().getColor(R.color.colorAccent));
                                        EditText edtView = new EditText(getApplicationContext());
                                        String valueJson = "";
                                        edtView.setText(value2.getText());
                                        edtView.setTextColor(getResources().getColor(R.color.grey));
                                        TableRow row=new TableRow(getApplicationContext());
                                        row.addView(txtView);
                                        table.addView(row);
                                        row = new TableRow(getApplicationContext());
                                        row.addView(edtView);
                                        table.addView(row);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        loadCard();
    }
    private void uploadPicture(){

        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        findViewById(R.id.layout).setVisibility(View.INVISIBLE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        storeImageInBlobStorage( timeStamp);

    }
    private void updateQuery() throws JSONException {
        query= "{";

//                for(int i=0;i<2;i++) {
//                    TableRow row = (TableRow) table.getChildAt(i);
//                    TextView txt = (TextView) row.getChildAt(0); // get child index on particular row
//                    EditText editText = (EditText) row.getChildAt(1);
//                    //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
//                    query += txt.getText() + ": ObjectId(\"" + editText.getText() + "\"),";
//                }
        for(int i=0;i<name.length*2;i+=2)
        {
            TableRow row = (TableRow)table.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row

            row = (TableRow)table.getChildAt(i+1);
            EditText editText =(EditText) row.getChildAt(0);
            if ((editText.getText() + "").replaceAll(" ", "") == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
            if((editText.getText()+"").charAt(0)=='[')
                query+=opt[i/2]+": "+editText.getText()+",";
                //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
            else query+=opt[i/2]+": "+"\""+editText.getText()+"\",";
        }

        query+= "Expand : [ ";
        for(int i=name.length*2;i<table.getChildCount()-2;i+=2)
        {
            query+="{";
            TableRow row = (TableRow)table.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row

            row = (TableRow)table.getChildAt(i+1);
            EditText editText =(EditText) row.getChildAt(0);
            if ((editText.getText() + "").replaceAll(" ", "") == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
            if((editText.getText()+"").charAt(0)=='[')
                query+=txt.getText()+": "+editText.getText()+",";
                //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
            else query+="\""+txt.getText() + "\": "+"\""+editText.getText()+"\"";
            query+="},";
        }
        query += "{";
        if(table.getChildCount()>name.length*2) {
            TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 2);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row
            row = (TableRow) table.getChildAt(table.getChildCount() - 1);

            EditText editText = (EditText) row.getChildAt(0);
//                Log.i("Update", txt.getText()+": "+"\""+editText.getText()+"\"");
            if ((editText.getText() + "").replaceAll(" ", "") == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
            query += "\""+txt.getText() + "\": " + "\"" + editText.getText() + "\"";
        }
        query += "}],";
        uploadPicture();

    }


    public void AddImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECTED_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case SELECTED_IMAGE:
                if(resultCode==RESULT_OK){
                    Uri uri = data.getData();
                    String get = uri.getPath();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();
                    int colIndex = cursor.getColumnIndex(projection[0]);
                    FilePath = cursor.getString(colIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
                    Drawable drawable = new BitmapDrawable(this.getResources(),bitmap);
                    ImageView img = (ImageView)findViewById(R.id.cardImage);
                    img.setImageBitmap(bitmap);
                    Log.d(TAG,FilePath);
                    isUpload=true;
                }
        }
    }
    protected void storeImageInBlobStorage(final String time){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast toast = Toast.makeText( getApplicationContext(), "Update success", Toast.LENGTH_SHORT);
                toast.show();
                try {
                    query += "CardImage: " + "\"" + imgPath + "\"";
                    query += "}";
                    // Log.i("Update", query.substring(266));
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Map<String, Object> data = new HashMap<>();
                    Log.i(TAG,"query"+query);
                    SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
                    id = getIntent().getStringExtra("cardId");
                    data.put("bizCardId", id);
                    JSONObject jsonObj = new JSONObject(query);

                    Log.i(TAG,"Response"+jsonObj);
                    data.put("docQuery", jsonObj);
                    Call<ResponseBody> call = apiService.updateCard(data);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {

                            Log.i(TAG,"Response"+response.body());
                            Toast toast = Toast.makeText( getApplicationContext(), "Update Complete", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(EditDetailActivity.this, CardDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("cardId", id);
                            intent.putExtra("from", "contact");
                            intent.putExtra("isOwner", "true");
                            intent.putExtra("update", "true");

                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody>call, Throwable t) {
                            // Log error here since request failed
                            Log.d(TAG, t.getMessage()+ "");
                            Toast toast = Toast.makeText( getApplicationContext(), "Create Fail", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    // Retrieve storage account from connection-string.
                    CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

                    // Create the blob client.
                    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

                    // Get a reference to a container.
                    // The container name must be lower case
                    CloudBlobContainer container = blobClient.getContainerReference("cardmixtio");

                    SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
                    CloudBlockBlob blob = container.getBlockBlobReference(sp.getString("accId","0")+time+".jpg");
                    File source = new File(FilePath);
                    blob.upload(new java.io.FileInputStream(source), source.length());
                    imgPath="https://mixtio.blob.core.windows.net/cardmixtio/"+sp.getString("accId","0")+time+".jpg";

                } catch (final Exception e){
                    Log.e("Error", e.toString());
                }

                return null;
            }
        };
        task.execute();

    }

    private void loadCard(){
        final Intent intent = getIntent();
        id = intent.getStringExtra("cardId");
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Map<String, String> data = new HashMap<>();
        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        data.put("cardId",id);
        data.put("accId",sp.getString("accId","0"));
        data.put("view","false");
        Call<ResponseBody> call = apiService.getCardDetail(data);

        // Log.d("AccountResponse", "email: " +email.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody>call, Response<ResponseBody> response) {
                try {
                    String x = response.body().string();
                    table=(TableLayout) findViewById(R.id.myTableLayout);
//                    Log.d("CardDetailRes", x );
                    JSONObject json = new JSONObject(x.substring(1,x.length()-1));
//
                    ImageView iv = (ImageView)findViewById(R.id.cardImage);
                    imgPath = json.optString("CardImage");
                    Picasso.with(getApplicationContext()).load(imgPath).into(iv);
                  //  getSupportActionBar().setTitle( json.optString(opt[6]));
                    for(int i =0;i<name.length;i++) {
                        TextView txtView = new TextView(getApplicationContext());
                        txtView.setText(name[i]);
                        EditText edtView = new EditText(getApplicationContext());
                        String valueJson = json.optString(opt[i]);
                        edtView.setText("" + valueJson);
                        ;
                        edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        edtView.setTextColor(getResources().getColor(R.color.grey));
                        txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        txtView.setTextColor(getResources().getColor(R.color.colorAccent));
                        TableRow row=new TableRow(getApplicationContext());
                        row.addView(txtView);
                        table.addView(row);
                        row = new TableRow(getApplicationContext());
                        row.addView(edtView);
                        table.addView(row);
                    }

                    JSONArray the_json_array = json.getJSONArray("Expand");

                    table=(TableLayout) findViewById(R.id.myTableLayout);

                    x= json.optString("Expand")+"";
                    x="{"+x.substring(1,x.length()-1).replace("{","").replace("}","")+"}";

                    json = new JSONObject(x);
                    Iterator iterator = json.keys();
                    Log.d(TAG,"Expand size:"+the_json_array.length());
//                    String key = (String)iterator.next();
                    while(iterator.hasNext())
                    {
                        TableRow row=new TableRow(getApplicationContext());
                        String key = (String)iterator.next();
                       String  valueJson = json.optString(key);
                       TextView  txtView =new TextView(getApplicationContext());
                        txtView.setText(""+key);
                       EditText  edtView=new EditText(getApplicationContext());;
                        edtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        edtView.setTextColor(getResources().getColor(R.color.grey));
                        txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        txtView.setTextColor(getResources().getColor(R.color.colorAccent));
                        edtView.setText(""+valueJson);
                        row.addView(txtView);
                        table.addView(row);
                        row=new TableRow(getApplicationContext());
                        row.addView(edtView);
                        table.addView(row);
                    }
                    findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                    findViewById(R.id.layout).setVisibility(View.VISIBLE);
//
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"io"+e.toString());
                    findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                    findViewById(R.id.layout).setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG,"json:"+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                loadCard();
                Toast toast = Toast.makeText( getApplicationContext(), "Sever don't response ... Please wait", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
//        Map<String, String> data = new HashMap<>();
//        data.put("cardId",id);
//        data.put("view","true");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signup, menu);
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
        if (idd == R.id.save) {
            if(isUpload) {
                try {
                    updateQuery();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast toast = Toast.makeText( getApplicationContext(), "Please upload the card image", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean checkInput(String type, String value) {
        Log.d(TAG, "Check input:" + type + " " + value);
        if (type.toLowerCase().compareTo(getString(R.string.mobile))==0
                &&!FormatChecker.isNumeric(value)
                ) {
            Log.d(TAG, "Check input: Wrong phone number ");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else if (type.toLowerCase().compareTo("first name")==0
                &&!FormatChecker.isName(value)
                ) {
            Log.d(TAG, "Check input: Wrong phone number ");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_name), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (type.toLowerCase().compareTo("last name")==0
                &&!FormatChecker.isName(value)
                ) {
            Log.d(TAG, "Check input: Wrong phone number ");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_name), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }  else if (type.toLowerCase().compareTo("email")==0
                &&!FormatChecker.isEmailValid(value)
                ) {
            Log.d(TAG, "Check input: Wrong phone number ");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_email), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }  else if (type.toLowerCase().compareTo("website")==0
                &&!FormatChecker.isWebsite(value)
                ) {
            Log.d(TAG, "Check input: Wrong phone number ");
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_website), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else return true;
    }
}
