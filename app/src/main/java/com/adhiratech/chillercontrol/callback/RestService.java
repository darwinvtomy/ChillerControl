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

package com.adhiratech.chillercontrol.callback;


import com.adhiratech.chillercontrol.Model.DeviceList;
import com.adhiratech.chillercontrol.Model.Devicedetail;
import com.adhiratech.chillercontrol.Rest.Constants;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Darwin v Tomy
 * @version 1.0.0
 * @date 1/22/2016
 */
public interface RestService {

    //  @POST("/v2/57ba0d9b0f00003f1ffcc926")

    @POST(Constants.HTTP.STATUS_AND_GRAPH)
    Call<Devicedetail> gettheDeviceDetailreport(@Query("d_id")String  d_id);

/*    @POST("/json-data/graph_view.php")
    Call<DeviceList> gettheDeviceslistreport(@Query("d_id")String  u_id, @Query("d_id")String  pwd);*/

    @POST(Constants.HTTP.loginURL)
    Call<DeviceList> gettheDeviceslistService(@Query("username")String  u_id, @Query("password")String  pwd);
}
