package com.mitsogo.test;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.mitsogo.test.CustomComponents.CustomTextView;
import com.mitsogo.test.Utils.BluetoothScannerUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_outer_circle)
    ImageView mImgOuterCircle;

    @BindView(R.id.img_inner_circle)
    ImageView mImgInnerCircle;

    @BindView(R.id.img_center_circle)
    ImageView mImgCenterCircle;

    @BindView(R.id.txt_scan)
    CustomTextView mTxtScan;

    @BindView(R.id.txt_scan_status)
    CustomTextView mTxtScanStatus;

    private BluetoothAdapter mBluetoothAdapter;

    private AnimatorSet mOuterCircleAnimation;
    private AnimatorSet mInnerCircleAnimation;

    private int mDeviceCount = 0;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    private static final int PERMISSION_REQUEST_LOCATION = 100;
    private static final int PERMISSION_REQUEST_BLUETOOTH = 101;

    private boolean mIsScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initBluetoothAdapter();

        // Initialize animations
        mOuterCircleAnimation = initAnimation(mImgOuterCircle);
        mInnerCircleAnimation = initAnimation(mImgInnerCircle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelDiscovery();
    }

    // Initialize the bluetooth settings on device
    private void initBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Bluetooth is not supported
            BluetoothScannerUtil.showSnackbar(mImgCenterCircle, R.string.bluetooth_unsupported_error);
        }
    }

    // Start scanning for nearby bluetooth devices
    private void discoverDevices() {
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, bluetoothFilter);

        mIsScanning = true;

        mOuterCircleAnimation.start();
        mInnerCircleAnimation.start();
        mTxtScanStatus.setText(R.string.scanning_for_devices);
        mTxtScanStatus.setVisibility(View.VISIBLE);
        mTxtScan.setText(R.string.stop);
        mBluetoothAdapter.startDiscovery();

    }

    // Cancel bluetooth scan
    private void cancelDiscovery() {
        mBluetoothAdapter.cancelDiscovery();

        try {
            unregisterReceiver(bluetoothReceiver);
        } catch (IllegalArgumentException e) {
            Log.e("Error", "Receiever already unregistered");
        }

        if (mOuterCircleAnimation.isRunning()) {
            mOuterCircleAnimation.cancel();
        }

        if (mInnerCircleAnimation.isRunning()) {
            mInnerCircleAnimation.cancel();
        }

        mTxtScan.setText(R.string.scan);

        mIsScanning = false;
        mDeviceCount = 0;
        mDevices.clear();
    }

    // Initialize the scanning animation
    private AnimatorSet initAnimation(ImageView imageView) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1, 1.2f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1, 1.2f);

        scaleXAnimator.setDuration(1000);
        scaleYAnimator.setDuration(1000);

        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 1, 0.5f);
        alphaAnimator.setDuration(1000);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimator)
                .with(scaleYAnimator)
                .with(alphaAnimator);

        return animatorSet;
    }

    // Unregister the broadcast receiver
    private void unregisterReceiver() {
        try {
        } catch (IllegalArgumentException e) {
            Log.e("Error", "Receieve already unregistered");
        }
    }

    @OnClick(R.id.img_center_circle)
    public void onScanClick(View view) {
        if (mIsScanning) {
            // Stop scanning

            if (mDeviceCount == 0) {
                mTxtScanStatus.setText(R.string.no_devices_found);
            } else {
                mTxtScanStatus.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                intent.putExtra("devices", mDevices);
                startActivity(intent);
            }

            cancelDiscovery();

        } else {
            // Start scanning

            if (BluetoothScannerUtil.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    BluetoothScannerUtil.isPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                if (mBluetoothAdapter.isEnabled()) {
                    discoverDevices();
                } else {
                    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(bluetoothIntent, PERMISSION_REQUEST_BLUETOOTH);
                }

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH && resultCode == RESULT_OK) {
            discoverDevices();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION && grantResults.length == 2) {
            mTxtScan.performClick();
        }
    }


    // Broadcast receiver , triggers when a blueooth device is found nearby
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a new device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !mDevices.contains(device)) {
                    mDevices.add(device);

                    mDeviceCount++;
                    String deviceCountMsg = String.format(getString(R.string.device_found_count), mDeviceCount);
                    mTxtScanStatus.setText(deviceCountMsg);
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mDeviceCount == 0) {
                    mTxtScanStatus.setText(R.string.no_devices_found);
                } else {
                    mTxtScanStatus.setVisibility(View.INVISIBLE);
                    Intent deviceListIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                    deviceListIntent.putExtra("devices", mDevices);
                    startActivity(deviceListIntent);
                }

                cancelDiscovery();
            }

        }
    };

}
