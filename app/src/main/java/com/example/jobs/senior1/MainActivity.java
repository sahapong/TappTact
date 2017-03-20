package com.example.jobs.senior1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private final static String TAG = "MainActivity";
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ExchangeActivity.class);
//                startActivity(intent);
////                Snackbar.make(view, "Exchange Feature", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        LoadNav(header);
//        findViewById(R.id.action_search).setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                //Do stuff here
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent);
//            }
//        });

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentContactList(), "Contact");
        adapter.addFrag(new FragmentManageCard(), "My Card");
        adapter.addFrag(new FragmentSetting(), "Setting");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        if(getIntent().getStringExtra("manage")!=null) {
            viewPager.setCurrentItem(1);
            Log.d(TAG,"Back from Manage");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.exchange) {
            // Handle the camera action

            Intent intent = new Intent(MainActivity.this, ExchangeActivity.class);
            startActivity(intent);
        }
            if (id == R.id.setting) {
                // logout();
                viewPager.setCurrentItem(2);
            }

            if (id == R.id.inbox) {
                // logout();
                Intent intent = new Intent(MainActivity.this, InboxActivity.class);
                startActivity(intent);
            }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void LoadNav(View header){
        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        ImageView iv = (ImageView) header.findViewById(R.id.imageViewPf);
        TextView name = (TextView) header.findViewById(R.id.nameNav);
        TextView email = (TextView) header.findViewById(R.id.emailNav);
        name.setText(sp.getString("fname","firstName")+" "+sp.getString("lname","lName"));
        email.setText(sp.getString("email","email"));
      //  Log.d("imageProfile",response.body().getImage());
        Picasso.with(getApplicationContext()).load(sp.getString("image",null)).transform(new CircleTransform()).into(iv);
//        ApiInterface apiService2 =
//                ApiClient.getClient().create(ApiInterface.class);
//        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
//        Call<AccountResponse> call2 = apiService2.getProfileImg(sp.getString("accId","0"));
//
//        call2.enqueue(new Callback<AccountResponse>() {
//            @Override
//            public void onResponse(Call<AccountResponse>call, Response<AccountResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<AccountResponse>call, Throwable t) {
//                // Log error here since request failed
//                Log.e("restImage", t.toString());
//            }
//        });
    }
}
