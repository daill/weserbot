/*
 * Copyright 2021 Christian Kramer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
 *  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 *  so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.daill.socket

import de.daill.BotProps
import de.daill.commands.CoinCommand
import de.daill.commands.DiceCommand
import de.daill.protocol.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.slf4j.LoggerFactory
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI
import java.time.Duration
import java.time.LocalTime

class BotSocket(val protocol: Protocol, val props: BotProps) : TextWebSocketHandler() {
    val LOG = LoggerFactory.getLogger(BotSocket::class.java)
    var sequenceNumber: Int? = null
    var heartbeat : Heartbeat? = null
    var activeConnection = false
    var lastHeartbeat : LocalTime = LocalTime.now()
    val heartbeatOffset = 1000
    var client = StandardWebSocketClient()
    var session : WebSocketSession? = null
    var gatewayInfo : Gateway? = null

    init {
        gatewayInfo = Json{ ignoreUnknownKeys = true }.decodeFromString<Gateway>(protocol.getGetGateway())
        LOG.info(gatewayInfo?.url + "/?v=" + props.gateway.get("version") + "&encoding=" + props.gateway.get("encoding"))
        LOG.info("create socket")
    }

    fun initSocket() {
        session = client.doHandshake(this, WebSocketHttpHeaders(), URI.create(gatewayInfo?.url + "/?v=" + props.gateway.get("version") + "&encoding=" + props.gateway.get("encoding"))).get()
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        LOG.error("session closed with status %d reason: %s".format(status.code, status.reason))
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        super.handleMessage(session, message)
        LOG.info(message.payload as String?)

        val op = Json{ ignoreUnknownKeys = true }.decodeFromString<Base>(message.payload as String)

        op.s?.let { handlingSequence(it) }

        // heartbeat init
        if(op.op == 10) {
            lastHeartbeat = LocalTime.now()
            heartbeat = op.d?.let { Json{ ignoreUnknownKeys = true }.decodeFromString<Heartbeat>(it.toString()) }
            CoroutineScope(Dispatchers.IO).launch {
                scheduleHeartbeat(heartbeat!!.heartbeat_interval-heartbeatOffset)
            }

            identify()
        }
        if(op.op == 11) {
            lastHeartbeat = LocalTime.now()
            CoroutineScope(Dispatchers.IO).launch {
                scheduleHeartbeat(heartbeat!!.heartbeat_interval-heartbeatOffset)
            }
        }
        if(op.op == 0) {
            // need to switch based on t
            when(op.t) {
                InteractionType.INTERACTION_CREATE.type -> {
                    handleInteraction(op.d)
                }

            }
        }

    }

    private fun handleInteraction(payload: JsonElement) {
        try {
            val interaction = Json.decodeFromJsonElement<Interaction>(payload)
            LOG.info(interaction.toString())
            when(interaction.data?.name) {
                "dice" -> { DiceCommand(protocol, interaction).process() }
                "coin" -> { CoinCommand(protocol, interaction).process() }
            }
        } catch (e: SerializationException){
            LOG.error(e.localizedMessage)
        }

    }

    private fun handlingSequence(s: Int) {
        if (s != null) {
            if (sequenceNumber == null) {
                sequenceNumber = s
            } else {
                if (s.compareTo(sequenceNumber!!) > 0) {
                    sequenceNumber = s
                }
            }
        }
    }

    private fun ensureApiGateway() {
        LOG.debug(heartbeat.toString())
        if ((Duration.between(lastHeartbeat, LocalTime.now()).seconds/1000) > heartbeat!!.heartbeat_interval) {
            LOG.error("gateway possibly dead, need to re init")
            initSocket()
        }
    }

    private fun <T> sendMessage(msg: WebSocketMessage<T>) {
        ensureApiGateway().also { session?.sendMessage(msg) }
    }

    private fun identify() {
        LOG.info("try to identify")
        val identifyProperties = IdentifyProperties("linux","weserbot", "weserbot")
        val identifyPayload = IdentifyPayload(props.token, false, null,null, 1, identifyProperties, null)
        val identify = Identify(Op.Identify.value, identifyPayload, null)
        val msg = Json.encodeToString(identify)
        LOG.info(msg)
        sendMessage(TextMessage(msg))
        LOG.info("identify sent")
    }

    private suspend fun scheduleHeartbeat(heartbeatInterval: Int) {
        LOG.info("heartbeat scheduled in %d".format(heartbeatInterval))
        delay(heartbeatInterval.toLong())
        activeConnection = false
        var msg = TextMessage(
            Json.encodeToString(HeartbeatResponse(1, null, sequenceNumber))
        )
        LOG.info(msg.payload)
        session?.sendMessage(msg)

        LOG.info("heartbeat sent")
    }
}