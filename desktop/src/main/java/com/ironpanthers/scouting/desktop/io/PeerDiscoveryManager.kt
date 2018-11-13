package com.ironpanthers.scouting.desktop.io

import javafx.collections.FXCollections
import org.slf4j.LoggerFactory
import javax.bluetooth.*

object PeerDiscoveryManager : DiscoveryListener {

    val logger = LoggerFactory.getLogger(javaClass)!!
    val peers = FXCollections.observableArrayList<BluetoothPeer>()

    private val ld = LocalDevice.getLocalDevice()

    fun beginPeerDiscovery() {
        ld.discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this)
    }

    fun beginServiceDiscovery() {
        peers.forEach {
            ld.discoveryAgent.searchServices(intArrayOf(), arrayOf(), it.remoteDevice, this)
        }
    }

    override fun deviceDiscovered(btDevice: RemoteDevice, cod: DeviceClass) {
        logger.debug("Discovered $btDevice of class $cod")
    }

    override fun inquiryCompleted(discType: Int) {
        logger.debug("Inquiry complete $discType")
        peers.setAll(ld.discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED).map { BluetoothPeer(it) })
        //trySearchNextDevice()
    }

    override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>) {
        //logger.debug("Discovered devices: {}", Arrays.toString(servRecord))
        //services.addAll(servRecord)
        //services.add()
    }

    override fun serviceSearchCompleted(transID: Int, respCode: Int) {
        //logger.debug("Finished service search, attempting to search next device")
        //trySearchNextDevice()
    }
}