package com.example.blelibrary.data;


public class BleConnectStateParameter {

    private int status;
    private boolean isActive;

    public BleConnectStateParameter(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
