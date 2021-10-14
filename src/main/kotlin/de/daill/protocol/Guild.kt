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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
enum class VideoQuality(val quality: String) {
    `1`("AUTO"),
    `2`("FULL"),
}

enum class PremiumTier(val tier: String) {
    `0`("NONE"),
    `1`("TIER_1"),
    `2`("TIER_2"),
    `3`("TIER_3"),
}

@Serializable
enum class ChannelType(val type: String) {
    `0`("GUILD_TEXT"),
    `1`("DM"),
    `2`("GUILD_VOICE"),
    `3`("GROUP_DM"),
    `4`("GUILD_CATEGORY"),
    `5`("GUILD_NEWS"),
    `6`("GUILD_STORE"),
    `10`("GUILD_NEWS_THREAD"),
    `11`("GUILD_PUBLIC_THREAD"),
    `12`("GUILD_PRIVATE_THREAD"),
    `13`("GUILD_STAGE_VOICE")
}


@Serializable
data class Channel (
    val id: String,
    val name: String? = null,
    val position: Int? = null,
    val type: Int,
    val bitrate: Int? = null,
    @SerialName("video_quality_mode")
    val videoQualityMode: Int? = null,
    @SerialName("user_limit")
    val userLimit: Int? = null
)

@Serializable
data class Guild (
    val id : String,
    @SerialName("max_members")
    val maxMembers: Int?,
    @SerialName("max_video_channel_users")
    val maxVideoChannelUsers: Int?,
    @SerialName("member_count")
    val memberCount: Int?,
    val name: String,
    @SerialName("premium_subscription_count")
    val premiumSubscriptionCount: Int?,
    @SerialName("premium_tier")
    val premiumTier: Int,
    @SerialName("owner_id")
    val ownerId: String,
    val channels: ArrayList<Channel>?
)