package com.example.blelibrary.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.blelibrary.BleManager;
import com.example.blelibrary.callback.BleMtuChangedCallback;
import com.example.blelibrary.callback.BleNotifyCallback;
import com.example.blelibrary.callback.BleReadCallback;
import com.example.blelibrary.callback.BleRssiCallback;
import com.example.blelibrary.data.BleMessage;
import com.example.blelibrary.data.BleWriteState;
import com.example.blelibrary.exception.GattException;
import com.example.blelibrary.exception.OtherException;
import com.example.blelibrary.exception.TimeoutException;

import java.util.UUID;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleConnector {

    private static final String UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";

    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mCharacteristic;
    private BleBluetooth mBleBluetooth;
    private Handler mHandler;

    BleConnector(BleBluetooth bleBluetooth) {
        this.mBleBluetooth = bleBluetooth;
        this.mBluetoothGatt = bleBluetooth.getBluetoothGatt();
        this.mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {

                    case BleMessage.NOTIFY_START: {
                        BleNotifyCallback notifyCallback = (BleNotifyCallback) msg.obj;
                        if (notifyCallback != null)
                            notifyCallback.onNotifyFailure(new TimeoutException());
                        break;
                    }

                    case BleMessage.NOTIFY_RESULT: {
                        notifyMsgInit();

                        BleNotifyCallback notifyCallback = (BleNotifyCallback) msg.obj;
                        Bundle bundle = msg.getData();
                        int status = bundle.getInt(BleMessage.NOTIFY_BUNDLE_STATUS);
                        if (notifyCallback != null) {
                            if (status == BluetoothGatt.GATT_SUCCESS) {
                                notifyCallback.onNotifySuccess();
                            } else {
                                notifyCallback.onNotifyFailure(new GattException(status));
                            }
                        }
                        break;
                    }

                    case BleMessage.NOTIFY_DATA_CHANGE: {
                        BleNotifyCallback notifyCallback = (BleNotifyCallback) msg.obj;
                        Bundle bundle = msg.getData();
                        byte[] value = bundle.getByteArray(BleMessage.NOTIFY_BUNDLE_VALUE);
                        if (notifyCallback != null) {
                            notifyCallback.onCharacteristicChanged(value);
                        }
                        break;
                    }

                    case BleMessage.READ_START: {
                        BleReadCallback readCallback = (BleReadCallback) msg.obj;
                        if (readCallback != null)
                            readCallback.onReadFailure(new TimeoutException());
                        break;
                    }

                    case BleMessage.READ_RESULT: {
                        readMsgInit();

                        BleReadCallback readCallback = (BleReadCallback) msg.obj;
                        Bundle bundle = msg.getData();
                        int status = bundle.getInt(BleMessage.READ_BUNDLE_STATUS);
                        byte[] value = bundle.getByteArray(BleMessage.READ_BUNDLE_VALUE);
                        if (readCallback != null) {
                            if (status == BluetoothGatt.GATT_SUCCESS) {
                                readCallback.onReadSuccess(value);
                            } else {
                                readCallback.onReadFailure(new GattException(status));
                            }
                        }
                        break;
                    }

                    case BleMessage.RSSI_START: {
                        BleRssiCallback rssiCallback = (BleRssiCallback) msg.obj;
                        if (rssiCallback != null)
                            rssiCallback.onRssiFailure(new TimeoutException());
                        break;
                    }

                    case BleMessage.RSSI_RESULT: {
                        rssiMsgInit();

                        BleRssiCallback rssiCallback = (BleRssiCallback) msg.obj;
                        Bundle bundle = msg.getData();
                        int status = bundle.getInt(BleMessage.RSSI_BUNDLE_STATUS);
                        int value = bundle.getInt(BleMessage.RSSI_BUNDLE_VALUE);
                        if (rssiCallback != null) {
                            if (status == BluetoothGatt.GATT_SUCCESS) {
                                rssiCallback.onRssiSuccess(value);
                            } else {
                                rssiCallback.onRssiFailure(new GattException(status));
                            }
                        }
                        break;
                    }

                    case BleMessage.MTU_START: {
                        BleMtuChangedCallback mtuChangedCallback = (BleMtuChangedCallback) msg.obj;
                        if (mtuChangedCallback != null)
                            mtuChangedCallback.onSetMTUFailure(new TimeoutException());
                        break;
                    }

                    case BleMessage.MTU_RESULT: {
                        mtuChangedMsgInit();

                        BleMtuChangedCallback mtuChangedCallback = (BleMtuChangedCallback) msg.obj;
                        Bundle bundle = msg.getData();
                        int status = bundle.getInt(BleMessage.MTU_BUNDLE_STATUS);
                        int value = bundle.getInt(BleMessage.MTU_BUNDLE_VALUE);
                        if (mtuChangedCallback != null) {
                            if (status == BluetoothGatt.GATT_SUCCESS) {
                                mtuChangedCallback.onMtuChanged(value);
                            } else {
                                mtuChangedCallback.onSetMTUFailure(new GattException(status));
                            }
                        }
                        break;
                    }
                }
            }
        };

    }

    private BleConnector withUUID(UUID serviceUUID, UUID characteristicUUID) {
        if (serviceUUID != null && mBluetoothGatt != null) {
            mGattService = mBluetoothGatt.getService(serviceUUID);
        }
        if (mGattService != null && characteristicUUID != null) {
            mCharacteristic = mGattService.getCharacteristic(characteristicUUID);
        }
        return this;
    }

    public BleConnector withUUIDString(String serviceUUID, String characteristicUUID) {
        return withUUID(formUUID(serviceUUID), formUUID(characteristicUUID));
    }

    private UUID formUUID(String uuid) {
        return uuid == null ? null : UUID.fromString(uuid);
    }


    /*------------------------------- main operation ----------------------------------- */


    /**
     * notify
     */
    public void enableCharacteristicNotify(BleNotifyCallback bleNotifyCallback, String uuid_notify,
                                           boolean userCharacteristicDescriptor) {
        if (mCharacteristic != null
                && (mCharacteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

            handleCharacteristicNotifyCallback(bleNotifyCallback, uuid_notify);
            setCharacteristicNotification(mBluetoothGatt, mCharacteristic, userCharacteristicDescriptor, true, bleNotifyCallback);
        } else {
            if (bleNotifyCallback != null)
                bleNotifyCallback.onNotifyFailure(new OtherException("this characteristic not support notify!"));
        }
    }

    /**
     * stop notify
     */
    public boolean disableCharacteristicNotify(boolean useCharacteristicDescriptor) {
        if (mCharacteristic != null
                && (mCharacteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            return setCharacteristicNotification(mBluetoothGatt, mCharacteristic,
                    useCharacteristicDescriptor, false, null);
        } else {
            return false;
        }
    }

    /**
     * notify setting
     */
    private boolean setCharacteristicNotification(BluetoothGatt gatt,
                                                  BluetoothGattCharacteristic characteristic,
                                                  boolean useCharacteristicDescriptor,
                                                  boolean enable,
                                                  BleNotifyCallback bleNotifyCallback) {
        if (gatt == null || characteristic == null) {
            notifyMsgInit();
            if (bleNotifyCallback != null)
                bleNotifyCallback.onNotifyFailure(new OtherException("gatt or characteristic equal null"));
            return false;
        }

        boolean success1 = gatt.setCharacteristicNotification(characteristic, enable);
        if (!success1) {
            notifyMsgInit();
            if (bleNotifyCallback != null)
                bleNotifyCallback.onNotifyFailure(new OtherException("gatt setCharacteristicNotification fail"));
            return false;
        }

        BluetoothGattDescriptor descriptor;
        if (useCharacteristicDescriptor) {
            descriptor = characteristic.getDescriptor(characteristic.getUuid());
        } else {
            descriptor = characteristic.getDescriptor(formUUID(UUID_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR));
        }
        if (descriptor == null) {
            notifyMsgInit();
            if (bleNotifyCallback != null)
                bleNotifyCallback.onNotifyFailure(new OtherException("descriptor equals null"));
            return false;
        } else {
            descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
                    BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            boolean success2 = gatt.writeDescriptor(descriptor);
            if (!success2) {
                notifyMsgInit();
                if (bleNotifyCallback != null)
                    bleNotifyCallback.onNotifyFailure(new OtherException("gatt writeDescriptor fail"));
            }
            return success2;
        }
    }

    /**
     * read
     */
    public void readCharacteristic(BleReadCallback bleReadCallback, String uuid_read) {
        if (mCharacteristic != null
                && (mCharacteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

            handleCharacteristicReadCallback(bleReadCallback, uuid_read);
            if (!mBluetoothGatt.readCharacteristic(mCharacteristic)) {
                readMsgInit();
                if (bleReadCallback != null)
                    bleReadCallback.onReadFailure(new OtherException("gatt readCharacteristic fail"));
            }
        } else {
            if (bleReadCallback != null)
                bleReadCallback.onReadFailure(new OtherException("this characteristic not support read!"));
        }
    }

    /**
     * rssi
     */
    public void readRemoteRssi(BleRssiCallback bleRssiCallback) {
        handleRSSIReadCallback(bleRssiCallback);
        if (!mBluetoothGatt.readRemoteRssi()) {
            rssiMsgInit();
            if (bleRssiCallback != null)
                bleRssiCallback.onRssiFailure(new OtherException("gatt readRemoteRssi fail"));
        }
    }

    /**
     * set mtu
     */
    public void setMtu(int requiredMtu, BleMtuChangedCallback bleMtuChangedCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handleSetMtuCallback(bleMtuChangedCallback);
            if (!mBluetoothGatt.requestMtu(requiredMtu)) {
                mtuChangedMsgInit();
                if (bleMtuChangedCallback != null)
                    bleMtuChangedCallback.onSetMTUFailure(new OtherException("gatt requestMtu fail"));
            }
        } else {
            if (bleMtuChangedCallback != null)
                bleMtuChangedCallback.onSetMTUFailure(new OtherException("API level lower than 21"));
        }
    }

    /**
     * requestConnectionPriority
     *
     * @param connectionPriority Request a specific connection priority. Must be one of
     *                           {@link BluetoothGatt#CONNECTION_PRIORITY_BALANCED},
     *                           {@link BluetoothGatt#CONNECTION_PRIORITY_HIGH}
     *                           or {@link BluetoothGatt#CONNECTION_PRIORITY_LOW_POWER}.
     * @throws IllegalArgumentException If the parameters are outside of their
     *                                  specified range.
     */
    public boolean requestConnectionPriority(int connectionPriority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mBluetoothGatt.requestConnectionPriority(connectionPriority);
        }
        return false;
    }


    /**************************************** Handle call back ******************************************/

    /**
     * notify
     */
    private void handleCharacteristicNotifyCallback(BleNotifyCallback bleNotifyCallback,
                                                    String uuid_notify) {
        if (bleNotifyCallback != null) {
            notifyMsgInit();
            bleNotifyCallback.setKey(uuid_notify);
            bleNotifyCallback.setHandler(mHandler);
            mBleBluetooth.addNotifyCallback(uuid_notify, bleNotifyCallback);
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(BleMessage.NOTIFY_START, bleNotifyCallback),
                    BleManager.getInstance().getOperateTimeout());
        }
    }

    /**
     * read
     */
    private void handleCharacteristicReadCallback(BleReadCallback bleReadCallback,
                                                  String uuid_read) {
        if (bleReadCallback != null) {
            readMsgInit();
            bleReadCallback.setKey(uuid_read);
            bleReadCallback.setHandler(mHandler);
            mBleBluetooth.addReadCallback(uuid_read, bleReadCallback);
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(BleMessage.READ_START, bleReadCallback),
                    BleManager.getInstance().getOperateTimeout());
        }
    }

    /**
     * rssi
     */
    private void handleRSSIReadCallback(BleRssiCallback bleRssiCallback) {
        if (bleRssiCallback != null) {
            rssiMsgInit();
            bleRssiCallback.setHandler(mHandler);
            mBleBluetooth.addRssiCallback(bleRssiCallback);
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(BleMessage.RSSI_START, bleRssiCallback),
                    BleManager.getInstance().getOperateTimeout());
        }
    }

    /**
     * set mtu
     */
    private void handleSetMtuCallback(BleMtuChangedCallback bleMtuChangedCallback) {
        if (bleMtuChangedCallback != null) {
            mtuChangedMsgInit();
            bleMtuChangedCallback.setHandler(mHandler);
            mBleBluetooth.addMtuChangedCallback(bleMtuChangedCallback);
            mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(BleMessage.MTU_START, bleMtuChangedCallback),
                    BleManager.getInstance().getOperateTimeout());
        }
    }

    public void notifyMsgInit() {
        mHandler.removeMessages(BleMessage.NOTIFY_START);
    }

    public void readMsgInit() {
        mHandler.removeMessages(BleMessage.READ_START);
    }

    public void rssiMsgInit() {
        mHandler.removeMessages(BleMessage.RSSI_START);
    }

    public void mtuChangedMsgInit() {
        mHandler.removeMessages(BleMessage.MTU_START);
    }

}