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

package de.daill.protocol

import de.daill.BotProps
import de.daill.socket.BotSocket
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.springframework.http.*
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.net.URI

@Service
class Protocol(private var props: BotProps) {
    val LOG = LoggerFactory.getLogger(Protocol::class.java)

    fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bot " + props.token)
        return headers
    }

    fun getGuilds(): String? {
        val restTemplate = RestTemplate()
        val entity = HttpEntity<MultiValueMap<String, String>>(getHeaders())

        val response: ResponseEntity<String> = restTemplate.exchange(
            props.endpoint + "/users/@me/guilds",
            HttpMethod.GET,
            entity,
            String::class.java
        )

        LOG.info(response.getBody())
        return null;
    }

    fun sendInteractionResponse(interactionId: String, interactionToken: String, interactionResponse: InteractionResponse) {
        // "https://discord.com/api/v8/interactions/<interaction_id>/<interaction_token>/callback"
        val restTemplate = RestTemplate()
        val headers = getHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val json = Json.encodeToString(interactionResponse)
        val entity = HttpEntity(json, headers)

        val response: ResponseEntity<String> = restTemplate.exchange(
            props.endpoint + "/interactions/" + interactionId + "/" + interactionToken + "/callback",
            HttpMethod.POST,
            entity,
            String::class.java
        )

        if(response.statusCode != HttpStatus.OK) {
            LOG.info(response.getBody())
        }
    }

    fun getSlashCommands(): String? {
        val restTemplate = RestTemplate()
        val entity = HttpEntity<MultiValueMap<String, String>>(getHeaders())

        val response: ResponseEntity<String> = restTemplate.exchange(
            props.endpoint + "/applications/" + props.clientid + "/commands",
            HttpMethod.GET,
            entity,
            String::class.java
        )

        LOG.info(response.getBody())
        return null;
    }

    fun getGetGateway(): String {
        val restTemplate = RestTemplate()
        val entity = HttpEntity<MultiValueMap<String, String>>(getHeaders())

        val response: ResponseEntity<String> = restTemplate.exchange(
            props.endpoint + "/gateway/bot",
            HttpMethod.GET,
            entity,
            String::class.java
        )

        LOG.info(response.getBody())
        return response.getBody();
    }
}