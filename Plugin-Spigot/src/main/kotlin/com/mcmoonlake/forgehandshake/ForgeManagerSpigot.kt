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

package com.mcmoonlake.forgehandshake

import com.mcmoonlake.api.callEvent
import com.mcmoonlake.api.chat.ChatSerializer
import com.mcmoonlake.api.notNull
import com.mcmoonlake.api.packet.*
import com.mcmoonlake.forgehandshake.api.Contents
import com.mcmoonlake.forgehandshake.api.ForgeManagerBase
import com.mcmoonlake.forgehandshake.api.ModInfo
import com.mcmoonlake.forgehandshake.api.Mods
import java.util.concurrent.ConcurrentHashMap

class ForgeManagerSpigot(main: Main) : ForgeManagerBase(main) {

    private val cancelledMaps: MutableMap<String, String> = ConcurrentHashMap()

    override val instance: Main
        get() = super.instance as Main

    private val listener = object: PacketListenerAnyAdapter(main) {
        override fun onSending(event: PacketEvent) {
            if(event.packet is PacketOutJoinGame && event.player != null) {
                val player = event.player.notNull()
                PacketOutPayload(Contents.REGISTER, writeForgeRegisterPacket()).send(player)
                PacketOutPayload(Contents.HANDSHAKE, writeForgeServerHelloPacket()).send(player)
            }
            if(event.player != null) {
                val player = event.player.notNull()
                val reason = cancelledMaps.remove(player.name) ?: return
                event.packet = PacketOutKickDisconnect(ChatSerializer.fromRaw(reason))
            }
        }
        override fun onReceiving(event: PacketEvent) {
            if(event.packet is PacketInPayload && event.player != null) {
                val packet = event.packet as PacketInPayload
                val buffer = packet.data
                if(packet.channel == Contents.HANDSHAKE && buffer.readByte().toInt() == Contents.PACKET_MODINFO_LIST) {
                    val player = event.player.notNull()
                    val mods = Mods((0 until buffer.readVarInt()).map { ModInfo(buffer.readString(), buffer.readString()) })
                    val forgeEvent = ForgeClientEvent(player.name, mods); forgeEvent.callEvent()
                    if(forgeEvent.isCancelled) cancelledMaps.put(player.name, forgeEvent.reason)
                    else modMaps.put(player.name, mods)
                }
            }
        }
        override fun handlerException(ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun initialize() {
        super.initialize()
        cancelledMaps.clear()
        PacketListeners.registerListener(listener)
    }

    override fun close() {
        super.close()
        cancelledMaps.clear()
        PacketListeners.unregisterListener(listener)
    }

    private fun writeForgeRegisterPacket(): PacketBuffer
            = PacketBuffer().writeStrings(Contents.CHANNELS)

    private fun writeForgeServerHelloPacket(): PacketBuffer
            = PacketBuffer().writeByte(Contents.PACKET_SERVER_HELLO)
            .writeByte(Contents.PROTOCOL_VERSION)
            .writeInt(Contents.DIMENSION)
}
