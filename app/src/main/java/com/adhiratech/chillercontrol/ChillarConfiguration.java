package com.adhiratech.chillercontrol;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Components.TimerComponent;
import com.adhiratech.chillercontrol.Components.ValueSelector;
import com.adhiratech.chillercontrol.Model.DevicesFound;
import com.adhiratech.chillercontrol.Rest.Constants;
import com.adhiratech.chillercontrol.Rest.HttpManager;
import com.adhiratech.chillercontrol.Rest.RequestPackage;
import com.adhiratech.chillercontrol.Rest.Utils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import static com.adhiratech.chillercontrol.R.id.noofcycles_seek;

public class ChillarConfiguration extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener, ValueSelector.ButtonclickCallBack {


    TextView devicename, devicelocation, noofcycles, injtime, brktime, retrivaltime, restime, noofcycles_DIALOG, injtime_DIALOG, brktime_DIALOG, retrivaltime_DIALOG, restime_DIALOG;
    SeekBar noofcyclesSeek, injtimeSeekSecond, retrivalTimeSeekSeconds, resttimeSeekHour, resttimeSeekMinute, resttimeSeekSecond;

    TimerComponent restTimetimer, retrivalTimeTimer, breaktimeTimer, injectiontimetimer;
    Button submmit;
    Utils utils;
    //   Switch switchon;
    ConfigSubmmitTask mAuthTask = null;
 //   String submmitURL;
    ValueSelector restimeValueSelect, retrivaltimeValueselect, breaktimeValueSelect, injectiontimeValueselect, noofcyclesValueselect;
    DecelerateInterpolator decelerateInterpolator_SEEKBAR = new DecelerateInterpolator();
    private View DialogPositiveAction;
    private int REST_HOUR;
    private int REST_MINUTE;
    private int REST_SECOND;
    private int REST_TOTAL_SECONDS;
    private int RETRIVAL_SECOND;
    private int BREAK_SECOND;
    private int INJECTION_SECOND;
    private int CYCLES;
    private String DEVICE_ID;
    private DevicesFound SELECTED_DEVICE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chillar_configuration);
      //  submmitURL = Constants.HTTP.CONFIGsubmmitURL;
        utils = (Utils) getApplicationContext();
        SELECTED_DEVICE = utils.getSELECTED_DEVICE();
        noofcycles = (TextView) findViewById(R.id.noofcycles);
        injtime = (TextView) findViewById(R.id.injtime);
        brktime = (TextView) findViewById(R.id.brktime);
        retrivaltime = (TextView) findViewById(R.id.retritime);
        restime = (TextView) findViewById(R.id.rsttime);

        noofcyclesSeek = (SeekBar) findViewById(noofcycles_seek);
        noofcyclesValueselect = (ValueSelector) findViewById(R.id.noofcycles_valueSelector);

        injtimeSeekSecond = (SeekBar) findViewById(R.id.injectiontime_seek);
        injectiontimeValueselect = (ValueSelector) findViewById(R.id.injection_valueSelector);
        injectiontimetimer = (TimerComponent) findViewById(R.id.injectiontime_timer);

        //  breaktimeseek = (SeekBar) findViewById(R.id.breaktime_seek);
        breaktimeTimer = (TimerComponent) findViewById(R.id.breaktime_timer);
        breaktimeValueSelect = (ValueSelector) findViewById(R.id.breaktime_valueSelector);

        //       retrivaltimeSeekMinute = (SeekBar) findViewById(R.id.retrivaltime_seek_min);
        retrivalTimeSeekSeconds = (SeekBar) findViewById(R.id.retrivaltime_seek_sec);
        retrivaltimeValueselect = (ValueSelector) findViewById(R.id.retrival_valueSelector);
        retrivalTimeTimer = (TimerComponent) findViewById(R.id.retrivaltime_timer);


        resttimeSeekHour = (SeekBar) findViewById(R.id.resttime_seek_hr);
        resttimeSeekMinute = (SeekBar) findViewById(R.id.resttime_seek_min);
        resttimeSeekSecond = (SeekBar) findViewById(R.id.resttime_seek_sec);
        restimeValueSelect = (ValueSelector) findViewById(R.id.resttime_valueSelector);
        restTimetimer = (TimerComponent) findViewById(R.id.resttime_timer);


        devicename = (TextView) findViewById(R.id.device_name_config);
        devicelocation = (TextView) findViewById(R.id.device_location_config);

        //     restimeValueSelect = (ValueSelector) findViewById(R.id.rsttimevalueSelector);

        noofcyclesSeek.setOnSeekBarChangeListener(this);
        noofcyclesValueselect.setButtonClickCallBack(this);

        injtimeSeekSecond.setOnSeekBarChangeListener(this);
        injectiontimeValueselect.setButtonClickCallBack(this);

        //  breaktimeseek.setOnSeekBarChangeListener(this);
        breaktimeValueSelect.setButtonClickCallBack(this);

        //  retrivaltimeSeekMinute.setOnSeekBarChangeListener(this);
        retrivalTimeSeekSeconds.setOnSeekBarChangeListener(this);
        retrivaltimeValueselect.setButtonClickCallBack(this);

        resttimeSeekHour.setOnSeekBarChangeListener(this);
        resttimeSeekMinute.setOnSeekBarChangeListener(this);
        resttimeSeekSecond.setOnSeekBarChangeListener(this);
        restimeValueSelect.setButtonClickCallBack(this);


        submmit = (Button) findViewById(R.id.submmitbutton);

        //   switchon = (Switch) findViewById(R.id.switchon);

        submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submmitTheChanges();
            }
        });
        DEVICE_ID = utils.getDEVICE_ID();


        if (SELECTED_DEVICE != null) {
            updateTheDeviceDisplay();
        }


    }

    private void updateTheDeviceDisplay() {


        CYCLES = Integer.parseInt(SELECTED_DEVICE.getCycles());
        INJECTION_SECOND = Integer.parseInt(SELECTED_DEVICE.getInjectionTime());
        BREAK_SECOND = Integer.parseInt(SELECTED_DEVICE.getBreakTime());
        RETRIVAL_SECOND = Integer.parseInt(SELECTED_DEVICE.getRetrievalTime());
        REST_TOTAL_SECONDS = Integer.parseInt(SELECTED_DEVICE.getResetTime());
        DEVICE_ID = SELECTED_DEVICE.getDeviceId();

        devicename.setText(SELECTED_DEVICE.getD_name());
        devicelocation.setText(SELECTED_DEVICE.getLocat());

        getSupportActionBar().setTitle("ID :" + SELECTED_DEVICE.getDeviceId());
        updatetheRestTimeValues(REST_TOTAL_SECONDS);
        updatetheRetriveTimeValues(RETRIVAL_SECOND);
        updatetheBreakTimeValues(BREAK_SECOND);
        updatetheInjectionTimeValues(INJECTION_SECOND);
        updateNoOfCyclesValues(CYCLES);


    }

    private void submmitTheChanges() {
        if (isOnline()) {


            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title(R.string.configurationChanges)
                    .customView(R.layout.dialog_customview, true)
                    .positiveText(R.string.commit)
                    .negativeText(android.R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mAuthTask = new ConfigSubmmitTask();
                            RequestPackage p = new RequestPackage();
                            p.setMethod("POST");
                            p.setUri(Constants.HTTP.CONFIGsubmmitURL);
                            p.setParam("d_id", DEVICE_ID);
                            p.setParam("it", String.valueOf(INJECTION_SECOND));
                            p.setParam("rt", String.valueOf(RETRIVAL_SECOND));
                            p.setParam("break", String.valueOf(BREAK_SECOND));
                            p.setParam("cycle", String.valueOf(CYCLES));
                            p.setParam("reset", String.valueOf(REST_TOTAL_SECONDS));
                            mAuthTask.execute(p);
                        }
                    }).build();

            DialogPositiveAction = dialog.getActionButton(DialogAction.POSITIVE);

            noofcycles_DIALOG = (TextView) dialog.getCustomView().findViewById(R.id.noOfCycles_dialog);
            injtime_DIALOG = (TextView) dialog.getCustomView().findViewById(R.id.injectionTime_Dialog);
            brktime_DIALOG = (TextView) dialog.getCustomView().findViewById(R.id.breakTime_dialog);
            retrivaltime_DIALOG = (TextView) dialog.getCustomView().findViewById(R.id.retrivalTime_Dialog);
            restime_DIALOG = (TextView) dialog.getCustomView().findViewById(R.id.restTime_Dialog);

            noofcycles_DIALOG.setText(String.valueOf(CYCLES));
            injtime_DIALOG.setText(String.valueOf(INJECTION_SECOND));
            brktime_DIALOG.setText(String.valueOf(BREAK_SECOND));
            retrivaltime_DIALOG.setText(String.valueOf(RETRIVAL_SECOND));
            restime_DIALOG.setText(String.valueOf(REST_TOTAL_SECONDS));
            //noinspection ConstantConditions
            //    passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);


            // Toggling the show password CheckBox will mask or unmask the password input EditText


            dialog.show();
            //      positiveAction.setEnabled(false);


        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        updateTheDisplay(seekBar, i, b);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        updateTheDisplay(seekBar, seekBar.getProgress(), false);

    }

    private void updateTheDisplay(SeekBar seekBarID, int i, boolean b) {

        switch (seekBarID.getId()) {

            case noofcycles_seek: {
                if (i > 1) {
                    noofcycles.setText(String.valueOf(i));
                    CYCLES = i;
                } else {
                    CYCLES = 1;
                    noofcycles.setText(String.valueOf(CYCLES));
                }
                updateTheCycles();

            }
            break;
            case R.id.injectiontime_seek:

            {
                INJECTION_SECOND = i;
                injtime.setText(String.valueOf(i));


                if (i < 30) {
                    INJECTION_SECOND = 30;
                    injectiontimetimer.setValue(30);
                }
                updateTheInjectionTime();
            }

            break;


            case R.id.retrivaltime_seek_sec: {
                RETRIVAL_SECOND = i;
                retrivaltime.setText(String.valueOf(i));


                if (i < 30) {
                    RETRIVAL_SECOND = 30;
                    retrivalTimeTimer.setValue(30);
                }
                updateTheRetrivalTime();
            }
            break;


            case R.id.resttime_seek_hr: {
                REST_HOUR = i;
                restime.setText(String.valueOf(i));
                //    restimeValueSelect.setValue(i);
                updatetheRestTimeValuesusingSeek();
            }
            break;
            case R.id.resttime_seek_min: {
                REST_MINUTE = i;
                restime.setText(String.valueOf(i));
                updatetheRestTimeValuesusingSeek();
                //   restimeValueSelect.setValue(i);
            }
            break;
            case R.id.resttime_seek_sec: {
                REST_SECOND = i;
                restime.setText(String.valueOf(i));

        /*        if (REST_TOTAL_SECONDS < 10) {
                    REST_SECOND = 10;
                    REST_TOTAL_SECONDS = 10;
                    restTimetimer.setValue(10);
                }*/
                updatetheRestTimeValuesusingSeek();

            }
            break;

        }


    }

    private void updateTheCycles() {

        noofcyclesValueselect.setValue(CYCLES);

    }

    private void updateTheInjectionTime() {
        int t = INJECTION_SECOND;
        int x = t / 30;
        int value = x * 30;

        injectiontimeValueselect.setValue(value);
        injectiontimetimer.setValue(value);
        INJECTION_SECOND = value;
    }

    private void updateTheRetrivalTime() {

        int t = RETRIVAL_SECOND;
        int x = t / 30;
        int value = x * 30;

        retrivaltimeValueselect.setValue(value);
        retrivalTimeTimer.setValue(value);
        RETRIVAL_SECOND = value;
    }

    private void updatetheRestTimeValuesusingSeek() {
        int resttimeseconds = (REST_HOUR * 60 * 60) + (REST_MINUTE * 60) + REST_SECOND;
        restimeValueSelect.setValue(resttimeseconds);
        restTimetimer.setValue(resttimeseconds);
        REST_TOTAL_SECONDS = resttimeseconds;
        if (REST_TOTAL_SECONDS < 10) {
            REST_SECOND = 10;
            REST_TOTAL_SECONDS = 10;
            restTimetimer.setValue(10);
        }

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAddButtonClick(View add) {
        Log.i("TIMER", "Value Updated Button Click");
        switch (add.getId()) {


            case R.id.resttime_valueSelector: {
                updatetheRestTimeValues(restimeValueSelect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.retrival_valueSelector: {
                updatetheRetriveTimeValues(retrivaltimeValueselect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.breaktime_valueSelector: {
                updatetheBreakTimeValues(breaktimeValueSelect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.injection_valueSelector: {
                updatetheInjectionTimeValues(injectiontimeValueselect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.noofcycles_valueSelector: {
                updateNoOfCyclesValues(noofcyclesValueselect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;

        }
    }


    @Override
    public void onReduceButtonClick(View minus) {
        switch (minus.getId()) {

            case R.id.resttime_valueSelector: {
                updatetheRestTimeValues(restimeValueSelect.getValue());
            }
            break;
            case R.id.retrival_valueSelector: {
                updatetheRetriveTimeValues(retrivaltimeValueselect.getValue());
            }
            break;
            case R.id.breaktime_valueSelector: {
                updatetheBreakTimeValues(breaktimeValueSelect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.injection_valueSelector: {
                updatetheInjectionTimeValues(injectiontimeValueselect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;
            case R.id.noofcycles_valueSelector: {
                updateNoOfCyclesValues(noofcyclesValueselect.getValue());

                Log.i("TIMER", "Value Updated Button Click");
            }
            break;

        }

    }


    private void updatetheRestTimeValues(int sec) {
        Log.i("TIMER", "Value Updated " + sec);
        restTimetimer.setValue(sec);

        int hours = sec / 3600;
        int minutes = (sec / 60) - (hours * 60);
        int seconds = sec - (hours * 3600) - (minutes * 60);

        REST_HOUR = hours;
        REST_MINUTE = minutes;
        REST_SECOND = seconds;

        //   resttimeSeekHour.setProgress(REST_HOUR);
        // resttimeSeekMinute.setProgress(REST_MINUTE);
        //    resttimeSeekSecond.setProgress(REST_SECOND);

        animateTheseekBar(resttimeSeekHour, REST_HOUR);
        animateTheseekBar(resttimeSeekMinute, REST_MINUTE);
        animateTheseekBar(resttimeSeekSecond, REST_SECOND);


    }


    private void updatetheRetriveTimeValues(int value) {
        retrivalTimeTimer.setValue(value);
        RETRIVAL_SECOND = value;
        //   retrivalTimeSeekSeconds.setProgress(value);

        animateTheseekBar(retrivalTimeSeekSeconds, value);
        // retrivalTimeSeekSeconds.setProgress(value);

    }

    private void updateNoOfCyclesValues(int value) {

        CYCLES = value;
        animateTheseekBar(noofcyclesSeek, value);
        //    noofcyclesSeek.setProgress(value);
    }

    private void updatetheInjectionTimeValues(int value) {
        injectiontimetimer.setValue(value);
        INJECTION_SECOND = value;
        animateTheseekBar(injtimeSeekSecond, value);
        //  injtimeSeekSecond.setProgress(value);
    }

    private void updatetheBreakTimeValues(int value) {
        BREAK_SECOND = value;
        breaktimeTimer.setValue(value);
        breaktimeValueSelect.setValue(value);
        brktime.setText(String.valueOf(value));
    }

    private void animateTheseekBar(SeekBar seekBar, int seekProgress) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", seekProgress);
            animation.setDuration(400); // 0.2 second
            animation.setInterpolator(decelerateInterpolator_SEEKBAR);
            animation.start();
        } else
            seekBar.setProgress(seekProgress);

    }


    public class ConfigSubmmitTask extends AsyncTask<RequestPackage, String, String> {


        @Override
        protected String doInBackground(RequestPackage... params) {
            // TODO: attempt authentication against a network service.

            String content = HttpManager.getData(params[0]);

            // TODO: register the new account here.
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;

            String theResult = result.trim();
            if (theResult.equals("1")) {
                Toast.makeText(ChillarConfiguration.this, "Submmission Sucessful", Toast.LENGTH_SHORT).show();
            }
            Log.e("REST", result);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}
