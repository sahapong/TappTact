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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jobs on 16-Jan-17.
 */

public class CreateCardActivity extends AppCompatActivity {
    private ListView listView;
    private String id, FilePath;
    private String imgPath;
    private String query;
    private boolean isUpload = false;
    TableLayout table;
    SharedPreferences sp;
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=mixtio;AccountKey=UtfZZRfSiPqJ6pD18tYVvzn8+fTFJWdAvwV8mefzDmgCsoQkusXJUtIox5MSLGEtIay7kXm/USCIIDe38ApfmQ==;";

    String[] name ;
    String[] opt = {"BusinessCardFname", "BusinessCardLname", "BusinessCardEmail", "CompanyName", "Occupation", "TelephoneNumber", "Note"};

    final Context context = this;
    private static final String TAG = "CreateCardActivity";
    private static final int SELECTED_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        name = new String[]{"First Name", "Last Name", "Email", "Company Name", "Occupation", getString(R.string.mobile), "Purpose"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//          Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        FloatingActionButton btnadd = (FloatingActionButton) findViewById(R.id.btnAdd);
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
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
                Spinner dropdown = (Spinner) promptsView.findViewById(R.id.spinner1);
                final String[] items = new String[]{getString(R.string.mobile),"Email","Website","Facebook", "Twitter", "Line", "Other"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCardActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
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
                                    public void onClick(DialogInterface dialog, int id) {

                                        TextView txtView = new TextView(getApplicationContext());
                                        txtView.setText(value1.getText());
                                        txtView.setTextColor(getResources().getColor(R.color.black));
                                        EditText edtView = new EditText(getApplicationContext());
                                        String valueJson = "";
                                        edtView.setText(value2.getText());
                                        edtView.setTextColor(getResources().getColor(R.color.colorAccent));
                                        TableRow row = new TableRow(getApplicationContext());
                                        row.addView(txtView);
                                        table.addView(row);
                                        row = new TableRow(getApplicationContext());
                                        row.addView(edtView);
                                        table.addView(row);
                                        View view = getCurrentFocus();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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

    private void uploadPicture() {

        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        findViewById(R.id.layout).setVisibility(View.INVISIBLE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        storeImageInBlobStorage(timeStamp);

    }

    private void updateQuery() throws JSONException {
        query = "{";

//                for(int i=0;i<2;i++) {
//                    TableRow row = (TableRow) table.getChildAt(i);
//                    TextView txt = (TextView) row.getChildAt(0); // get child index on particular row
//                    EditText editText = (EditText) row.getChildAt(1);
//                    //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
//                    query += txt.getText() + ": ObjectId(\"" + editText.getText() + "\"),";
//                }
        for (int i = 0; i < name.length * 2; i += 2) {
            TableRow row = (TableRow) table.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row

            row = (TableRow) table.getChildAt(i + 1);
            EditText editText = (EditText) row.getChildAt(0);
            if (editText.getText() + "" == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
            if ((editText.getText() + "").charAt(0) == '[')
                query += opt[i / 2] + ": " + editText.getText() + ",";
                //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
            else query += opt[i / 2] + ": " + "\"" + editText.getText() + "\",";
        }

        query += "Expand : [ ";
        for (int i = name.length * 2; i < table.getChildCount() - 2; i += 2) {
            query += "{";
            TableRow row = (TableRow) table.getChildAt(i);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row

            row = (TableRow) table.getChildAt(i + 1);
            EditText editText = (EditText) row.getChildAt(0);
            if (editText.getText() + "" == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
            if ((editText.getText() + "").charAt(0) == '[')
                query += txt.getText() + ": " + editText.getText() + ",";
                //      Log.i("Update", txt.getText()+": "+"ObjectId(\""+editText.getText()+"\"),");
            else query += "\"" + txt.getText() + "\": " + "\"" + editText.getText() + "\"";
            query += "},";
        }
        query += "{";
        if (table.getChildCount() > name.length * 2) {
            TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 2);
            TextView txt = (TextView) row.getChildAt(0); // get child index on particular row
            row = (TableRow) table.getChildAt(table.getChildCount() - 1);
            EditText editText = (EditText) row.getChildAt(0);
            if ((editText.getText() + "").replaceAll(" ", "") == "") {

                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.filled_miss), Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                if(!checkInput(txt.getText() + "", editText.getText() + "")) return;
            }
//                Log.i("Update", txt.getText()+": "+"\""+editText.getText()+"\"");
            query += "\"" + txt.getText() + "\": " + "\"" + editText.getText() + "\"";
        }
        query += "}],";
        query += "AccountProfilePicture: " + "\"" + sp.getString("image", "0") + "\",";
        if (imgPath == "") {

            Toast toast = Toast.makeText(getApplicationContext(), "Please upload the card image", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        uploadPicture();
        // Log.i("Update", query.substring(266));
    }

    private void loadCard() {
        String[] hint = {sp.getString("fname", "0"), sp.getString("lname", "0"), sp.getString("email", "0")
                , sp.getString("comp", "0"), sp.getString("occu", "0")};


        table = (TableLayout) findViewById(R.id.myTableLayout);
//                 getSupportActionBar().setTitle( json.optString(opt[0])+" "+ json.optString(opt[1]));
        for (int i = 0; i < name.length - 2; i++) {
            TextView txtView = new TextView(getApplicationContext());
            txtView.setText(name[i]);
            txtView.setTextColor(getResources().getColor(R.color.black));
            EditText edtView = new EditText(getApplicationContext());
            String valueJson = "";
            edtView.setText(hint[i]);
            edtView.setTextColor(getResources().getColor(R.color.colorAccent));
            edtView.setInputType(InputType.TYPE_CLASS_TEXT);
            edtView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            TableRow row = new TableRow(getApplicationContext());
            row.addView(txtView);
            table.addView(row);
            row = new TableRow(getApplicationContext());
            row.addView(edtView);
            table.addView(row);
        }
        TextView txtView = new TextView(getApplicationContext());
        txtView.setText(name[name.length - 2]);
        txtView.setTextColor(getResources().getColor(R.color.black));
        EditText edtView = new EditText(getApplicationContext());
        String valueJson = "";
//        edtView.setHint(R.string.this_card_for);
        edtView.setInputType(InputType.TYPE_CLASS_TEXT);
        edtView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtView.setTextColor(getResources().getColor(R.color.colorAccent));
        edtView.setHintTextColor(getResources().getColor(R.color.grey));
        ((TextView) (edtView)).setHint(R.string.add_phone);
        TableRow row = new TableRow(getApplicationContext());
        row.addView(txtView);
        table.addView(row);
        row = new TableRow(getApplicationContext());
        row.addView(edtView);
        table.addView(row);
        txtView = new TextView(getApplicationContext());
        txtView.setText(name[name.length - 1]);
        txtView.setTextColor(getResources().getColor(R.color.black));
        edtView = new EditText(getApplicationContext());
        valueJson = "";
//        edtView.setHint(R.string.this_card_for);
        edtView.setInputType(InputType.TYPE_CLASS_TEXT);
        edtView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtView.setTextColor(getResources().getColor(R.color.colorAccent));
        edtView.setHintTextColor(getResources().getColor(R.color.grey));
        ((TextView) (edtView)).setHint(R.string.add_note);
        row = new TableRow(getApplicationContext());
        row.addView(txtView);
        table.addView(row);
        row = new TableRow(getApplicationContext());
        row.addView(edtView);
        table.addView(row);

    }

    public void AddImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECTED_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String get = uri.getPath();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int colIndex = cursor.getColumnIndex(projection[0]);
                    FilePath = cursor.getString(colIndex);
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(FilePath);
                    Drawable drawable = new BitmapDrawable(this.getResources(), bitmap);
                    ImageView img = (ImageView) findViewById(R.id.cardImage);
                    img.setBackground(drawable);
                    Log.d(TAG, FilePath);
                    isUpload = true;
                }
        }
    }

    protected void storeImageInBlobStorage(final String time) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                query += "CardImage: " + "\"" + imgPath + "\"";
                query += "}";

                super.onPostExecute(aVoid);
                Toast toast = Toast.makeText(getApplicationContext(), "Upload success", Toast.LENGTH_SHORT);
                toast.show();
                try {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Map<String, Object> data = new HashMap<>();
                    Log.i(TAG, "query" + query);
                    id = sp.getString("accId", "0");
                    data.put("accId", id);
                    JSONObject jsonObj = new JSONObject(query);

                    Log.i(TAG, "Response" + jsonObj);
                    data.put("docQuery", jsonObj);
                    Call<ResponseBody> call = apiService.createCard(data);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            Log.i(TAG, "Response" + response.body());
                            Toast toast = Toast.makeText(getApplicationContext(), "Create Complete", Toast.LENGTH_SHORT);
                            toast.show();
                            ContactList.destroy();
                            Intent intent = new Intent(CreateCardActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("manage", "true");
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                            Log.d(TAG, t.getMessage() + "");
                            Toast toast = Toast.makeText(getApplicationContext(), "Create Fail", Toast.LENGTH_SHORT);
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
                    CloudBlockBlob blob = container.getBlockBlobReference(sp.getString("accId", "0") + time + ".jpg");
                    File source = new File(FilePath);
                    blob.upload(new java.io.FileInputStream(source), source.length());
                    imgPath = "https://mixtio.blob.core.windows.net/cardmixtio/" + sp.getString("accId", "0") + time + ".jpg";

                } catch (final Exception e) {
                    Log.e("Error", e.toString());
                }

                return null;
            }
        };
        task.execute();

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
            if (isUpload) {
                try {
                    updateQuery();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Please upload the card image", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkInput(String type, String value) {
        Log.d(TAG, "Check input:" + type + " " + value);
        if (type.toLowerCase().compareTo(getString(R.string.mobile).toLowerCase())==0
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
