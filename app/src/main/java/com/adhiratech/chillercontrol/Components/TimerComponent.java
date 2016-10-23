package com.adhiratech.chillercontrol.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adhiratech.chillercontrol.R;

/**
 * Created by DARWIN V TOMY on 8/31/2016.
 */

public class TimerComponent extends RelativeLayout {

    View rootView;
    TextView hourTextView,minuteTextView,secondsTextView;


    public TimerComponent(Context context) {

        super(context);
        init(context);

    }

    public TimerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);


        init(context);
    }

    private void init(Context context) {
        //do setup work here

        rootView = inflate(context, R.layout.timer_component, this);
        hourTextView = (TextView) rootView.findViewById(R.id.timer_hour);
        minuteTextView = (TextView) rootView.findViewById(R.id.timer_minute);
        secondsTextView = (TextView) rootView.findViewById(R.id.timer_second);



      //  setValue(minimumValue);

    }



    public int getValue() {
        return Integer.valueOf(hourTextView.getText().toString());
    }

    public void setValue(int newValue) {
        Log.i("TIMER","Value Received "+newValue);

        int hours = newValue / 3600;
        int minutes = (newValue / 60) - (hours * 60);
        int seconds = newValue - (hours * 3600) - (minutes * 60);

        hourTextView.setText(String.format("%02d",hours));
        minuteTextView.setText(String.format("%02d",minutes));
        secondsTextView.setText(String.format("%02d",seconds));
      //  vListner.ListentotheValue(value);
    }







}