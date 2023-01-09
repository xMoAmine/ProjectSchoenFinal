package com.example.blelibrary.data;

public class BleMessage {

    // Scan
    public static final int SCAN_DEVICE = 0X00;

    // Connect
    public static final int CONNECT_FAIL = 0x01;
    public static final int DISCONNECTED = 0x02;
    public static final int RECONNECT = 0x03;
    public static final int DISCOVER_SERVICES = 0x04;
    public static final int DISCOVER_FAIL = 0x05;
    public static final int DISCOVER_SUCCESS = 0x06;
    public static final int CONNECT_OVER_TIME = 0x07;

    // Notify
    public static final int NOTIFY_START = 0x11;
    public static final int NOTIFY_RESULT = 0x12;
    public static final int NOTIFY_DATA_CHANGE = 0x13;
    public static final String NOTIFY_BUNDLE_STATUS = "notify_status";
    public static final String NOTIFY_BUNDLE_VALUE = "notify_value";

    // Read
    public static final int READ_START = 0x41;
    public static final int READ_RESULT = 0x42;
    public static final String READ_BUNDLE_STATUS = "read_status";
    public static final String READ_BUNDLE_VALUE = "read_value";

    // Rssi
    public static final int RSSI_START = 0x51;
    public static final int RSSI_RESULT = 0x52;
    public static final String RSSI_BUNDLE_STATUS = "rssi_status";
    public static final String RSSI_BUNDLE_VALUE = "rssi_value";

    // Mtu
    public static final int MTU_START = 0x61;
    public static final int MTU_RESULT = 0x62;
    public static final String MTU_BUNDLE_STATUS = "mtu_status";
    public static final String MTU_BUNDLE_VALUE = "mtu_value";



}