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

import de.daill.protocol.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.slf4j.LoggerFactory

@Serializable
data class StatsOption(
    val name: String,
    val value: String,
    val type: Int
)

class StatsCommand(val protocol: Protocol, val interaction: Interaction) : Command {
    val LOG = LoggerFactory.getLogger(this::class.java)
    val tracker = TrackerApi()

    override fun process() {
        var opts = interaction.data?.options
        var nick = ""
        var game = ""
        opts?.forEach {
            var statsOpt = Json.decodeFromJsonElement<StatsOption>(it)
            when(statsOpt.name) {
                "nick" -> { nick = statsOpt.value }
                "game" -> { game = statsOpt.value }
            }
        }

        LOG.info("sent nick %s for game %s".format(nick, game))

        var result : String = ""
        when(game) {
            "vanguard" -> { result = parseVanguardStats(tracker.getVanguardStats(nick)) }
            "warzone" -> { result = parseWarzoneStats(tracker.getWarzoneStats(nick)) }
        }


        // prepare answer
        var data = InteractionResponseData(tts = null, content = result, description = null, embeds = null, url = null)
        var response = InteractionResponse(type = 4, data = data)
        protocol.sendInteractionResponse(interaction.id, interaction.token, response)
    }

    fun parseWarzoneStats(stats: Stats) : String {
        var result = "data retrieval failed"
        (stats as WarzoneStats).data?.let {
            var segment = it.segments.filter { s -> s.type.equals("overview")  }.single()
            var level = segment.stats.getValue("level") as HashMap<String, Object>
            var levelNumber = level.getValue("displayValue")
            var metadata = level.getValue("metadata") as HashMap<String, String>
            var rankImage = ""
            var rankName = ""
            if (!metadata.isEmpty()) {
                rankImage = metadata.getValue("imageUrl")
                rankName = metadata.getValue("rankName")
            }
            var prestige = segment.stats.getValue("prestige") as HashMap<String, Object>
            var prestigeNumber = prestige.getValue("displayValue")
            metadata = prestige.getValue("metadata") as HashMap<String, String>
            if (!metadata.isEmpty()) {
                rankImage = metadata.getValue("imageUrl")
                rankName = metadata.getValue("rankName")
            }
            result = "level: %s rank: %s\nprestige: %s\n%s\nlink: %s".format(levelNumber, rankName,prestigeNumber, rankImage, (stats as WarzoneStats).detailsUrl)
        }
        return result
    }

    fun parseVanguardStats(stats: Stats) : String {
        var result = "data retrieval failed"
        (stats as VanguardStats).data?.let {
            var segment = it.segments.filter { s -> s.type.equals("overview")  }.single()
            var level = segment.stats.getValue("level") as HashMap<String, Object>
            var levelNumber = level.getValue("displayValue")
            var metadata = level.getValue("metadata") as HashMap<String, String>
            var rankImage = metadata.getValue("imageUrl")
            var rankName = metadata.getValue("rankName")
            result = "level: %s rank: %s \n%s\nlink: %s".format(levelNumber, rankName, rankImage, (stats as VanguardStats).detailsUrl)
        }
        return result
    }

}