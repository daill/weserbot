package de.daill.protocol

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class WarzoneStats (
    val data: WarzoneData?,
    var detailsUrl: String?
) : Stats

@JsonIgnoreProperties(ignoreUnknown = true)
data class WarzoneData (
    val segments : ArrayList<WarzoneSegment>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WarzoneSegment (
    val type : String,
    val stats : HashMap<String, Object>
)