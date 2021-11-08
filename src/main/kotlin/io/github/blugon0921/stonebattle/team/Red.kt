package io.github.blugon0921.stonebattle.team

import io.github.blugon09.itemhelper.component
import io.github.blugon0921.stonebattle.StoneBattle.Companion.team_info
import io.github.blugon0921.stonebattle.StoneBattle.Companion.yaml
import io.github.blugon0921.stonebattle.team.Blue.Companion.blueTeam
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_core
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_join
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_spawn
import net.kyori.adventure.text.Component
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.ServerLoadEvent

class Red : Listener {

    companion object {
        val redTeam = ArrayList<Player>()

        var red_join = Location(Bukkit.getWorld("world"), 0.5, 151.0, -3.5)
        var red_core = Location(Bukkit.getWorld("world"), 0.0, 62.0, -53.0)
        var red_spawn = Location(Bukkit.getWorld("world"), 0.5, 53.0, -40.5)

        val red_bar = Bukkit.createBossBar("RED", BarColor.RED, BarStyle.SEGMENTED_10)
    }


    @EventHandler
    fun onEnable(event : ServerLoadEvent) {
        yaml.load(team_info)
        red_join = yaml.getLocation("red.join")!!
        red_core = yaml.getLocation("red.core")!!
        red_spawn = yaml.getLocation("red.spawn")!!

        red_bar.progress = 1.0
    }

    @EventHandler
    fun joinRedTeam(event : PlayerMoveEvent) {
        val player = event.player

        for(nbp in red_join.getNearbyEntities(0.5, 0.5, 0.5)) {
            if(nbp !is Player) return
            if(nbp.gameMode == GameMode.SPECTATOR) return

            if(blueTeam.contains(nbp)) blueTeam.remove(nbp)
            if(!redTeam.contains(nbp)) redTeam.add(nbp)
            nbp.playerListName("${ChatColor.RED}${nbp.name}".component())
            nbp.sendActionBar("${ChatColor.RED}Join RedTeam!".component())
        }
    }
}