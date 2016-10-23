package com.adhiratech.chillercontrol;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Model.Devicedetail;
import com.adhiratech.chillercontrol.Model.DevicesFound;
import com.adhiratech.chillercontrol.Rest.Constants;
import com.adhiratech.chillercontrol.Rest.HttpManager;
import com.adhiratech.chillercontrol.Rest.RequestPackage;
import com.adhiratech.chillercontrol.Rest.RestManager;
import com.adhiratech.chillercontrol.Rest.Utils;
import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeviceStatus extends AppCompatActivity {

    private static final String TAG = "DEVICE_STATUS";
    //   Bundle savedInstanceStates;
    private static String[] device_Modes;
    TextView temp1, temp2, psr1, psr2, device_name, device_location, device_mode;
    //   TextView temp1unit, temp2unit, psr1unit, psr2unit;
    //   ImageView temp1_ICON, temp2_ICON, psr1_ICON, psr2_ICON;
    TextView update_time_tv, flostatus_tv, motorstatus_tv, overlod_tv,
            injectiontime_tv, rtrivaltime_tv, breaktime_tv, cycles_tv, resttime_tv, divice_name_ontop;
    CardView temp1Container, temp2Container, psr1Container, psr2Container;
    Utils utils;
    ImageView mImageViewFilling;
    //Graph THING
    Resources resources;
    ConfigSubmmitTask mAuthTask = null;
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 10;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private RestManager DeviceManager;
    private String DEVICE_ID,
            MODE,
            SERVER_TIME,
            TEMP1,
            TEMP2,
            PSR1,
            PSR2,
            FLOW_STATUS,
            MOTOR_STATUS,
            OVERLOAD,
            INJECTION_TIME,
            RETRIVAL_TIME,
            BREAK_TIME,
            CYCLES,
            REST_TIME;
    private List<String> TIME_LABELS = new ArrayList<>();
    private List<Float> TEMP_1_DATA = new ArrayList<Float>();
    private List<Float> TEMP_2_DATA = new ArrayList<Float>();
    private List<Float> PSR_1_DATA = new ArrayList<Float>();
    private List<Float> PSR_2_DATA = new ArrayList<Float>();
    private HashMap<Integer, ArrayList<Float>> GRAPH_ITEMS_DISPLAY_DATA = new HashMap<Integer, ArrayList<Float>>();
    private DevicesFound SELECTED_DEVICE;
    private SlidingUpPanelLayout mLayout;
    private Switch statusButton;
    private Boolean RESUMED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        //  setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        utils = (Utils) getApplicationContext();
        SELECTED_DEVICE = utils.getSELECTED_DEVICE();
        DeviceManager = new RestManager();

        //      this.savedInstanceStates = savedInstanceState;
        divice_name_ontop = (TextView) findViewById(R.id.d_name_status);
        device_name = (TextView) findViewById(R.id.device_name_status);
        statusButton = (Switch) findViewById(R.id.switch_button);
        device_location = (TextView) findViewById(R.id.device_location_status);
        device_mode = (TextView) findViewById(R.id.device_mode_status);
        update_time_tv = (TextView) findViewById(R.id.time_update);
        flostatus_tv = (TextView) findViewById(R.id.fl_status);
        motorstatus_tv = (TextView) findViewById(R.id.motor_status);
        overlod_tv = (TextView) findViewById(R.id.ovrd_status);
        injectiontime_tv = (TextView) findViewById(R.id.inject_time);
        rtrivaltime_tv = (TextView) findViewById(R.id.retriv_time);
        breaktime_tv = (TextView) findViewById(R.id.break_time);
        cycles_tv = (TextView) findViewById(R.id.cycles);
        resttime_tv = (TextView) findViewById(R.id.rest_time);
        temp1 = (TextView) findViewById(R.id.temp1_status);
        temp2 = (TextView) findViewById(R.id.temp2_status);
        psr1 = (TextView) findViewById(R.id.psr1_status);
        psr2 = (TextView) findViewById(R.id.psr2_status);
        temp1Container = (CardView) findViewById(R.id.temp1_card);
        temp2Container = (CardView) findViewById(R.id.temp2_card);
        psr1Container = (CardView) findViewById(R.id.psr1_card);
        psr2Container = (CardView) findViewById(R.id.psr2_card);

        resources = getResources();
        device_Modes = resources.getStringArray(R.array.modes);

        ///Graph Chart
        chart = (LineChartView) findViewById(R.id.chart);
        //  chart.setOnValueTouchListener(new PlaceholderFragment.ValueTouchListener());

        // Generate some random values.
        mImageViewFilling = (ImageView) findViewById(R.id.animation_View);

/*
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) mImageViewFilling.getBackground();

        // Start the animation (looped playback by default).
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();*/

        //  frameAnimation.start();
        if (SELECTED_DEVICE != null) {
            applyTheLatestStatus(SELECTED_DEVICE);
            DEVICE_ID = SELECTED_DEVICE.getDeviceId();
        }


        loadDeviceDetailsFeed();

        temp1Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 /*               if (savedInstanceStates == null) {
                    getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
                }*/

            }
        });


        temp2Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        psr1Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        psr2Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        statusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (RESUMED) {
                        statusButton.setChecked(true);
                        RESUMED = false;
                    } else
                        statusButton.setChecked(false);
                } else {
                    switchOffTheMachine(DEVICE_ID);
                }
            }
        });
    }

    private void switchOffTheMachine(String device_id) {
        mAuthTask = new ConfigSubmmitTask();
        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(Constants.HTTP.SWITCH_OFF_URL);
        p.setParam("d_id", device_id);
        mAuthTask.execute(p);
    }

    private void applyTheLatestStatus(DevicesFound selected_device) {

      //  String path = "android.resource://" + getPackageName() + "/" + R.raw.injection;
        //  getSupportActionBar().setTitle("ID :" + selected_device.getDeviceId());
        device_name.setText(selected_device.getD_name());
        divice_name_ontop.setText(selected_device.getD_name());
        device_location.setText(selected_device.getLocat());
        device_mode.setText(device_Modes[Integer.parseInt(selected_device.getMode())]);
        temp1.setText(selected_device.getTmp1());
        temp2.setText(selected_device.getTmp2());
        psr1.setText(selected_device.getPsr1());
        psr2.setText(selected_device.getPsr2());
        injectiontime_tv.setText(selected_device.getInjectionTime());
        rtrivaltime_tv.setText(selected_device.getRetrievalTime());
        breaktime_tv.setText(selected_device.getBreakTime());
        cycles_tv.setText(selected_device.getCycles());
        resttime_tv.setText(selected_device.getResetTime());
        configureTheToggleButton(selected_device.getInjectionTime(),
                selected_device.getRetrievalTime(), selected_device.getBreakTime(), selected_device.getCycles(), selected_device.getResetTime());
        if (selected_device.getFlstatus().trim().equals("0")) {
            flostatus_tv.setTextColor(getResources().getColor(R.color.off_Color));
            flostatus_tv.setText("OFF");

        } else {
            flostatus_tv.setTextColor(resources.getColor(R.color.on_Color));
            flostatus_tv.setText("ON");
        }
        if (selected_device.getMotor().trim().equals("0")) {
            motorstatus_tv.setTextColor(resources.getColor(R.color.off_Color));
            motorstatus_tv.setText("OFF");

        } else {
            motorstatus_tv.setTextColor(resources.getColor(R.color.on_Color));
            motorstatus_tv.setText("ON");
        }
        if (selected_device.getOverld().trim().equals("0")) {
            overlod_tv.setTextColor(resources.getColor(R.color.off_Color));
            overlod_tv.setText("OVERLOAD");

        } else {
            overlod_tv.setTextColor(resources.getColor(R.color.on_Color));
            overlod_tv.setText("NO");
        }

    }

    private void configureTheToggleButton(String injectionTime, String retrievalTime, String breakTime, String cycles, String resetTime) {
        Log.e(TAG, "configureTheToggleButton:  " + injectionTime + "  " + retrievalTime + "  " + breakTime + "  " + cycles + "  " + resetTime);
        if (injectionTime.equals("0") && retrievalTime.equals("0") && breakTime.equals("0") && cycles.equals("0") && resetTime.equals("0")) {
            Log.e(TAG, "configureTheToggleButton: Status Button Turned OFF");
            statusButton.setChecked(false);
        } else
            Log.e(TAG, "configureTheToggleButton: Status Button Turned ON");
        statusButton.setChecked(true);

    }


    private void loadDeviceDetailsFeed() {


        if (getNetworkAvailability()) {
            getFeed();
        } else {
            Toast.makeText(DeviceStatus.this, "Network Not avaliable", Toast.LENGTH_SHORT).show();
        }
    }

    public void getFeed() {


        Call<Devicedetail> videolistfromApi = DeviceManager.getDeviceStatusService().gettheDeviceDetailreport(DEVICE_ID);
        videolistfromApi.enqueue(new Callback<Devicedetail>() {
            @Override
            public void onResponse(Call<Devicedetail> call, Response<Devicedetail> response) {
                if (response.isSuccessful()) {

                    Log.e("RESPONSE", response.body().getCurDevice().getDeviceId());
                    //     getActionBar().setTitle("Hello world App");

                    DEVICE_ID = response.body().getCurDevice().getDeviceId();
                    MODE = response.body().getCurDevice().getMode();
                    SERVER_TIME = response.body().getCurDevice().getSdatetme();
                    TEMP1 = response.body().getCurDevice().getTmp1();
                    TEMP2 = response.body().getCurDevice().getTmp2();
                    PSR1 = response.body().getCurDevice().getPsr1();
                    PSR2 = response.body().getCurDevice().getPsr2();
                    FLOW_STATUS = response.body().getCurDevice().getFlstatus();
                    MOTOR_STATUS = response.body().getCurDevice().getMotor();
                    OVERLOAD = response.body().getCurDevice().getOverld();
                    INJECTION_TIME = response.body().getCurDevice().getInjectionTime();
                    RETRIVAL_TIME = response.body().getCurDevice().getRetrievalTime();
                    BREAK_TIME = response.body().getCurDevice().getBreakTime();
                    CYCLES = response.body().getCurDevice().getCycles();
                    REST_TIME = response.body().getCurDevice().getResetTime();

                    configureTheToggleButton(INJECTION_TIME, RETRIVAL_TIME, BREAK_TIME, CYCLES, REST_TIME);

                    if (FLOW_STATUS.trim().equals("0")) {
                        flostatus_tv.setTextColor(resources.getColor(R.color.off_Color));
                        flostatus_tv.setText("OFF");

                    } else {
                        flostatus_tv.setTextColor(resources.getColor(R.color.on_Color));
                        flostatus_tv.setText("ON");
                    }
                    if (MOTOR_STATUS.trim().equals("0")) {
                        motorstatus_tv.setTextColor(resources.getColor(R.color.off_Color));
                        motorstatus_tv.setText("OFF");

                    } else {
                        motorstatus_tv.setTextColor(resources.getColor(R.color.on_Color));
                        motorstatus_tv.setText("ON");
                    }
                    if (OVERLOAD.trim().equals("0")) {
                        overlod_tv.setTextColor(resources.getColor(R.color.off_Color));
                        overlod_tv.setText("OVERLOAD");

                    } else {
                        overlod_tv.setTextColor(resources.getColor(R.color.on_Color));
                        overlod_tv.setText("NO");
                    }


                    TIME_LABELS = response.body().getCurDevice().getTimelabels();
                    TEMP_1_DATA = response.body().getCurDevice().getDtemp1();
                    TEMP_2_DATA = response.body().getCurDevice().getDtemp2();
                    PSR_1_DATA = response.body().getCurDevice().getDpsr1();
                    PSR_2_DATA = response.body().getCurDevice().getDpsr2();


                    generateValues();
                    //   generateData();
                    // Disable viewport recalculations, see toggleCubic() method for more info.
                    chart.setViewportCalculationEnabled(false);
                    resetViewport();
/*                    for (int k = 0; k < TEMP_1_DATA.size(); ++k) {
                        Log.e("GRAPHPLOAT", "T1 " + TEMP_1_DATA.get(k));
*//*                        Log.e("GRAPHPLOAT","T2 "+TEMP_2_DATA.get(k));
                        Log.e("GRAPHPLOAT","P1 "+PSR_1_DATA.get(k));
                        Log.e("GRAPHPLOAT","P2 "+PSR_2_DATA.get(k));*//*
                        // randomNumbersTab[i][k]=TEMP_1_DATA.indexOf(k);
                    }*/
                    //     getSupportActionBar().setTitle("Device ID :" + DEVICE_ID);

                    //   Set<Integer> set = new HashSet<Integer>();
                    device_mode.setText(device_Modes[Integer.parseInt(MODE)]);
                    Log.i(TAG, "______Starting the Animation____");
                    upDateTheAnimation(Integer.parseInt(MODE));
                    temp1.setText(TEMP1);
                    temp2.setText(TEMP2);
                    psr1.setText(PSR1);
                    psr2.setText(PSR2);
                    update_time_tv.setText(SERVER_TIME);
                    injectiontime_tv.setText(INJECTION_TIME);
                    rtrivaltime_tv.setText(RETRIVAL_TIME);
                    breaktime_tv.setText(BREAK_TIME);
                    cycles_tv.setText(CYCLES);
                    resttime_tv.setText(REST_TIME);


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
            public void onFailure(Call<Devicedetail> call, Throwable t) {

            }
        });

    }

    private void upDateTheAnimation(int i) {
        Log.e(TAG, "Animation State has been set to " + i);
        int IMAGE_ID;
        switch (i) {
            case 4:
                IMAGE_ID = R.drawable.injection;
                break;
            case 5:
                IMAGE_ID = R.drawable.breaked;
                break;
            case 6:
                IMAGE_ID = R.drawable.retrival;

                break;
            case 7:
                IMAGE_ID = R.drawable.breaked; //Retrival Break Mode
                break;
            case 8:
                IMAGE_ID = R.drawable.rest;
                break;
            default:
                IMAGE_ID = R.drawable.rest;
                break;
        }
        Glide.with(this)
                .load(IMAGE_ID)
                .crossFade()
                .into(mImageViewFilling);
    }

    public boolean getNetworkAvailability() {
        return Utils.isNetworkAvailable(getApplicationContext());
    }


    private void generateValues() {
        int i = 0;
        while (i <= numberOfLines) {
            Log.e("GRAPHPLOAT", "Enter the looping");

            for (int k = 0; k < TEMP_1_DATA.size(); ++k) {
                Log.e("GRAPHPLOAT", "T1 " + i + " " + TEMP_1_DATA.get(k));
                randomNumbersTab[i][k] = TEMP_1_DATA.get(k);
            }
            i++;
            for (int l = 0; l < TEMP_2_DATA.size(); ++l) {
                randomNumbersTab[i][l] = TEMP_2_DATA.get(l);
                Log.e("GRAPHPLOAT", "T2 " + i + " " + TEMP_2_DATA.get(l));
            }
            i++;
            for (int m = 0; m < PSR_1_DATA.size(); ++m) {
                Log.e("GRAPHPLOAT", "P1 " + i + " " + PSR_1_DATA.get(m));
                randomNumbersTab[i][m] = PSR_1_DATA.get(m);
            }
            i++;
            for (int n = 0; n < PSR_2_DATA.size(); ++n) {
                Log.e("GRAPHPLOAT", "P2 " + i + " " + PSR_2_DATA.get(n));
                randomNumbersTab[i][n] = PSR_2_DATA.get(n);
            }
            i++;

        }
        Log.e("GRAPHPLOAT", "Exit the looping " + i);
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    /**
     * To animate values you have to change targets values and then call
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * {@link LineChartView#setLineChartData(LineChartData)} again.
     */
    private void prepareDataAnimation() {
        for (Line line : data.getLines()) {
            for (PointValue value : line.getValues()) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.getX(), (float) Math.random() * 100);
            }
        }
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
        }

        generateData();
    }

    private void toggleAxes() {
        hasAxes = !hasAxes;

        generateData();
    }

    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;

        generateData();
    }

    private void toggleFilled() {
        isFilled = !isFilled;

        generateData();
    }

    private void togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor;

        generateData();
    }

    private void setCircles() {
        shape = ValueShape.CIRCLE;

        generateData();
    }

    private void setSquares() {
        shape = ValueShape.SQUARE;

        generateData();
    }

    private void setDiamonds() {
        shape = ValueShape.DIAMOND;

        generateData();
    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        generateData();
    }

    private void addLineToData() {
        if (data.getLines().size() >= maxNumberOfLines) {
            Toast.makeText(DeviceStatus.this, "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ++numberOfLines;
        }

        generateData();
    }

    private void toggleLines() {
        hasLines = !hasLines;

        generateData();
    }

    private void togglePoints() {
        hasPoints = !hasPoints;

        generateData();
    }

    private void toggleCubic() {
        isCubic = !isCubic;

        generateData();

        if (isCubic) {
            // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
            // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
            // parameter or just set top and bottom values manually.
            // In this example I know that Y values are within (0,100) range so I set viewport height range manually
            // to (-5, 105).
            // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
            // modifying viewport.
            // Remember to set viewport after you call setLineChartData().
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = -5;
            v.top = 105;
            // You have to set max and current viewports separately.
            chart.setMaximumViewport(v);
            // I changing current viewport with animation in this case.
            chart.setCurrentViewportWithAnimation(v);
        } else {
            // If not cubic restore viewport to (0,100) range.
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;

            // You have to set max and current viewports separately.
            // In this case, if I want animation I have to set current viewport first and use animation listener.
            // Max viewport will be set in onAnimationFinished method.
            chart.setViewportAnimationListener(new ChartAnimationListener() {

                @Override
                public void onAnimationStarted() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationFinished() {
                    // Set max viewpirt and remove listener.
                    chart.setMaximumViewport(v);
                    chart.setViewportAnimationListener(null);

                }
            });
            // Set current viewpirt with animation;
            chart.setCurrentViewportWithAnimation(v);
        }

    }

    private void reset() {
        numberOfLines = 1;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        shape = ValueShape.CIRCLE;
        isFilled = false;
        hasLabels = false;
        isCubic = false;
        hasLabelForSelected = false;
        pointsHaveDifferentColor = false;

        chart.setValueSelectionEnabled(hasLabelForSelected);
        resetViewport();
    }


    public void loadGraphdata(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.tmp1_chkbox: {
                if (checked)
                    // Put some meat on the sandwich
                    //        Toast.makeText(DeviceStatus.this, "Temperature 1", Toast.LENGTH_SHORT).show();
                    // addpointsToGraph(0, 0);
                    addPLottingpointsToGraph(0, TEMP_1_DATA);
                else
                    //  Toast.makeText(DeviceStatus.this, "Temperature 1 unchecked", Toast.LENGTH_SHORT).show();
                    // Remove the meat
                    removePLottingpointsFromGraph(0);

            }
            break;
            case R.id.tmp2_chkbox: {
                if (checked)
                    // Cheese me
                    //  Toast.makeText(DeviceStatus.this, "Temperature 2", Toast.LENGTH_SHORT).show();

                    addPLottingpointsToGraph(1, TEMP_2_DATA);
                else
                    //  Toast.makeText(DeviceStatus.this, "Temperature 1 unchecked", Toast.LENGTH_SHORT).show();
                    // I'm lactose intolerant
                    removePLottingpointsFromGraph(1);
            }
            break;
            case R.id.psr1_chkbox: {
                if (checked)
                    // Cheese me
                    //    Toast.makeText(DeviceStatus.this, "Pressure 1", Toast.LENGTH_SHORT).show();

                    addPLottingpointsToGraph(2, PSR_1_DATA);
                else
                    //    Toast.makeText(DeviceStatus.this, "Temperature 1 unchecked", Toast.LENGTH_SHORT).show();
                    // I'm lactose intolerant
                    removePLottingpointsFromGraph(2);
            }
            break;
            case R.id.psr2_chkbox: {
                if (checked)
                    // Cheese me
                    //   Toast.makeText(DeviceStatus.this, "Pressure 2", Toast.LENGTH_SHORT).show();

                    addPLottingpointsToGraph(3, PSR_2_DATA);
                else
                    //  Toast.makeText(DeviceStatus.this, "Temperature 1 unchecked", Toast.LENGTH_SHORT).show();
                    // I'm lactose intolerant
                    removePLottingpointsFromGraph(3);
            }
            break;
            // TODO: Veggie sandwich
        }
    }

    private void removePLottingpointsFromGraph(int i) {
        GRAPH_ITEMS_DISPLAY_DATA.remove(i);
        updateTheGraph();
    }

    private void addPLottingpointsToGraph(int colorkey, List<Float> graph_data) {
        GRAPH_ITEMS_DISPLAY_DATA.put(colorkey, (ArrayList<Float>) graph_data);
        updateTheGraph();

    }

    private void updateTheGraph() {
        //  GRAPH_ITEMS_DISPLAY_DATA.size();


        List<Line> lines = new ArrayList<Line>();
        for (Integer key : GRAPH_ITEMS_DISPLAY_DATA.keySet()) {
            //   System.out.println("Key = " + key);

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < GRAPH_ITEMS_DISPLAY_DATA.get(key).size(); ++j) {
                Log.e("GRAPHDATALOADING", GRAPH_ITEMS_DISPLAY_DATA.get(key).get(j) + "");
                values.add(new PointValue(j, GRAPH_ITEMS_DISPLAY_DATA.get(key).get(j)));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[key]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(key + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);
    }

    @Override
    protected void onResume() {
        RESUMED = true;
        loadDeviceDetailsFeed();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {

            item.setTitle(R.string.action_config);

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(DeviceStatus.this, ChillarConfiguration.class);
        startActivity(intent);
/*        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }

        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
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
                Toast.makeText(DeviceStatus.this, "Device is Switched Off", Toast.LENGTH_SHORT).show();
            }
            Log.e("REST", result);

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}
