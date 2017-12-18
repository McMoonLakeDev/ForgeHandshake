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

import com.mcmoonlake.forgehandshake.api.ForgeInstance
import com.mcmoonlake.forgehandshake.api.ForgeManager
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), ForgeInstance {

    private var forgeManager: ForgeManager? = null

    override fun onEnable() {
        forgeManager = ForgeManagerSpigot(this)
        forgeManager?.initialize()
    }

    override fun onDisable() {
        forgeManager?.close()
        forgeManager = null
    }

//    @EventHandler
//    fun onClient(event: ForgeClientEvent) {
//        if(event.hasMod("journeymap")) {
//            event.isCancelled = true
//            event.reason = "&c断开连接: 由于你的客户端加载有 \"旅行者地图\" 模组."
//        }
//    }
}
