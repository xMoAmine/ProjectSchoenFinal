package com.example.blelibrary.callback;


import com.example.blelibrary.data.BleDevice;

import java.util.List;

public abstract class BleScanCallback implements BleScanPresenterImp {

    public abstract void onScanFinished(List<BleDevice> scanResultList);

    public void onLeScan(BleDevice bleDevice) {
    }
}
