package com.adhiratech.chillercontrol;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Adapter.DeviceListAdapter;
import com.adhiratech.chillercontrol.Model.DevicesFound;
import com.adhiratech.chillercontrol.Rest.BackgroundService;
import com.adhiratech.chillercontrol.Rest.RestManager;
import com.adhiratech.chillercontrol.Rest.Utils;

import java.util.ArrayList;
import java.util.List;

public class DevicesListActivity extends AppCompatActivity implements DeviceListAdapter.ItemClickCallBack {

    Utils utils;
    // private DeviceListAdapter derpAdapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private RestManager VideoManager;
    private List<DevicesFound> devicesFoundList = new ArrayList<>();
    //  private RecyclerView recyclerView;
    private DeviceListAdapter mAdapter;
    private RecyclerView recView;
    private int list_refresh_value, status_refresh_value;
    private Intent intentservice;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            devicesFoundList.clear();
            devicesFoundList.addAll(utils.getDEVICE_LIST().getDevicesFound());
            //    devicesFoundList =
            // Log.i("RETROSARVICE",devicesFoundList.get(1).getBreakTime());

            for (int i = 1; i < devicesFoundList.size(); i++) {
                Log.i("RETROSARVICE", devicesFoundList.get(i).toString());
            }
            // Log.i("RETROSARVICE",devicesFoundList.get(1).getBreakTime());
            // Log.i("RETROSARVICE",devicesFoundList.get(1).getBreakTime());
          /*  mAdapter = new DeviceListAdapter(devicesFoundList, getResources(), DevicesListActivity.this);
            mAdapter.setItemClickCallBack(DevicesListActivity.this);
            recView.setHasFixedSize(true);
            recView.setAdapter(mAdapter);*/
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        VideoManager = new RestManager();
        utils = (Utils) getApplicationContext();

        recView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recView.setLayoutManager(mLayoutManager);

        sharedPref = this.getSharedPreferences(getString(R.string.MYKEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
/*        boolean loggedin = sharedPref.getBoolean(getString(R.string.LOGGED_IN), false);
        if (!loggedin) {
            Intent intent = new Intent(DevicesListActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            String userID = sharedPref.getString(getString(R.string.USERNAME_KEY), null);
            String passworD = sharedPref.getString(getString(R.string.USERNAME_KEY), null);
            utils.setUSR_ID(userID);
            utils.setPASSWORD(passworD);
        }*/

        int REFRESH_DEFAULT_VALUE = getResources().getInteger(R.integer.refresh_default_time);
        list_refresh_value = sharedPref.getInt(getString(R.string.status_refresh_time), REFRESH_DEFAULT_VALUE);
        status_refresh_value = sharedPref.getInt(getString(R.string.list_refresh_time), REFRESH_DEFAULT_VALUE);
        utils.setLIST_REFRESH_TIME(list_refresh_value);
        utils.setSTATUS_REFRESH_TIME(status_refresh_value);

        if (utils.getDEVICE_LIST() != null) {
            devicesFoundList.addAll(utils.getDEVICE_LIST().getDevicesFound());
            mAdapter = new DeviceListAdapter(devicesFoundList, getResources(), this);
            mAdapter.setItemClickCallBack(DevicesListActivity.this);
            recView.setHasFixedSize(true);
            recView.setAdapter(mAdapter);

        } else {
            //  loadDeviceListFeed();
        }

        intentservice = new Intent(this, BackgroundService.class);

    }

/*    private void loadDeviceListFeed() {


        if (getNetworkAvailability()) {
            getFeed();
        } else {
            Toast.makeText(DevicesListActivity.this, "Network Not avaliable", Toast.LENGTH_SHORT).show();
        }
    }*/

/*    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }*/

/*    public void getFeed() {


        Call<DeviceList> videolistfromApi = VideoManager.gettheDeviceslistreport().gettheDeviceslistService(utils.getUSR_ID(), utils.getPASSWORD());
        videolistfromApi.enqueue(new Callback<DeviceList>() {
            @Override
            public void onResponse(Call<DeviceList> call, Response<DeviceList> response) {
                if (response.isSuccessful()) {

                    Log.i("RETROLIST", response.body().getUid());
                    //  Log.e("RESPONSE", response.body().getCurDevice().getDeviceId());
                    //     getActionBar().setTitle("Hello world App");
                    devicesFoundList = response.body().getDevicesFound();
                    for (int i = 1; i < devicesFoundList.size(); i++) {
                        Log.i("RETROLIST", "The device found is " + devicesFoundList.get(i).getDeviceId());
                    }

                    Log.i("RETROLIST", response.body().getDevicesFound().size() + "");
                    mAdapter = new DeviceListAdapter(devicesFoundList, getResources(), DevicesListActivity.this);
                    mAdapter.setItemClickCallBack(DevicesListActivity.this);
                    recView.setHasFixedSize(true);
                    recView.setAdapter(mAdapter);

                    // mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("RETROSAMPLE", "Out Put is not sucessful");
                    Log.e("RETROSAMPLE", response.message());
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }

                }


            }


            @Override
            public void onFailure(Call<DeviceList> call, Throwable t) {

            }
        });

    }*/

    @Override
    public void onItemClick(int listitem) {
        DevicesFound device = devicesFoundList.get(listitem);
        Toast.makeText(DevicesListActivity.this, "Item Clicked " + device.getDeviceId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewButtonClick(int viewButton) {
        DevicesFound device = devicesFoundList.get(viewButton);
        Toast.makeText(DevicesListActivity.this, "ViewButton Clicked " + device.getDeviceId(), Toast.LENGTH_SHORT).show();
        Intent deviceStatus = new Intent(DevicesListActivity.this, DeviceStatus.class);
        utils.setSELECTED_DEVICE(device);
        startActivity(deviceStatus);
    }

    @Override
    public void onConfigureButtonClick(int confButton) {
        DevicesFound device = devicesFoundList.get(confButton);
        Toast.makeText(DevicesListActivity.this, "ViewButton Clicked " + device.getDeviceId(), Toast.LENGTH_SHORT).show();
        Intent deviceStatus = new Intent(DevicesListActivity.this, ChillarConfiguration.class);
        utils.setSELECTED_DEVICE(device);
        startActivity(deviceStatus);

    }


    @Override
    protected void onResume() {
        startService(intentservice);
        registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.BROADCAST_ACTION));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(DevicesListActivity.this, SampleActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                //    LOGGED_IN = true;
                editor.putBoolean(getString(R.string.LOGGED_IN), false);
                editor.putString(getString(R.string.USERNAME_KEY), null);
                editor.putString(getString(R.string.PASSWORD_KEY), null);
                editor.apply();
                Intent loginintent = new Intent(DevicesListActivity.this, LoginActivity.class);
                startActivity(loginintent);
                //TODO- Logout of the system
                /*Intent intent = new Intent(DevicesListActivity.this, SampleActivity.class);
                startActivity(intent);*/
                break;

        }
        return true;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}
