package io.github.blugon0921.stonebattle.events

import io.github.blugon0921.stonebattle.StoneBattle
import io.github.blugon0921.stonebattle.team.Blue.Companion.blueTeam
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_spawn
import io.github.blugon0921.stonebattle.team.Red.Companion.redTeam
import io.github.blugon0921.stonebattle.team.Red.Companion.red_spawn
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.floor

class Death : Listener {


    @EventHandler
    fun outWorld(event : PlayerMoveEvent) {
        val player = event.player
        val location = player.location

        if(location.y < -10) {
            player.damage(10000000000.0)
        }
    }



    @EventHandler
    fun death(event : PlayerRespawnEvent) {
        val player = event.player


        for(i in 0..35) {
            if(player.inventory.getItem(i) != null) {
                val item = player.inventory.getItem(i)!!
                if(item.type == Material.COBBLESTONE) {
                    item.amount = item.amount/2
                    player.inventory.setItem(i, item)
                }
            }
        }

        if(redTeam.contains(player)) {
            player.gameMode = GameMode.SPECTATOR
            player.teleport(red_spawn)
            var i = 10.0

            var rp_id = 0

            rp_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(StoneBattle::class.java), {
                if(0.0 < i) {
                    player.sendActionBar(Component.text("리스폰까지 ${(floor(i*10))/10}초"))
                    i -= 0.1
                } else {
                    Bukkit.getScheduler().cancelTask(rp_id)
                    player.gameMode = GameMode.SURVIVAL
                    player.teleport(red_spawn)
                    player.sendActionBar(Component.text("리스폰!"))
                }
            }, 0, 2)
        } else if(blueTeam.contains(player)) {
            player.gameMode = GameMode.SPECTATOR
            player.teleport(blue_spawn)
            var i = 10.0

            var rp_id = 0

            rp_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(StoneBattle::class.java), {
                if(0.0 < i) {
                    player.sendActionBar(Component.text("리스폰까지 ${(floor(i*10))/10}초"))
                    i -= 0.1
                } else {
                    Bukkit.getScheduler().cancelTask(rp_id)
                    player.gameMode = GameMode.SURVIVAL
                    player.teleport(blue_spawn)
                    player.sendActionBar(Component.text("리스폰!"))
                }
            }, 0, 2)
        }
    }
}