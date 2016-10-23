package com.adhiratech.chillercontrol.Rest;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Model.DeviceList;
import com.adhiratech.chillercontrol.Model.DevicesFound;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DARWIN V TOMY on 10/2/2016.
 */

public class BackgroundService extends Service {
    public static final String BROADCAST_ACTION = "com.adhiratech.chillercontrol.broadcastevent";
    private static final String TAG = "RETROSARVICE";
    private final Handler handler = new Handler();
    Intent intent;
    Utils utils ;
    int counter = 0;
    private List<DevicesFound> DEVICE_FOUND_LIST = new ArrayList<>();
    private RestManager VideoManager;
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 10000); // 10 seconds
        }
    };

    @Override
    public void onCreate() {
        Log.e(TAG, "Background Service Oncreate");
        super.onCreate();
        VideoManager = new RestManager();
        intent = new Intent(BROADCAST_ACTION);
        utils = ((Utils) getApplication());
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        Log.e("RETROSARVICE", "Background Service Onstart");
        handler.postDelayed(sendUpdatesToUI, utils.getLIST_REFRESH_TIME()); // 1 second

    }

    private void DisplayLoggingInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        loadDeviceListFeed();
    //    intent.putExtra("counter", String.valueOf(++counter));
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Ondestroy from service called");
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }

    private void loadDeviceListFeed() {

        Log.d(TAG, "LOAD DEVICE FEED");
        if (getNetworkAvailability()) {
            getFeed();
        } else {
            Toast.makeText(BackgroundService.this, "Network Not avaliable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }

    public void getFeed() {

Log.i(TAG,"Global class acess"+utils.getUSR_ID()+" "+ utils.getPASSWORD());
        Call<DeviceList> videolistfromApi = VideoManager.gettheDeviceslistreport().gettheDeviceslistService(utils.getUSR_ID(), utils.getPASSWORD());
        videolistfromApi.enqueue(new Callback<DeviceList>() {
            @Override
            public void onResponse(Call<DeviceList> call, Response<DeviceList> response) {
                if (response.isSuccessful()) {

                    Log.i("RETROSARVICE", response.body().getUid());
                    //  Log.e("RESPONSE", response.body().getCurDevice().getDeviceId());
                    //     getActionBar().setTitle("Hello world App");
                    DEVICE_FOUND_LIST = response.body().getDevicesFound();
                    for (int i = 1; i < DEVICE_FOUND_LIST.size(); i++) {
                        Log.i("RETROSARVICE", "The device found is " + DEVICE_FOUND_LIST.get(i).getDeviceId());
                    }
                    utils.setDEVICE_LIST(response.body());

                    Log.i("RETROSARVICE", response.body().getDevicesFound().size() + "");


                    // mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("RETROSARVICE", "Out Put is not sucessful");
                    Log.e("RETROSARVICE", response.message());
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

    }
}