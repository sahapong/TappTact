package com.example.jobs.senior1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    private String FilePath;
    private String imgPath = "";
    private SharedPreferences sp;
    private static final int SELECTED_IMAGE = 1;
    private boolean isUpload;
    private EditText mPasswordView;
    private AutoCompleteTextView mEmailView, fnameView, lnameView, companyView, OccupationView;
    private static final String TAG = "SignUpActivity";
    private static final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=mixtio;AccountKey=UtfZZRfSiPqJ6pD18tYVvzn8+fTFJWdAvwV8mefzDmgCsoQkusXJUtIox5MSLGEtIay7kXm/USCIIDe38ApfmQ==;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_profile);
        isUpload = false;
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        fnameView = (AutoCompleteTextView) findViewById(R.id.fname);
        lnameView = (AutoCompleteTextView) findViewById(R.id.lname);
        companyView = (AutoCompleteTextView) findViewById(R.id.company);
//        siteView = (AutoCompleteTextView) findViewById(R.id.website);
        OccupationView = (AutoCompleteTextView) findViewById(R.id.occupation);
        sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        try{
            mPasswordView.setText(sp.getString("pass","0"));
            mEmailView.setText(sp.getString("email","0"));
            fnameView.setText(sp.getString("fname","0"));
            lnameView.setText(sp.getString("lname","0"));
            companyView.setText(sp.getString("comp","0"));
            OccupationView.setText(sp.getString("occu","0"));
//            siteView.setText(sp.getString("site","0"));
            imgPath = sp.getString("image","0");
            ImageView iv = (ImageView) findViewById(R.id.profile);
            iv.setBackground(getResources().getDrawable(R.drawable.ic_add_white_24dp));
            Picasso.with(getApplicationContext()).load(sp.getString("image","0")).transform(new CircleTransform()).into(iv);
            isUpload=true;
        }
        catch (Exception e){

        }
    }  private void signUp() {

        if (!isUpload) {

            Toast toast = Toast.makeText(getApplicationContext(), "Please upload the card image", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            attempt();
        }
    }

    private void createAccount() throws JSONException {

        String query = "{";
        query += "AccountEmail: " + "\"" + mEmailView.getText() + "\",";
        query += "Password: " + "\"" + mPasswordView.getText() + "\",";
        query += "FirstName: " + "\"" + fnameView.getText() + "\",";
        query += "LastName: " + "\"" + lnameView.getText() + "\",";
//        query += "Website: " + "\"" + siteView.getText() + "\",";
        query += "Company: " + "\"" + companyView.getText() + "\",";
        query += "Occupation: " + "\"" + OccupationView.getText() + "\",";
        query += "AccountProfilePicture: " + "\"" + imgPath + "\"}";
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Map<String, Object> data = new HashMap<>();
        Log.i(TAG, "query" + query);
        JSONObject jsonObj = new JSONObject(query);

        Log.i(TAG, "jObj" + jsonObj);
        data.put("docQuery", jsonObj);;
        data.put("id", sp.getString("accId","0"));
        Call<ResponseBody> call = apiService.updateAccount(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String res= "Error";
                try {
                    res = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Response" +res);
                if (res.compareTo("Succes") == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Update Complete", Toast.LENGTH_SHORT);
                    toast.show();
                    SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("fname", fnameView.getText()+"");
                    editor.putString("lname", lnameView.getText()+"");
                    editor.putString("email", mEmailView.getText()+"");
                    editor.putString("image", imgPath);
                    editor.putString("comp", companyView.getText()+"");
//                    editor.putString("site", siteView.getText()+"");
                    editor.putString("occu", OccupationView.getText()+"");
                    editor.putString("pass", mPasswordView.getText()+"");
                    editor.commit();
                    Intent intent = new Intent(UpdateProfile.this, LoginActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                    findViewById(R.id.layout).setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log error here since request failed
                Log.d(TAG, t.getMessage() + "");
                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);
                findViewById(R.id.layout).setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "Sign Up Account Fail", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }


    private void attempt() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        fnameView.setError(null);
        lnameView.setError(null);
        companyView.setError(null);
        OccupationView.setError(null);
        //set focus
//        mEmailView.requestFocus();
//        mPasswordView.requestFocus();
//        fnameView.requestFocus();
//        lnameView.requestFocus();
//        companyView.requestFocus();
//        OccupationView.requestFocus();
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String fname = fnameView.getText().toString();
        String lname = lnameView.getText().toString();
        String company = companyView.getText().toString();
        String Occupation = OccupationView.getText().toString();
//        String Site = siteView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(fname.replace(" ",""))){
            fnameView.setError(getString(R.string.error_field_required));
            focusView = fnameView;
            cancel = true;
        } else if (!FormatChecker.isName(fname)) {
            fnameView.setError(getString(R.string.error_invalid_name));
            focusView = fnameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lname)) {
            lnameView.setError(getString(R.string.error_field_required));
            focusView = lnameView;
            cancel = true;
        }else if (!FormatChecker.isName(lname)) {
            lnameView.setError(getString(R.string.error_invalid_name));
            focusView = lnameView;
            cancel = true;
        }
//        if (TextUtils.isEmpty(Site.replace(" ",""))) {
//            siteView.setError(getString(R.string.error_field_required));
//            focusView = siteView;
//            cancel = true;
//        } else if (!isWebsite(Site)) {
//            siteView.setError(getString(R.string.error_invalid_website));
//            focusView = siteView;
//            cancel = true;
//        }
        if (TextUtils.isEmpty(company.replace(" ",""))) {
            companyView.setError(getString(R.string.error_field_required));
            focusView = companyView;
            cancel = true;
        }
        if (TextUtils.isEmpty(Occupation.replace(" ","")))  {
            OccupationView.setError(getString(R.string.error_field_required));
            focusView = OccupationView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!FormatChecker.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password) && !FormatChecker.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

            findViewById(R.id.layout).setVisibility(View.INVISIBLE);

            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.wait), Toast.LENGTH_SHORT);
            toast.show();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            storeImageInBlobStorage(timeStamp);
        }
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
                    ImageView img = (ImageView) findViewById(R.id.profile);
                    img.setImageBitmap(getCroppedBitmap(bitmap));
                    img.setBackground(getResources().getDrawable(R.color.white));
                    isUpload = true;
                    Log.d(TAG, FilePath);
                }
        }
    }

    protected void storeImageInBlobStorage(final String time) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
//                Toast toast = Toast.makeText( getApplicationContext(), "Upload image success", Toast.LENGTH_SHORT);
//                toast.show();

                try {
                    createAccount();
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

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth()/2 , bitmap.getWidth()/2 ,
                bitmap.getWidth()/2 , paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
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
            // refresh();
            signUp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
