
package com.adhiratech.chillercontrol.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Devicedetail  {

    @SerializedName("cur_device")
    @Expose
    private CurDevice curDevice;

    /**
     * 
     * @return
     *     The curDevice
     */
    public CurDevice getCurDevice() {
        return curDevice;
    }

    /**
     * 
     * @param curDevice
     *     The cur_device
     */
    public void setCurDevice(CurDevice curDevice) {
        this.curDevice = curDevice;
    }

}
