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

package de.daill

import de.daill.protocol.Protocol
import de.daill.socket.BotSocket
import de.daill.socket.BotSocketEvent
import de.daill.socket.BotSocketEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationListener
import java.time.Instant
import java.util.*


@SpringBootApplication
class WeserBot: ApplicationRunner, ApplicationListener<BotSocketEvent> {
    val LOG = LoggerFactory.getLogger(WeserBot::class.java)
    var lastReconnect = Instant.now().epochSecond

    @Autowired
    lateinit var props: BotProps
    @Autowired
    lateinit var publisher : BotSocketEventPublisher

    var socket : BotSocket? = null
    var protocol : Protocol? = null

    override fun run(args: ApplicationArguments?) {
        protocol = Protocol(props)
        LOG.info("get gateway info")
        publisher.publishbotSocketEvent("init")
    }

    override fun onApplicationEvent(event: BotSocketEvent) {
        LOG.info(event.message)
        socket = null
        var wait : Long = 1000

        if ((Instant.now().epochSecond-lastReconnect) < 5000) {
            wait = 20000
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                socket = BotSocket(props = props, protocol = protocol!!, publisher = publisher)
                socket!!.initSocket()
            }
        }, wait)
    }


}

