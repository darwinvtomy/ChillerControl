package com.adhiratech.chillercontrol.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class CurDevice {

    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("tmp1")
    @Expose
    private String tmp1;
    @SerializedName("tmp2")
    @Expose
    private String tmp2;
    @SerializedName("psr1")
    @Expose
    private String psr1;
    @SerializedName("psr2")
    @Expose
    private String psr2;
    @SerializedName("sdatetme")
    @Expose
    private String sdatetme;
    @SerializedName("flstatus")
    @Expose
    private String flstatus;
    @SerializedName("motor")
    @Expose
    private String motor;
    @SerializedName("overld")
    @Expose
    private String overld;
    @SerializedName("injection_time")
    @Expose
    private String injectionTime;
    @SerializedName("break_time")
    @Expose
    private String breakTime;
    @SerializedName("retrieval_time")
    @Expose
    private String retrievalTime;
    @SerializedName("reset_time")
    @Expose
    private String resetTime;
    @SerializedName("cycles")
    @Expose
    private String cycles;
    @SerializedName("timelabels")
    @Expose
    private List<String> timelabels = new ArrayList<String>();
    @SerializedName("dtemp1")
    @Expose
    private List<Float> dtemp1 = new ArrayList<Float>();
    @SerializedName("dtemp2")
    @Expose
    private List<Float> dtemp2 = new ArrayList<Float>();
    @SerializedName("dpsr1")
    @Expose
    private List<Float> dpsr1 = new ArrayList<Float>();
    @SerializedName("dpsr2")
    @Expose
    private List<Float> dpsr2 = new ArrayList<Float>();

    /**
     * @return The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return The mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode The mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return The tmp1
     */
    public String getTmp1() {
        return tmp1;
    }

    /**
     * @param tmp1 The tmp1
     */
    public void setTmp1(String tmp1) {
        this.tmp1 = tmp1;
    }

    /**
     * @return The tmp2
     */
    public String getTmp2() {
        return tmp2;
    }

    /**
     * @param tmp2 The tmp2
     */
    public void setTmp2(String tmp2) {
        this.tmp2 = tmp2;
    }

    /**
     * @return The psr1
     */
    public String getPsr1() {
        return psr1;
    }

    /**
     * @param psr1 The psr1
     */
    public void setPsr1(String psr1) {
        this.psr1 = psr1;
    }

    /**
     * @return The psr2
     */
    public String getPsr2() {
        return psr2;
    }

    /**
     * @param psr2 The psr2
     */
    public void setPsr2(String psr2) {
        this.psr2 = psr2;
    }

    /**
     * @return The sdatetme
     */
    public String getSdatetme() {
        return sdatetme;
    }

    /**
     * @param sdatetme The sdatetme
     */
    public void setSdatetme(String sdatetme) {
        this.sdatetme = sdatetme;
    }

    /**
     * @return The flstatus
     */
    public String getFlstatus() {
        return flstatus;
    }

    /**
     * @param flstatus The flstatus
     */
    public void setFlstatus(String flstatus) {
        this.flstatus = flstatus;
    }

    /**
     * @return The motor
     */
    public String getMotor() {
        return motor;
    }

    /**
     * @param motor The motor
     */
    public void setMotor(String motor) {
        this.motor = motor;
    }

    /**
     * @return The overld
     */
    public String getOverld() {
        return overld;
    }

    /**
     * @param overld The overld
     */
    public void setOverld(String overld) {
        this.overld = overld;
    }

    /**
     * @return The injectionTime
     */
    public String getInjectionTime() {
        return injectionTime;
    }

    /**
     * @param injectionTime The injection_time
     */
    public void setInjectionTime(String injectionTime) {
        this.injectionTime = injectionTime;
    }

    /**
     * @return The breakTime
     */
    public String getBreakTime() {
        return breakTime;
    }

    /**
     * @param breakTime The break_time
     */
    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    /**
     * @return The retrievalTime
     */
    public String getRetrievalTime() {
        return retrievalTime;
    }

    /**
     * @param retrievalTime The retrieval_time
     */
    public void setRetrievalTime(String retrievalTime) {
        this.retrievalTime = retrievalTime;
    }

    /**
     * @return The resetTime
     */
    public String getResetTime() {
        return resetTime;
    }

    /**
     * @param resetTime The reset_time
     */
    public void setResetTime(String resetTime) {
        this.resetTime = resetTime;
    }

    /**
     * @return The cycles
     */
    public String getCycles() {
        return cycles;
    }

    /**
     * @param cycles The cycles
     */
    public void setCycles(String cycles) {
        this.cycles = cycles;
    }

    /**
     * @return The timelabels
     */
    public List<String> getTimelabels() {
        return timelabels;
    }

    /**
     * @param timelabels The timelabels
     */
    public void setTimelabels(List<String> timelabels) {
        this.timelabels = timelabels;
    }

    /**
     * @return The dtemp1
     */
    public List<Float> getDtemp1() {
        return dtemp1;
    }

    /**
     * @param dtemp1 The dtemp1
     */
    public void setDtemp1(List<Float> dtemp1) {
        this.dtemp1 = dtemp1;
    }

    /**
     * @return The dtemp2
     */
    public List<Float> getDtemp2() {
        return dtemp2;
    }

    /**
     * @param dtemp2 The dtemp2
     */
    public void setDtemp2(List<Float> dtemp2) {
        this.dtemp2 = dtemp2;
    }

    /**
     * @return The dpsr1
     */
    public List<Float> getDpsr1() {
        return dpsr1;
    }

    /**
     * @param dpsr1 The dpsr1
     */
    public void setDpsr1(List<Float> dpsr1) {
        this.dpsr1 = dpsr1;
    }

    /**
     * @return The dpsr2
     */
    public List<Float> getDpsr2() {
        return dpsr2;
    }

    /**
     * @param dpsr2 The dpsr2
     */
    public void setDpsr2(List<Float> dpsr2) {
        this.dpsr2 = dpsr2;
    }

}
