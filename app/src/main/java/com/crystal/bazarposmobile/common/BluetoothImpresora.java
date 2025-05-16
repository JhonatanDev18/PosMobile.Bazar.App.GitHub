package com.crystal.bazarposmobile.common;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;

public class BluetoothImpresora {
    private static BluetoothImpresora instance = null;
    private Connection connection;

    public BluetoothImpresora(String mac) {
        connection = new BluetoothConnection(mac);
    }

    public static BluetoothImpresora getInstance(String mac){

        if(instance == null) {
            instance = new BluetoothImpresora(mac);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
