package com.ironpanthers.scouting.desktop.io

import javafx.collections.FXCollections
import org.slf4j.LoggerFactory
import javax.bluetooth.DiscoveryAgent
import javax.bluetooth.LocalDevice

object PeerDiscoveryManager {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val peers = FXCollections.observableArrayList<BluetoothPeer>()

    private val ld = LocalDevice.getLocalDevice()

    fun refreshPairedDevices() {
        logger.debug("Refreshing paired devices")
        val devs = LocalDevice.getLocalDevice().discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN) ?: emptyArray()
        peers.setAll(devs.map { dev ->
            BluetoothPeer(dev)
        })
    }

}