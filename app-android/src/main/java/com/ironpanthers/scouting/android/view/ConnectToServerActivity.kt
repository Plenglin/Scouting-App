package com.ironpanthers.scouting.android.view

import android.os.Bundle
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID
import com.ironpanthers.scouting.android.R
import com.ironpanthers.scouting.android.io.match.MatchClientService
import com.ironpanthers.scouting.io.match.client.MatchClient

import kotlinx.android.synthetic.main.activity_connect_to_server.*

class ConnectToServerActivity : Activity() {

    val bt = BluetoothAdapter.getDefaultAdapter()
    lateinit var bonded: Array<BluetoothDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_to_server)

        layout_refresh.setOnRefreshListener(::updateDevices)
        list_devices.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val device = bonded[position]
            MatchClientService.connectToServer(this, device)
        }
        updateDevices()
    }

    fun updateDevices() {
        bonded = bt.bondedDevices.toTypedArray()
        list_devices.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_2,
                bonded.map { it.name }.toTypedArray()
        )
        layout_refresh.isRefreshing = false
    }

}
