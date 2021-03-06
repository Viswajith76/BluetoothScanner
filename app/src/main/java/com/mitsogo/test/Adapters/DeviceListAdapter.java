package com.mitsogo.test.Adapters;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mitsogo.test.CustomComponents.CustomTextView;
import com.mitsogo.test.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by viswajith on 15/9/17.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceItemHolder> {

    private Context mContext;
    private ArrayList<BluetoothDevice> mDeviceItems;

    public DeviceListAdapter(Context context, ArrayList<BluetoothDevice> deviceItems) {
        this.mContext = context;
        this.mDeviceItems = deviceItems;
    }

    @Override
    public DeviceListAdapter.DeviceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_device_item, parent, false);
        return new DeviceItemHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceListAdapter.DeviceItemHolder holder, int position) {
        BluetoothDevice bluetoothDevice = mDeviceItems.get(position);
        if (bluetoothDevice != null) {

            BluetoothClass bluetoothClass = bluetoothDevice.getBluetoothClass();
            int deviceType = bluetoothClass.getMajorDeviceClass();

            holder.mTxtDeviceName.setText(bluetoothDevice.getName());
            holder.mTxtDeviceAddress.setText(bluetoothDevice.getAddress());
            holder.mImgDeviceType.setImageDrawable(getImageTypeDrawable(deviceType));
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceItems.size();
    }

    private Drawable getImageTypeDrawable(int deviceType) {
        Drawable drawable = null;
        switch (deviceType) {
            case BluetoothClass.Device.Major.COMPUTER:
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_laptop);
                break;

            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_headset);
                break;

            case BluetoothClass.Device.Major.PHONE:
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_phone);
                break;

            default:
                drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_bluetooth);
                break;
        }
        return drawable;
    }

    public class DeviceItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_device_type)
        ImageView mImgDeviceType;

        @BindView(R.id.txt_device_name)
        CustomTextView mTxtDeviceName;

        @BindView(R.id.txt_device_address)
        CustomTextView mTxtDeviceAddress;

        public DeviceItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
