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

package de.daill.commands

import de.daill.protocol.Interaction
import de.daill.protocol.InteractionResponse
import de.daill.protocol.InteractionResponseData
import de.daill.protocol.Protocol
import org.slf4j.LoggerFactory
import kotlin.random.Random

class CoinCommand(val protocol: Protocol, val interaction: Interaction) : Command {
    val LOG = LoggerFactory.getLogger(this::class.java)

    override fun process() {
        LOG.info("flip the coin")
        // prepare answer
        var value = "heads"
        if(Random.nextBoolean()) {
            value = "tails"
        }

        var data = InteractionResponseData(tts = null, content = value, description = null, embeds = null, url = null)
        var response = InteractionResponse(type = 4, data = data)
        protocol.sendInteractionResponse(interaction.id, interaction.token, response)
    }
}