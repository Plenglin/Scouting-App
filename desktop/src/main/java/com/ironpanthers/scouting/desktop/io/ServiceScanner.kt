package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.BLUETOOTH_CHAT_UUID_RAW
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID_RAW
import com.ironpanthers.scouting.desktop.util.BT_CHAT
import com.ironpanthers.scouting.desktop.util.BT_MATCH
import java.lang.IllegalStateException
import javax.bluetooth.*
import javax.microedition.io.Connector

class ServiceScanner(val device: RemoteDevice, val onSearchCompleted: (List<ServiceRecord>) -> Unit) : DiscoveryListener {

    private val records = mutableListOf<ServiceRecord>()

    init {
        records.clear()
        LocalDevice.getLocalDevice().discoveryAgent.searchServices(
                intArrayOf(),
                arrayOf(BT_MATCH, BT_CHAT),
                device, this)
        //Connector.open()
    }

    override fun servicesDiscovered(p0: Int, p1: Array<out ServiceRecord>) {
        records.addAll(p1)
    }

    override fun serviceSearchCompleted(p0: Int, p1: Int) {
        onSearchCompleted(records)
    }

    override fun deviceDiscovered(p0: RemoteDevice?, p1: DeviceClass?) {
        throw IllegalStateException()
    }

    override fun inquiryCompleted(p0: Int) {
        throw IllegalStateException()
    }
}