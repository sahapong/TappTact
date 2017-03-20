package com.example.jobs.senior1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "LoginActivity";
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:(positions,email-address,firstName,lastName,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    public static final String PACKAGE = "com.example.jobs.senior1";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Button login_linkedin_btn = (Button) findViewById(R.id.linkedInBtn);
        login_linkedin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_linkedin();
            }
        });
        // Set up the login form.
//        generateHashkey();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        if (isLogin()) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.create_account);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(new AlphaAnimation(1F, 0.8F));
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(new AlphaAnimation(1F, 0.8F));


//                showProgress(true);
                attemptLogin();
//                login();
//                showProgress(false);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean isLogin() {

        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        return sp.contains("accId");
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !FormatChecker.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            login();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }

//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@") && email.contains(".com");
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void login() {

        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        findViewById(R.id.input).setVisibility(View.INVISIBLE);
//        findViewById(R.id.buttonGrp).setVisibility(View.INVISIBLE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> data = new HashMap<>();
        data.put("email", mEmailView.getText().toString().toLowerCase());
        data.put("pass", mPasswordView.getText().toString());
        data.put("fireBaseToken", FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "email: " + mEmailView.getText().toString());
        Log.d(TAG, "pass: " + mPasswordView.getText().toString());
        Log.d(TAG, "Token: " + FirebaseInstanceId.getInstance().getToken());
        Call<AccountResponse> call = apiService.getAccount(data);
        final String[] res = {"No"};
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                try {
                    AccountResponse ar = (AccountResponse)response.body();
                    Log.d(TAG, "Id: " + response.body());
                    Log.d(TAG, "Id: " + ar.getId());

                    Toast toast = Toast.makeText(getApplicationContext(), ar.getFname() + " " + ar.getLname(), Toast.LENGTH_SHORT);
                    toast.show();
                    SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("accId", ar.getId());
                    editor.putString("fname", ar.getFname());
                    editor.putString("lname", ar.getLname());
                    editor.putString("email", ar.getEmail());
                    editor.putString("image", ar.getImage());
                    editor.putString("comp", ar.getComp());
                    editor.putString("site", ar.getSite());
                    editor.putString("occu", ar.getOccu());
                    editor.putString("pass", ar.getPassword());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e){

                }
            }

            @Override
            public void onFailure( Call<AccountResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, call+"");

                findViewById(R.id.loadingPanel).setVisibility(View.INVISIBLE);

                findViewById(R.id.input).setVisibility(View.VISIBLE);
//                findViewById(R.id.buttonGrp).setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(),R.string.signIn_fail, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    public void login_linkedin(){
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

//                 Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onAuthError(LIAuthError error) {

//                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    // After complete authentication start new HomePage Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
//        Intent intent = new Intent(LoginActivity.this,UserProfile.class);
//        startActivity(intent);
        Log.d(TAG,"linkedin Sucess");
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        final String[] email = new String[1];
        final String[] fname = new String[1];
        final String[] lname = new String[1];
        final String[] pic = new String[1];
        final String[] pos = new String[1];
        final String[] comp = new String[1];
        final String[] site = new String[1];
        apiHelper.getRequest(LoginActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    Log.d(TAG,result.getResponseDataAsJson()+"");
                    email[0] =  result.getResponseDataAsJson().get("emailAddress")+"";
                    fname[0] =  result.getResponseDataAsJson().get("firstName")+"";
                    lname[0] =  result.getResponseDataAsJson().get("lastName")+"";
                    site[0] =  result.getResponseDataAsJson().get("publicProfileUrl")+"";
                    comp[0] = result.getResponseDataAsJson().getJSONObject("positions").getJSONArray("values").getJSONObject(0)
                            .getJSONObject("company").optString("name")+"";
  pos[0] = result.getResponseDataAsJson().getJSONObject("positions").getJSONArray("values").getJSONObject(0).optString("title")+"";
//
//          Log.d(TAG,comp[0]+" Company");
  pic[0] =  result.getResponseDataAsJson().get("pictureUrl")+"";

                    LISessionManager.getInstance(getApplicationContext()).clearSession();
//                    Toast.makeText(getApplicationContext(), email[0], Toast.LENGTH_LONG).show();
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseBody> call = apiService.isAccount(email[0]);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String res =response.body().string();
                                Log.d(TAG,"ResLink:"+res);
                                if(res.compareTo("Not found")==0){

                                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                                    intent.putExtra("email", email[0]);
                                    intent.putExtra("fname", fname[0]);
                                    intent.putExtra("lname", lname[0]);
                                    intent.putExtra("comp", comp[0]);
                                    intent.putExtra("pos", pos[0]);
                                    intent.putExtra("site", site[0]);
                                    intent.putExtra("pic", pic[0]);
                                    startActivity(intent);
                                }
                                else{
                                    mEmailView.setText(email[0]);
                                    Log.d(TAG,"Password:"+res);
                                    mPasswordView.setText(res);
                                    attemptLogin();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                Toast.makeText(getApplicationContext(), "failed " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });

    }

    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
//    public void generateHashkey(){
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    PACKAGE,
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//                Log.d(TAG,info.packageName);
//                Log.d(TAG,Base64.encodeToString(md.digest(), Base64.NO_WRAP));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.d(TAG, e.getMessage(), e);
//        } catch (NoSuchAlgorithmException e) {
//            Log.d(TAG, e.getMessage(), e);
//        }
//    }
}

