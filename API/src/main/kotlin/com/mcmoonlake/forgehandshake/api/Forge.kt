/*
 * Copyright (C) 2016-Present The MoonLake (mcmoonlake@hotmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mcmoonlake.forgehandshake.api

object Contents {
    @JvmField
    val CHANNELS = arrayOf("FML|HS", "FML", "FML|MP", "FML", "FORGE") // 需要注册的通道
    const val REGISTER = "REGISTER" // 注册通道
    const val HANDSHAKE = "FML|HS" // Forge 的捂手通道
    const val PACKET_SERVER_HELLO = 0x0 // 服务器 Hello 数据包
    const val PACKET_MODINFO_LIST = 0x2 // 模组信息列表数据包
    const val PROTOCOL_VERSION = 0x2 // Forge 协议版本
    const val DIMENSION = 0x0 // 维度
}

data class ModInfo(val modid: String, val version: String)

data class Mods(private val args: Collection<ModInfo>) : Iterable<ModInfo> {

    override fun iterator(): Iterator<ModInfo>
            = args.iterator()
}

/**
 * * Main Interface
 */
interface ForgeInstance

interface ForgeEvent {

    val player: String

    val isRealName: Boolean

    val mods: Mods?

    var reason: String

    var isCancelled: Boolean

    fun hasMod(modid: String): Boolean
            = mods?.find { it.modid == modid } != null
}
