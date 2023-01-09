package com.example.projectschoenfinal.comm;


import com.example.blelibrary.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
