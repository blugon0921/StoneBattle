package io.github.blugon0921.stonebattle.team

import io.github.blugon0921.stonebattle.StoneBattle.Companion.team_info
import io.github.blugon0921.stonebattle.StoneBattle.Companion.yaml
import io.github.blugon0921.stonebattle.team.Red.Companion.redTeam
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.ServerLoadEvent

class Blue : Listener {

    companion object {
        val blueTeam = ArrayList<Player>()

        var blue_join = Location(Bukkit.getWorld("world"), 0.5, 151.0, 4.5)
        var blue_core = Location(Bukkit.getWorld("world"), 0.0, 62.0, 53.0)
        var blue_spawn = Location(Bukkit.getWorld("world"), 0.5, 53.0, 41.5)

        val blue_bar = Bukkit.createBossBar("BLUE", BarColor.BLUE, BarStyle.SEGMENTED_10)
    }


    @EventHandler
    fun onEnable(event : ServerLoadEvent) {
        blue_bar.progress = 1.0
    }


    @EventHandler
    fun joinRedTeam(event : PlayerMoveEvent) {
        val player = event.player

        for(nbp in blue_join.getNearbyEntities(0.5, 0.5, 0.5)) {
            if(nbp !is Player) return

            if(redTeam.contains(nbp)) redTeam.remove(nbp)
            if(!blueTeam.contains(nbp)) blueTeam.add(nbp)
            nbp.sendActionBar(Component.text("${ChatColor.BLUE}Join BlueTeam!"))
        }
    }
}