package de.daill.protocol

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class VanguardStats(
    val data: VanguardData?,
    var detailsUrl: String?
) : Stats

@JsonIgnoreProperties(ignoreUnknown = true)
data class VanguardData (
    val segments : ArrayList<VanguardSegment>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class VanguardSegment (
    val type : String,
    val stats : HashMap<String, Object>
)