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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adhiratech.chillercontrol.Model.DeviceList;
import com.adhiratech.chillercontrol.Rest.RestManager;
import com.adhiratech.chillercontrol.Rest.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    Utils utils;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private RestManager loginManager;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //   private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mDeviceIDView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //  private String loginURL;
    private DeviceList theDeviceList;
    // private boolean LOGGED_IN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // loginURL = loginURL;
        loginManager = new RestManager();
        // Set up the login form.
        mDeviceIDView = (AutoCompleteTextView) findViewById(R.id.deviceID);
        //   populateAutoComplete();
        utils = (Utils) getApplicationContext();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mDeviceSignInButton = (Button) findViewById(R.id.sign_in_button);
        mDeviceSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        sharedPref = this.getSharedPreferences(getString(R.string.MYKEY),Context.MODE_PRIVATE);
        editor = sharedPref.edit();

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mDeviceIDView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userID = mDeviceIDView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userID address.
        if (TextUtils.isEmpty(userID)) {
            mDeviceIDView.setError(getString(R.string.error_field_required));
            focusView = mDeviceIDView;
            cancel = true;
        } else if (!isdeviceIDValid(userID)) {
            mDeviceIDView.setError(getString(R.string.error_invalid_userid));
            focusView = mDeviceIDView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //    mAuthTask = new ConfigSubmmitTask(userID, password);

            utils.setUSR_ID(userID);
            utils.setPASSWORD(password);
            loadFlowerFeed();

        }
    }

    private boolean isdeviceIDValid(String ID) {
        //TODO: Replace this with your own logic
        return (ID.length() > 2);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    private void loadFlowerFeed() {


        if (getNetworkAvailability()) {
            getFeed();
        } else {
            Toast.makeText(LoginActivity.this, "Network Not avaliable", Toast.LENGTH_SHORT).show();
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
                    editor.putBoolean(getString(R.string.LOGGED_IN), true);
                    editor.putString(getString(R.string.USERNAME_KEY), utils.getUSR_ID());
                    editor.putString(getString(R.string.PASSWORD_KEY), utils.getPASSWORD());
                    editor.apply();

                    Log.i("PREFS", "Updated ? " + sharedPref.getBoolean(getString(R.string.LOGGED_IN), false));
                    showProgress(false);
                    Intent deviceList = new Intent(LoginActivity.this, DevicesListActivity.class);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

