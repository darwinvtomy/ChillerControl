package com.adhiratech.chillercontrol.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adhiratech.chillercontrol.R;

/**
 * Created by DARWIN V TOMY on 8/31/2016.
 */

public class ValueSelector extends RelativeLayout {
    private final long REPEAT_INTERVAL_MS = 100l;
    Handler handler = new Handler();
    View rootView;
    TextView valueTextView;
    View minusButton;
    View plusButton;
    boolean plusButtonIsPressed = false;
    boolean minusButtonIsPressed = false;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private int minimumValue;
    private int maximumValue;
    private int valuedifference;
    //  private ValueChangedListner vListner;
    private ButtonclickCallBack valueButtonClickCallBack;

    public ValueSelector(Context context) {

        super(context);
        init(context);

    }

    public ValueSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ValueSelector, 0, 0);
        try {
            valuedifference = ta.getInteger(R.styleable.ValueSelector_valueDifference, 1);
            minimumValue = ta.getInteger(R.styleable.ValueSelector_minValue, Integer.MIN_VALUE);
            maximumValue = ta.getInteger(R.styleable.ValueSelector_maxValue, Integer.MAX_VALUE);
        } finally {
            ta.recycle();
        }

        init(context);
    }

    public void setButtonClickCallBack(final ButtonclickCallBack buttonClickCallBack) {
        this.valueButtonClickCallBack = buttonClickCallBack;
    }

    private void init(Context context) {
        //do setup work here

        rootView = inflate(context, R.layout.num_picker_layoput, this);
        valueTextView = (TextView) rootView.findViewById(R.id.numpickervalue);

        minusButton = rootView.findViewById(R.id.minus_button);
        plusButton = rootView.findViewById(R.id.plusbutton);
        setMinValue(minimumValue);
        setMaxValue(maximumValue);
        //  setValue(minimumValue);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue(); //we'll define this method later
                valueButtonClickCallBack.onReduceButtonClick(rootView);
            }
        });
        minusButton.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        minusButtonIsPressed = true;
                        handler.post(new AutoDecrementer());
                        return false;
                    }
                }
        );
        minusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    minusButtonIsPressed = false;
                }
                return false;
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue(); //we'll define this method later
                valueButtonClickCallBack.onAddButtonClick(rootView);
            }
        });

        plusButton.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View arg0) {
                        plusButtonIsPressed = true;
                        handler.post(new AutoIncrementer());
                        return false;
                    }
                }
        );
        plusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)) {
                    plusButtonIsPressed = false;
                }
                return false;
            }
        });
    }

    private void decrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal > minValue) {
            valueTextView.setText(String.valueOf(currentVal - valuedifference));
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return Integer.valueOf(valueTextView.getText().toString());
    }

    public void setValue(int newValue) {
        int x = newValue / valuedifference;

        int value = x * valuedifference;

        if (newValue < minValue) {
            value = minValue;
        } else if (newValue > maxValue) {
            value = maxValue;
        }

        valueTextView.setText(String.valueOf(value));
        //  vListner.ListentotheValue(value);
    }

    private void incrementValue() {
        int currentVal = Integer.valueOf(valueTextView.getText().toString());
        if (currentVal < maxValue) {
            valueTextView.setText(String.valueOf(currentVal + valuedifference));
        }
    }


    /*    public interface ValueChangedListner {

            void ListentotheValue(int value);
        }*/
    public interface ButtonclickCallBack {


        void onAddButtonClick(View add);

        void onReduceButtonClick(View minus);
    }

    private class AutoIncrementer implements Runnable {
        @Override
        public void run() {
            if (plusButtonIsPressed) {
                incrementValue();
                handler.postDelayed(new AutoIncrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

    private class AutoDecrementer implements Runnable {
        @Override
        public void run() {
            if (minusButtonIsPressed) {
                decrementValue();
                handler.postDelayed(new AutoDecrementer(), REPEAT_INTERVAL_MS);
            }
        }
    }

}