package com.ironpanthers.scouting.desktop.view

import javax.bluetooth.*

/*
class ServerConnectionWizard : View() {
    override val root: Parent

    init {
        //root = listview<Any> {


    }
}*/

fun main(args: Array<String>) {
    LocalDevice.getLocalDevice().discoveryAgent.startInquiry(DiscoveryAgent.GIAC, object : DiscoveryListener {
        override fun serviceSearchCompleted(transID: Int, respCode: Int) {
        }

        override fun deviceDiscovered(btDevice: RemoteDevice?, cod: DeviceClass?) {
            println("Discovered $btDevice of class $cod")
        }

        override fun servicesDiscovered(transID: Int, servRecord: Array<out ServiceRecord>?) {
        }

        override fun inquiryCompleted(discType: Int) {
            println("Inquiry complete $discType")
        }

    })
}