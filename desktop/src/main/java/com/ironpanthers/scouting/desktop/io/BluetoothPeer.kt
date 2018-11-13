package com.ironpanthers.scouting.desktop.io

import javafx.collections.FXCollections
import javax.bluetooth.RemoteDevice
import javax.bluetooth.ServiceRecord


class BluetoothPeer(val remoteDevice: RemoteDevice) {
    val availableServices = FXCollections.observableArrayList<ServiceRecord>()
}