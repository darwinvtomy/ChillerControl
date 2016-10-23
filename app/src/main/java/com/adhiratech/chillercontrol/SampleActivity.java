package com.adhiratech.chillercontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adhiratech.chillercontrol.Components.ValueSelector;

public class SampleActivity extends AppCompatActivity implements ValueSelector.ButtonclickCallBack {

    ValueSelector listrefresher, statusrefresher;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    int REFRESH_DEFAULT_VALUE, list_refresh_value, status_refresh_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listrefresher = (ValueSelector) findViewById(R.id.list_refresh_selector);
        statusrefresher = (ValueSelector) findViewById(R.id.status_refresh_selector);


        listrefresher.setButtonClickCallBack(this);
        statusrefresher.setButtonClickCallBack(this);

        sharedPref = this.getSharedPreferences(getString(R.string.MYKEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        REFRESH_DEFAULT_VALUE = getResources().getInteger(R.integer.refresh_default_time);
        list_refresh_value = sharedPref.getInt(getString(R.string.status_refresh_time), REFRESH_DEFAULT_VALUE);
        status_refresh_value = sharedPref.getInt(getString(R.string.list_refresh_time), REFRESH_DEFAULT_VALUE);

        listrefresher.setValue(list_refresh_value);
        statusrefresher.setValue(status_refresh_value);

    }

    @Override
    public void onAddButtonClick(View add) {
        switch (add.getId()) {
            case R.id.list_refresh_selector:
                updateTheListRefresh(listrefresher.getValue());
                break;
            case R.id.status_refresh_selector:
                updateTheStatusRefresh(statusrefresher.getValue());
                break;
        }
    }


    @Override
    public void onReduceButtonClick(View minus) {

        switch (minus.getId()) {
            case R.id.list_refresh_selector:
                updateTheListRefresh(listrefresher.getValue());
                break;
            case R.id.status_refresh_selector:
                updateTheStatusRefresh(statusrefresher.getValue());
                break;
        }
    }

    private void updateTheStatusRefresh(int value) {
        editor.putInt(getString(R.string.status_refresh_time), value);
        editor.apply();

    }

    private void updateTheListRefresh(int value) {
        editor.putInt(getString(R.string.list_refresh_time), value);
        editor.apply();

    }
}
