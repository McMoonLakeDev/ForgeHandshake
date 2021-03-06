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

import com.mcmoonlake.api.event.MoonLakeEvent
import com.mcmoonlake.forgehandshake.api.ForgeEvent
import com.mcmoonlake.forgehandshake.api.Mods
import org.bukkit.event.HandlerList

class ForgeClientEvent(
        override val player: String,
        override val mods: Mods?,
        override var reason: String = "Kick") : MoonLakeEvent(), ForgeEvent {

    private var cancel = false

    override var isCancelled: Boolean
        get() = cancel
        set(value) { cancel = value }

    override fun getHandlers(): HandlerList
            = handlerList

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}
