package com.ironpanthers.scouting.android.io.match

import android.app.IntentService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.ironpanthers.scouting.BLUETOOTH_MAIN_UUID
import com.ironpanthers.scouting.android.ScoutingApplication
import com.ironpanthers.scouting.common.Match
import com.ironpanthers.scouting.io.match.MSG_MATCH_ASSIGN
import com.ironpanthers.scouting.io.match.MSG_MATCH_BEGIN
import com.ironpanthers.scouting.io.match.client.MatchClient
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates

private const val ACTION_SEND_MATCH_DATA = "com.ironpanthers.scouting.android.io.match.action.SEND_MATCH"
private const val ACTION_REQUEST_MATCH_DATA = "com.ironpanthers.scouting.android.io.match.action.REQUEST_MATCH"
private const val ACTION_CONNECT_TO_SERVER = "com.ironpanthers.scouting.android.io.match.action.CONNECT_TO_SERVER"

private const val EVENT_CONNECTED_TO_SERVER = "com.ironpanthers.scouting.android.io.match.event.CONNECTED_TO_SERVER"
private const val EVENT_DISCONNECTED_FROM_SERVER = "com.ironpanthers.scouting.android.io.match.event.DISCONNECTED_FROM_SERVER"
private const val EVENT_RECEIVE_ASSIGNMENT = "com.ironpanthers.scouting.android.io.match.event.ASSIGNMENT_RECEIVED"
private const val EVENT_MATCH_BEGIN = "com.ironpanthers.scouting.android.io.match.event.MATCH_BEGIN"

private const val EXTRA_MATCH_DATA = "com.ironpanthers.scouting.android.io.match.extra.MATCH_DATA"
private const val EXTRA_MATCH_NUMBER = "com.ironpanthers.scouting.android.io.match.extra.MATCH_NUMBER"
private const val EXTRA_MATCH_TEAM = "com.ironpanthers.scouting.android.io.match.extra.MATCH_TEAM"
private const val EXTRA_BLUETOOTH_HOST = "com.ironpanthers.scouting.android.io.match.extra.BLUETOOTH_HOST"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class MatchClientService : IntentService("MatchClientService") {

    var socket: BluetoothSocket? = null
    val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val app by lazy { application as ScoutingApplication }
    var isConnected by Delegates.observable(app::isConnected)

    private var client: MatchClient? = null

    override fun onHandleIntent(intent: Intent?) {
        logger.debug("Received intent {}", intent)
        when (intent?.action) {
            ACTION_CONNECT_TO_SERVER -> {
                connectToServer(intent.getParcelableExtra(EXTRA_BLUETOOTH_HOST) as BluetoothDevice)
            }
            ACTION_SEND_MATCH_DATA -> {
                val obj = intent.getSerializableExtra(EXTRA_MATCH_DATA)
                client?.send(MSG_MATCH_BEGIN, mapper.valueToTree(obj))
            }
            /*ACTION_REQUEST_MATCH_DATA -> {
                val match = intent.getIntExtra(EXTRA_MATCH_NUMBER, -1)
                val team = intent.getIntExtra(EXTRA_MATCH_TEAM, -1)
                if (match == -1) {
                    throw IllegalArgumentException("No match number provided!")
                }
                if (team == -1) {
                    throw IllegalArgumentException("No team number provided!")
                }
                client?.send(MSG_MATCH_ASSIGN)
            }*/
        }
    }

    fun connectToServer(host: BluetoothDevice) {
        logger.info("attempting to connect to {}", host)

        val socket = host.createRfcommSocketToServiceRecord(BLUETOOTH_MAIN_UUID)

        socket.connect()
        sendBroadcast(Intent(EVENT_CONNECTED_TO_SERVER))

        val client = MatchClient(socket.inputStream, socket.outputStream).apply {
            start()
        }
        client.listener = {
            logger.debug("received message {}", it)
            when (it.type) {
                MSG_MATCH_BEGIN -> {
                    sendBroadcast(Intent(EVENT_MATCH_BEGIN).apply {
                        putExtra(EXTRA_MATCH_DATA, mapper.treeToValue<Match>(it.data!!))
                    })
                }
                MSG_MATCH_ASSIGN -> {
                    sendBroadcast(Intent(EVENT_RECEIVE_ASSIGNMENT).apply {
                        putExtra(EXTRA_MATCH_DATA, mapper.treeToValue<Match>(it.data!!))
                    })
                }
                else -> {
                    logger.warn("Received invalid event type '{}'!", it.type)
                }
            }
        }
        this.client = client
        this.socket = socket
        isConnected = true
        logger.info("successfully connected to {}", host)
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(MatchClientService::class.java)

        @JvmStatic
        private val mapper = jacksonObjectMapper()
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        @JvmStatic
        fun sendMatchData(context: Context, match: Match) {
            val intent = Intent(context, MatchClientService::class.java).apply {
                action = ACTION_SEND_MATCH_DATA
                putExtra(EXTRA_MATCH_DATA, match)
            }
            context.startService(intent)
        }

        @JvmStatic
        fun connectToServer(context: Context, server: BluetoothDevice) {
            context.startService(Intent(context, MatchClientService::class.java).apply {
                action = ACTION_CONNECT_TO_SERVER
                putExtra(EXTRA_BLUETOOTH_HOST, server)
            })
        }

    }
}
