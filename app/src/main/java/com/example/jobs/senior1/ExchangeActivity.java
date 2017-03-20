package com.example.jobs.senior1;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RangedBeacon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "ExchangeActivity";
    private BeaconManager beaconManager;
    View loading;
    View nofound;
    public static BeaconTransmitter beaconTransmitter;
    ListView lv;
    Collection<Beacon> beacons;
    ArrayList<AccountResponse> accList;
    boolean refreshable;

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
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
            beaconTransmitter.stopAdvertising();
            onBackPressed();
            return true;
        }
        if (idd == R.id.refresh) {
            nofound.setVisibility(View.INVISIBLE);
            refresh();

            refreshable = true;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        final ArrayList<Beacon> beacons2 = new ArrayList<Beacon>();
        try {

            Log.d(TAG, "Refresh");
            beacons2.addAll(beacons);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            if (beacons2.size() == 0)
                loadListView(beacons2);
            for (int i = 0; i < beacons2.size(); i++) {
                String id = beacons2.get(i).getId1() + "";
                id = id.replaceAll("-", "");
                id = id.substring(8);
                Call<AccountResponse> call = apiService.getProfileImg(id);
                call.enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
//                        if(!accList.contains(response.body()))
                        accList.add(response.body());
                        Log.d(TAG, "Add");
                        if (beacons2.size() == accList.size())
                            loadListView(beacons2);
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        // Log error here since request failed
                    }
                });
            }

        } catch (Exception e) {

            Log.d(TAG, e.getMessage());
        } finally {
            //refreshable = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else startBeacon();
        setContentView(R.layout.activity_exchange);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.listView);
        loading = findViewById(R.id.loadingPanel);
        nofound = findViewById(R.id.noFound);
        TextView cancel = (TextView) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nofound.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);

                refreshable = false;
            }
        });


    }

    private void startBeacon() {

        Toast toast = Toast.makeText(getApplicationContext(), "Start Beacon", Toast.LENGTH_SHORT);
        toast.show();
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        //beaconManager.setBackgroundScanPeriod(30000);
        final SharedPreferences examplePrefs = getSharedPreferences("Notification", 0);

        if (examplePrefs.getBoolean("Notification", true)) {
            broadcastBeacon();
        }
        accList = new ArrayList<AccountResponse>();
        refreshable = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                startBeacon();
                // Do something with the contact here (bigger example below)
            } else {

                Intent intent = new Intent(ExchangeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }


    private void setBeacon(Collection<Beacon> beacons) {

        Log.d(TAG, "Set Beacon");
        // beaconManager.unbind(this);
        accList = new ArrayList<AccountResponse>();
        this.beacons = beacons;
        refresh();
    }

    private void loadListView(ArrayList<Beacon> beacons2) {

        // beaconManager.unbind(this);
        Log.d(TAG, beacons.size() + " beacons " + accList.size());
        if (accList.size() == 0) {
            runOnUiThread(new Runnable() {
                public void run() {
                    loading.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.INVISIBLE);
                    TextView found = (TextView) findViewById(R.id.found);
                    found.setText("0 Found");
                }
            });
            Log.d(TAG, "ListView: No");

        } else {
            final ExchangeListAdapter adapter = new ExchangeListAdapter(getApplicationContext(), accList, beacons2);
            runOnUiThread(new Runnable() {
                public void run() {
                    lv.setVisibility(View.VISIBLE);
                    Log.d(TAG, "ListView:" + accList.size());
                    TextView found = (TextView) findViewById(R.id.found);
                    found.setText(accList.size() + " Found");
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
                            Intent intent = new Intent(ExchangeActivity.this, ChooseCardActivity.class);
                            intent.putExtra("targetAccId", adapter.getId(arg2));
                            intent.putExtra("name", adapter.getName(arg2));
                            intent.putExtra("token", adapter.getToken(arg2));
                            //  Log.d("cardId",arg2+""+adapter.getId(arg2));
                            startActivity(intent);
                        }
                    });
                    loading.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            beaconManager.unbind(this);
        } catch (Exception e) {
            Log.e(TAG, "Beacon Destroy: " + e.getMessage());
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (refreshable) {
                    if (beacons.size() > 0) refreshable = false;
                    Log.i(TAG, "There are  " + beacons.size() + " beacons away.");
                    setBeacon(beacons);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//refresh();
////stuff that updates ui
//
//                        }
//                    });

                }

            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    public void broadcastBeacon() {
        SharedPreferences sp = getSharedPreferences("Account", Context.MODE_PRIVATE);
        Beacon beacon = new Beacon.Builder()
                .setId1(sp.getString("accId", "0"))
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l}))
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
        Log.d(TAG, "Start broadcast");
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                //NavUtils.navigateUpFromSameTask(this);
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
