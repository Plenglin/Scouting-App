package com.ironpanthers.scouting.desktop.io

import javafx.collections.FXCollections
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import javax.bluetooth.*

object PeerDiscoveryManager {
    val logger = LoggerFactory.getLogger(javaClass)!!
    private var paired: List<BluetoothPeer> = emptyList()
    private var adhoc: Queue<BluetoothPeer> = ConcurrentLinkedDeque()

    val peers = FXCollections.observableArrayList<BluetoothPeer>()

    private val ld = LocalDevice.getLocalDevice()

    fun refreshPairedDevices() {
        logger.debug("Refreshing paired devices")
        paired = LocalDevice.getLocalDevice().discoveryAgent.retrieveDevices(DiscoveryAgent.PREKNOWN)
                ?.map { BluetoothPeer(it) } ?: emptyList()
        updateCombinedList()
    }

    private fun updateCombinedList() {
        peers.setAll(paired + adhoc)
    }

}