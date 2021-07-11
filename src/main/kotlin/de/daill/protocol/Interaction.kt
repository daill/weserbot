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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@Serializable
data class InteractionUser(
    val username: String,
    @SerialName("public_flags")
    val publicFlags: Int?,
    val id: String,
    val discriminator: String,
    val avatar: String?
)

@Serializable
data class InteractionMember(
    val user: InteractionUser,
    val roles: Array<String>,
    @SerialName("premium_since")
    val premiumSince: String? = null,
    val permissions: String,
    val pending: Boolean,
    val nick: String?,
    val mute: Boolean,
    @SerialName("joined_at")
    val joinedAt: String,
    @SerialName("is_pending")
    val isPending: Boolean,
    val deaf: Boolean,
    val avatar: String? = null
)

@Serializable
data class InteractionData(
    val name: String,
    val id: String,
    @SerialName("custom_id")
    val customId: String? = null,
    @SerialName("component_type")
    val componentType: Int? = null,
    val options: Array<JsonElement>? = null
)

@Serializable
data class Interaction(
    val version: Int,
    val type: Int,
    val token: String,
    val id: String,
    @SerialName("guild_id")
    val guildId: String? = null,
    @SerialName("channel_id")
    val channelId: String? = null,
    @SerialName("application_id")
    val applicationId: String,
    val member: InteractionMember? = null,
    val data: InteractionData? = null,
    val user: InteractionUser? = null,
    val message: String? = null
)