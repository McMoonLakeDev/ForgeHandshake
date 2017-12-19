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

import com.mcmoonlake.forgehandshake.api.Contents
import com.mcmoonlake.forgehandshake.api.ForgeManagerBase
import io.netty.buffer.Unpooled
import net.md_5.bungee.UserConnection
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.forge.ForgeClientHandler
import net.md_5.bungee.forge.ForgeConstants
import net.md_5.bungee.protocol.DefinedPacket
import net.md_5.bungee.protocol.packet.PluginMessage
import java.util.concurrent.TimeUnit

class ForgeManagerBungee(main: Main) : ForgeManagerBase(main), Listener {

    override val instance: Main
        get() = super.instance as Main

    @EventHandler(priority = Byte.MAX_VALUE)
    fun onLogin(event: PostLoginEvent) {
        instance.proxy.scheduler.schedule(instance, {
            if(event.player.server != null)
                setForgeHandshake(event.player)
        }, 1L, TimeUnit.SECONDS)
    }

    override fun initialize() {
        super.initialize()
        instance.proxy.pluginManager.registerListener(instance, this)
    }

    override fun close() {
        super.close()
        instance.proxy.pluginManager.unregisterListener(this)
    }

    private fun setForgeHandshake(player: ProxiedPlayer) {
        val conn = player as? UserConnection ?: return
        conn.forgeClientHandler = Handler(conn)
        conn.unsafe().sendPacket(ForgeConstants.FML_RESET_HANDSHAKE)
        conn.unsafe().sendPacket(writeFML(Contents.REGISTER, -1))
        conn.unsafe().sendPacket(writeFML(Contents.HANDSHAKE, Contents.PACKET_SERVER_HELLO))
        conn.unsafe().sendPacket(writeFML(Contents.HANDSHAKE, Contents.PACKET_MODINFO_LIST))
    }

    private fun writeFML(channel: String, type: Int): PluginMessage {
        val pluginMessage = PluginMessage()
        val buffer = Unpooled.buffer()
        when (channel) {
            Contents.REGISTER -> {
                val value = Contents.CHANNELS.joinToString(separator = "\u0000")
                DefinedPacket.writeString(value, buffer)
            }
            Contents.HANDSHAKE -> if (type == Contents.PACKET_SERVER_HELLO) {
                buffer.writeByte(Contents.PACKET_SERVER_HELLO)
                buffer.writeByte(Contents.PROTOCOL_VERSION)
                buffer.writeInt(Contents.DIMENSION)
            } else if (type == Contents.PACKET_MODINFO_LIST) {
                buffer.writeByte(Contents.PACKET_MODINFO_LIST)
                DefinedPacket.writeVarInt(0, buffer)
            }
        }
        pluginMessage.tag = channel
        pluginMessage.data = buffer.array()
        buffer.release()
        return pluginMessage
    }

    private class Handler(conn: UserConnection) : ForgeClientHandler(conn) {
        override fun handle(message: PluginMessage) { }
        override fun resetHandshake() { }
        override fun isHandshakeComplete(): Boolean = false
    }
}
