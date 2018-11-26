package com.ironpanthers.scouting.desktop.io

import com.ironpanthers.scouting.desktop.util.BT_CHAT
import com.ironpanthers.scouting.desktop.util.BT_MATCH
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import tornadofx.getValue
import tornadofx.setValue
import javax.bluetooth.*


typealias SearchCompletedListener = (BluetoothPeer) -> Unit

class BluetoothPeer(val device: RemoteDevice) : DiscoveryListener {
    val availableServices = FXCollections.observableArrayList<ServiceRecord>()

    val searchingProperty = SimpleBooleanProperty(false)
    var searching by searchingProperty
    private val records = mutableListOf<ServiceRecord>()

    fun findServices() {
        if (searching) {
            throw IllegalStateException()
        }
        searching = true
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
        searching = false
    }

    override fun deviceDiscovered(p0: RemoteDevice?, p1: DeviceClass?) {
        throw IllegalStateException()
    }

    override fun inquiryCompleted(p0: Int) {
        throw IllegalStateException()
    }

}