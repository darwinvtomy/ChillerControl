/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package com.adhiratech.chillercontrol.Rest;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.adhiratech.chillercontrol.Model.DeviceList;
import com.adhiratech.chillercontrol.Model.DevicesFound;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author Filippo Engidashet
 * @version 1.0
 * @date today
 */
public class Utils extends Application {


    private String DEVICE_ID;
    private String LOGIN_LEVEL;
    private String USR_ID;
    private String PASSWORD;
    private DeviceList DEVICE_LIST;
    private DevicesFound SELECTED_DEVICE = null;
    private int LIST_REFRESH_TIME, STATUS_REFRESH_TIME;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static byte[] getPictureByteOfArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmapFromByte(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String durationInSecondsToString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec / 60) - (hours * 60);
        int seconds = sec - (hours * 3600) - (minutes * 60);
        String formatted = String.format("%d:%02d:%02d", hours, minutes, seconds);
        return formatted;
    }


    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getLOGIN_LEVEL() {
        return LOGIN_LEVEL;
    }

    public void setLOGIN_LEVEL(String LOGIN_LEVEL) {
        this.LOGIN_LEVEL = LOGIN_LEVEL;
    }

    public String getUSR_ID() {
        return USR_ID;
    }

    public void setUSR_ID(String USR_ID) {
        this.USR_ID = USR_ID;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public DeviceList getDEVICE_LIST() {
        return DEVICE_LIST;
    }

    public void setDEVICE_LIST(DeviceList DEVICE_LIST) {
        this.DEVICE_LIST = DEVICE_LIST;
    }

    public List<DevicesFound> getTheDeviceFoundList() {
        return DEVICE_LIST.getDevicesFound();
    }

    public DevicesFound getSELECTED_DEVICE() {
        return SELECTED_DEVICE;
    }

    public void setSELECTED_DEVICE(DevicesFound SELECTED_DEVICE) {
        this.SELECTED_DEVICE = SELECTED_DEVICE;
    }

    public int getLIST_REFRESH_TIME() {
        return LIST_REFRESH_TIME;
    }

    public void setLIST_REFRESH_TIME(int LIST_REFRESH_TIME) {
        this.LIST_REFRESH_TIME = LIST_REFRESH_TIME;
    }

    public int getSTATUS_REFRESH_TIME() {
        return STATUS_REFRESH_TIME;
    }

    public void setSTATUS_REFRESH_TIME(int STATUS_REFRESH_TIME) {
        this.STATUS_REFRESH_TIME = STATUS_REFRESH_TIME;
    }
}
