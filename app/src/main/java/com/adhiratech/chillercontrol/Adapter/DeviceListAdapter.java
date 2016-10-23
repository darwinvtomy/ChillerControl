package com.adhiratech.chillercontrol.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adhiratech.chillercontrol.Model.DevicesFound;
import com.adhiratech.chillercontrol.R;

import java.util.List;

/**
 * Created by DARWIN V TOMY on 9/8/2016.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DerpHolder> {

    private static String[] device_Modes;
    Resources resources;
    private List<DevicesFound> listData;
    private LayoutInflater inflater;
    private ItemClickCallBack itemClickCallBack;

    public DeviceListAdapter(List<DevicesFound> listDatas, Resources resources, Context context) {
        this.resources = resources;
        device_Modes = resources.getStringArray(R.array.modes);
        this.inflater = LayoutInflater.from(context);
        this.listData = listDatas;
    }

    public void setItemClickCallBack(final ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.device_item_status, parent, false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder holder, int position) {
        DevicesFound item = listData.get(position);

        holder.devicename.setText(item.getD_name());
        holder.txt_ID.setText(item.getDeviceId());
        holder.Location.setText(item.getLocat());
        holder.mode.setText(device_Modes[Integer.parseInt(item.getMode())]);
        holder.temp1.setText(item.getTmp1());
        holder.psr1.setText(item.getPsr1());
        holder.temp2.setText(item.getTmp2());
        holder.psr2.setText(item.getPsr2());

        if (item.getFlstatus().trim().equals("0")) {
            holder.floststus.setTextColor(resources.getColor(R.color.off_Color));
            holder.floststus.setText("OFF");

        } else {
            holder.floststus.setTextColor(resources.getColor(R.color.on_Color));
            holder.floststus.setText("ON");
        }
        if (item.getMotor().trim().equals("0")) {
            holder.motorststus.setTextColor(resources.getColor(R.color.off_Color));
            holder.motorststus.setText("OFF");

        } else {
            holder.motorststus.setTextColor(resources.getColor(R.color.on_Color));
            holder.motorststus.setText("ON");
        }
        if (item.getOverld().trim().equals("0")) {
            holder.overload.setTextColor(resources.getColor(R.color.off_Color));
            holder.overload.setText("OVERLOAD");

        } else {
            holder.overload.setTextColor(resources.getColor(R.color.on_Color));
            holder.overload.setText("NO");
        }

        holder.injectiontime.setText(item.getInjectionTime());
        holder.retrivaltime.setText(item.getRetrievalTime());
        holder.breaktime.setText(item.getBreakTime());
        holder.cycles.setText(item.getCycles());
        holder.restTime.setText(item.getResetTime());
        //   holder.txt_ID.setText(item.getDeviceId());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public interface ItemClickCallBack {

        void onItemClick(int listitem);

        void onViewButtonClick(int viewButton);

        void onConfigureButtonClick(int confButton);
    }

    class DerpHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView devicename, txt_ID, Location, mode, temp1, psr1, temp2, psr2, floststus,
                motorststus, overload, injectiontime, retrivaltime, breaktime, cycles, restTime;
        private RelativeLayout viewStatusButton, configure;
        private View container;

        public DerpHolder(View itemView) {
            super(itemView);

            viewStatusButton = (RelativeLayout) itemView.findViewById(R.id.view_status_button_lst);
            configure = (RelativeLayout) itemView.findViewById(R.id.configure_button_lst);

            devicename = (TextView) itemView.findViewById(R.id.device_name_list);
            txt_ID = (TextView) itemView.findViewById(R.id.item_id);
            Location = (TextView) itemView.findViewById(R.id.device_loca_lst);
            mode = (TextView) itemView.findViewById(R.id.device_mode_lst);
            temp1 = (TextView) itemView.findViewById(R.id.temp1_lst);
            psr1 = (TextView) itemView.findViewById(R.id.psr1_lst);
            temp2 = (TextView) itemView.findViewById(R.id.temp2_lst);
            psr2 = (TextView) itemView.findViewById(R.id.psr2_lst);
            floststus = (TextView) itemView.findViewById(R.id.fl_status_lst);
            motorststus = (TextView) itemView.findViewById(R.id.moter_status_lst);
            overload = (TextView) itemView.findViewById(R.id.overload_lst);
            injectiontime = (TextView) itemView.findViewById(R.id.injectiontime_lst);
            retrivaltime = (TextView) itemView.findViewById(R.id.rtrival_time_lst);
            breaktime = (TextView) itemView.findViewById(R.id.break_time_lst);
            cycles = (TextView) itemView.findViewById(R.id.cycles_lst);
            restTime = (TextView) itemView.findViewById(R.id.rest_time_lst);
            container = itemView.findViewById(R.id.device_item_list);

            viewStatusButton.setOnClickListener(this);
            configure.setOnClickListener(this);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_status_button_lst: {
                    itemClickCallBack.onViewButtonClick(getAdapterPosition());
                }
                break;
                case R.id.configure_button_lst: {
                    itemClickCallBack.onConfigureButtonClick(getAdapterPosition());
                }
                break;
                case R.id.device_item_list: {
                    itemClickCallBack.onItemClick(getAdapterPosition());
                }
                break;
            }
        }
    }
}
