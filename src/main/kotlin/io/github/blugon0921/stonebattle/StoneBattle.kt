package io.github.blugon0921.stonebattle

import io.github.blugon09.itemhelper.ItemObject
import io.github.blugon09.itemhelper.component
import io.github.blugon0921.stonebattle.command.Kommand
import io.github.blugon0921.stonebattle.events.CobbleStoneSpawn
import io.github.blugon0921.stonebattle.events.Death
import io.github.blugon0921.stonebattle.events.Stone
import io.github.blugon0921.stonebattle.team.Blue
import io.github.blugon0921.stonebattle.team.Blue.Companion.blueTeam
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_bar
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_core
import io.github.blugon0921.stonebattle.team.Red
import io.github.blugon0921.stonebattle.team.Red.Companion.redTeam
import io.github.blugon0921.stonebattle.team.Red.Companion.red_bar
import io.github.blugon0921.stonebattle.team.Red.Companion.red_core
import io.github.blugon0921.stonebattle.team.Red.Companion.red_join
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class StoneBattle : JavaPlugin(),Listener {

    companion object {
        val team_info = File("plugins/StoneBattle/config.yml")
        val yaml = YamlConfiguration.loadConfiguration(team_info)

        var playing = false
    }


    override fun onEnable() {
        logger.info("Plugin Enable")
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getPluginManager().registerEvents(Stone(), this)
        Bukkit.getPluginManager().registerEvents(CobbleStoneSpawn(), this)
        Bukkit.getPluginManager().registerEvents(Death(), this)

        Bukkit.getPluginManager().registerEvents(Red(), this)
        Bukkit.getPluginManager().registerEvents(Blue(), this)


        this.getCommand("sb")!!.apply {
            setExecutor(Kommand())
            tabCompleter = Kommand()
        }

        if(!team_info.exists()) {
            saveConfig()
            config.options().copyDefaults(true)
            saveConfig()
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
            for(players in Bukkit.getOnlinePlayers()) {
                if(redTeam.contains(players)) {
                    val head = ItemObject(Material.RED_CONCRETE, 1, " ".component()).build()

                    players.inventory.setItem(EquipmentSlot.HEAD, head)
                }
                if(blueTeam.contains(players)) {
                    val head = ItemObject(Material.BLUE_CONCRETE, 1, " ".component()).build()

                    players.inventory.setItem(EquipmentSlot.HEAD, head)
                }
            }
        }, 0, 1)
    }

    override fun onDisable() {
        for(players in Bukkit.getOnlinePlayers()) {
            red_bar.removePlayer(players)
            blue_bar.removePlayer(players)
        }
        logger.info("Plugin Disable")
    }

    @EventHandler
    fun headClick(event : InventoryClickEvent) {
        if(event.clickedInventory != (event.whoClicked as Player).inventory) return
        if(event.slot == 39) {
            event.isCancelled = true
        }
    }


    @EventHandler
    fun coreBreak(event : BlockBreakEvent) {
        val block = event.block
        val location = block.location

        if(location != red_core && location != blue_core) return
        if(event.player.gameMode == GameMode.CREATIVE) return
        event.isCancelled = true
    }


    @EventHandler
    fun damage(event : EntityDamageByEntityEvent) {
        if(!playing) {
            if(event.damager !is Player) return
            if(event.entity !is Player) return
            event.isCancelled = true
        }
    }
}