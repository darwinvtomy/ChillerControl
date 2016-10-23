package com.adhiratech.chillercontrol;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Model.DeviceList;
import com.adhiratech.chillercontrol.Rest.RestManager;
import com.adhiratech.chillercontrol.Rest.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Utils utils;
    private RestManager loginManager;
    private View mLoginFormView;
    private View mProgressView;
    //  TextView tv;
    private DeviceList theDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils = (Utils) getApplicationContext();

        sharedPref = this.getSharedPreferences(getString(R.string.MYKEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        mProgressView = findViewById(R.id.main_activity_progress);
        mLoginFormView = findViewById(R.id.mainactivity_form);


        boolean loggedin = sharedPref.getBoolean(getString(R.string.LOGGED_IN), false);
      /*  String userID = sharedPref.getString(getString(R.string.USERNAME_KEY), null);
        String passworD = sharedPref.getString(getString(R.string.PASSWORD_KEY), null);*/
        Log.e("PREFS", "onCreate:LOGGID IN  " + loggedin);
/*        Log.e("PREFS", "onCreate:LOGGID IN  "+userID );
        Log.e("PREFS", "onCreate:LOGGID IN  "+passworD );*/
        if (loggedin) {
            String userID = sharedPref.getString(getString(R.string.USERNAME_KEY), null);
            String passworD = sharedPref.getString(getString(R.string.PASSWORD_KEY), null);
            utils.setUSR_ID(userID);
            utils.setPASSWORD(passworD);
            loginManager = new RestManager();
            loadFlowerFeed();

        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }


    }

    public void OpenLoginPage(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void OpenScrollingPage(View view) {
        Intent intent = new Intent(MainActivity.this, DevicesListActivity.class);
        startActivity(intent);
    }

    public void deviceDetailss(View view) {
        Intent intent = new Intent(MainActivity.this, DeviceStatus.class);
        startActivity(intent);
    }

    public void configureChillar(View view) {
        Intent intent = new Intent(MainActivity.this, ChillarConfiguration.class);
        startActivity(intent);
    }

    private void loadFlowerFeed() {


        if (getNetworkAvailability()) {
            getFeed();
        } else {
            Toast.makeText(MainActivity.this, "Network Not avaliable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }

    public void getFeed() {


        Call<DeviceList> videolistfromApi = loginManager.gettheDeviceslistreport().gettheDeviceslistService(utils.getUSR_ID(), utils.getPASSWORD());
        videolistfromApi.enqueue(new Callback<DeviceList>() {
            @Override
            public void onResponse(Call<DeviceList> call, Response<DeviceList> response) {
                if (response.isSuccessful()) {


                    Log.i("RETROLIST", response.body().getUid());
                    theDeviceList = response.body();
                    utils.setDEVICE_LIST(theDeviceList);
                    for (int i = 1; i < theDeviceList.getDevicesFound().size(); i++) {
                        Log.i("RETROLIST", "The device found Login is " + theDeviceList.getDevicesFound().get(i).getDeviceId());
                    }
                    Log.i("RETROLIST", response.body().getDevicesFound().size() + "");
                    //    LOGGED_IN = true;
                   /* editor.putBoolean(getString(LOGGED_IN), true);
                    editor.putString(getString(R.string.USERNAME_KEY), utils.getUSR_ID());
                    editor.putString(getString(R.string.PASSWORD_KEY), utils.getPASSWORD());
                    editor.apply();*/


                    showProgress(false);
                    Intent deviceList = new Intent(MainActivity.this, DevicesListActivity.class);
                    startActivity(deviceList);
                    finish();


                    // mAdapter.notifyDataSetChanged();
                } else {
                    showProgress(false);
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

    }

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

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
