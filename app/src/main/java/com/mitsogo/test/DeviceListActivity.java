package com.mitsogo.test;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.mitsogo.test.Adapters.DeviceListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceListActivity extends AppCompatActivity {

    @BindView(R.id.devices_list)
    RecyclerView mDevicesListRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        initRecyclerView();
        initAdapter();
    }

    // Initialize the recyclerview
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mDevicesListRecycler.setLayoutManager(linearLayoutManager);
        mDevicesListRecycler.addItemDecoration(dividerItemDecoration);
    }

    // Initialize and set the adapter
    private void initAdapter() {
        ArrayList<BluetoothDevice> deviceItemsList = getIntent().getParcelableArrayListExtra("devices");
        if (deviceItemsList != null) {
            DeviceListAdapter deviceListAdapter = new DeviceListAdapter(this, deviceItemsList);
            mDevicesListRecycler.setAdapter(deviceListAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_scan) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
