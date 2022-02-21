package de.daill.protocol

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import java.net.URL
import java.net.URLEncoder

class TrackerApi {
    val LOG = LoggerFactory.getLogger(TrackerApi::class.java)

    fun getVanguardStats(nick: String): Stats {
        try {
            var nick = URLEncoder.encode(nick, "utf-8")
            var vanguardUrl = "https://api.tracker.gg/api/v2/vanguard/standard/profile/atvi/%s".format(nick)
            val apiResponse = URL(vanguardUrl).readText()

            val mapper = jacksonObjectMapper()
            val stats: VanguardStats = mapper.readValue(apiResponse)
            stats.detailsUrl = "https://cod.tracker.gg/vanguard/profile/atvi/%s/overview".format(nick)
            LOG.info("retrieved vanguard stats")
            return stats
        } catch (ex: Exception) {
            return VanguardStats(null, "")
        }
    }

    fun getWarzoneStats(nick: String): Stats {
        try {
            var nick = URLEncoder.encode(nick, "utf-8")
            var warzonedUrl = "https://api.tracker.gg/api/v2/warzone/standard/profile/atvi/%s".format(nick)
            val apiResponse = URL(warzonedUrl).readText()

            val mapper = jacksonObjectMapper()
            val stats: WarzoneStats = mapper.readValue(apiResponse)
            stats.detailsUrl = "https://cod.tracker.gg/warzone/profile/atvi/%s/overview".format(nick)
            LOG.info("retrieved warzone stats")
            return stats
        } catch (ex: Exception) {
            return WarzoneStats(null, "")
        }
    }
}