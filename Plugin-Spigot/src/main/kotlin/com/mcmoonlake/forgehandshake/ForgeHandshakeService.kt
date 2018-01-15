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

import com.mcmoonlake.api.player.MoonLakePlayer
import com.mcmoonlake.api.service.ServiceAbstract
import com.mcmoonlake.api.service.ServiceForgeHandshake
import com.mcmoonlake.api.wrapper.Mod
import org.bukkit.entity.Player

class ForgeHandshakeService(private val manager: ForgeManagerSpigot) : ServiceAbstract(), ServiceForgeHandshake {

    override fun onInitialized() {
        manager.instance.logger.info("月色之湖 ForgeHandshake 服务成功加载.")
    }

    override fun onUnloaded() {
    }

    override fun getMods(player: MoonLakePlayer): Array<Mod>?
            = getMods(player.bukkitPlayer)

    override fun getMods(player: Player): Array<Mod>?
            = manager.getMods(player.name)?.map { Mod(it.modid, it.version) }?.toTypedArray()

    override fun resetMods(player: MoonLakePlayer): Boolean
            = resetMods(player.bukkitPlayer)

    override fun resetMods(player: Player): Boolean
            = manager.resetMods(player.name)
}
