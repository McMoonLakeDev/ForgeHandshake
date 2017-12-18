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

package com.mcmoonlake.forgehandshake.bungee

import com.mcmoonlake.forgehandshake.api.ForgeEvent
import com.mcmoonlake.forgehandshake.api.Mods
import net.md_5.bungee.api.plugin.Event

class ForgeClientEvent(
        override val player: String,
        override val mods: Mods?,
        override var reason: String = "Kick") : Event(), ForgeEvent {

    private var cancel = false

    override var isCancelled: Boolean
        get() = cancel
        set(value) { cancel = value }

    override val isRealName: Boolean
        get() = true
}
