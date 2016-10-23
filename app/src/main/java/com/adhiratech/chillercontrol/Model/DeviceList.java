
package com.adhiratech.chillercontrol.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class DeviceList  {

    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("login_lvl")
    @Expose
    private String loginLvl;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("usrtype")
    @Expose
    private String usrtype;
    @SerializedName("noofdevices")
    @Expose
    private String noofdevices;
    @SerializedName("devices_found")
    @Expose
    private List<DevicesFound> devicesFound = new ArrayList<DevicesFound>();

    /**
     * 
     * @return
     *     The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 
     * @param uid
     *     The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 
     * @return
     *     The loginLvl
     */
    public String getLoginLvl() {
        return loginLvl;
    }

    /**
     * 
     * @param loginLvl
     *     The login_lvl
     */
    public void setLoginLvl(String loginLvl) {
        this.loginLvl = loginLvl;
    }

    /**
     * 
     * @return
     *     The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 
     * @param msg
     *     The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 
     * @return
     *     The usrtype
     */
    public String getUsrtype() {
        return usrtype;
    }

    /**
     * 
     * @param usrtype
     *     The usrtype
     */
    public void setUsrtype(String usrtype) {
        this.usrtype = usrtype;
    }

    /**
     * 
     * @return
     *     The noofdevices
     */
    public String getNoofdevices() {
        return noofdevices;
    }

    /**
     * 
     * @param noofdevices
     *     The noofdevices
     */
    public void setNoofdevices(String noofdevices) {
        this.noofdevices = noofdevices;
    }

    /**
     * 
     * @return
     *     The devicesFound
     */
    public List<DevicesFound> getDevicesFound() {
        return devicesFound;
    }

    /**
     * 
     * @param devicesFound
     *     The devices_found
     */
    public void setDevicesFound(List<DevicesFound> devicesFound) {
        this.devicesFound = devicesFound;
    }

}
