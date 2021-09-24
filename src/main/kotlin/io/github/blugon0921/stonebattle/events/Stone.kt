package io.github.blugon0921.stonebattle.events

import io.github.blugon0921.stonebattle.StoneBattle
import io.github.blugon0921.stonebattle.team.Blue.Companion.blueTeam
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_bar
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_core
import io.github.blugon0921.stonebattle.team.Red.Companion.redTeam
import io.github.blugon0921.stonebattle.team.Red.Companion.red_bar
import io.github.blugon0921.stonebattle.team.Red.Companion.red_core
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import net.md_5.bungee.api.ChatColor
import org.bukkit.inventory.EquipmentSlot

class Stone : Listener {


    //돌던지기
    @EventHandler
    fun launchStone(event : PlayerInteractEvent) {
        val player = event.player
        val world = player.world
        val location = player.eyeLocation
        val eventItem = player.inventory.itemInMainHand

        if(player.gameMode == GameMode.SPECTATOR) return
        if(event.action != Action.LEFT_CLICK_AIR) return
        if(eventItem.type != Material.COBBLESTONE) return
        if(player.getCooldown(Material.COBBLESTONE) != 0) return

        world.playSound(location, Sound.ENTITY_ARROW_SHOOT, 0.5f, 0.5f)

        val stone = world.spawn(location, Snowball::class.java)
        stone.shooter = player
        stone.item = ItemStack(Material.COBBLESTONE)
        stone.velocity = location.direction.multiply(1)

        player.setCooldown(Material.COBBLESTONE, 2)

        //돌 다썼을때
        if(player.gameMode != GameMode.CREATIVE) {
            eventItem.amount = eventItem.amount-1
            player.inventory.setItemInMainHand(eventItem)
            if(eventItem.amount == 0) {
                player.playSound(location, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.5f, 1f)
                for(i in 0..35) {
                    if(player.inventory.getItem(i) != null && player.inventory.getItem(i)!!.type == Material.COBBLESTONE) {
                        val item = player.inventory.getItem(i)!!

                        player.inventory.setItem(i, ItemStack(Material.AIR))
                        player.inventory.setItemInMainHand(item)
                        break
                    }
                }
            }
        }
    }



    //돌 맞았을때
    @EventHandler
    fun damageStone(event : ProjectileHitEvent) {
        if(event.hitEntity == null) return

        if(event.entity !is Snowball) return
        if((event.entity as Snowball).item.type != Material.COBBLESTONE) return
        if(event.entity.shooter !is Player) return
        if(event.hitEntity !is LivingEntity) return

        val stone : Snowball = event.entity as Snowball
        val entity : LivingEntity = event.hitEntity as LivingEntity
        val world = stone.world
        val location = stone.location

        entity.noDamageTicks = 0
        entity.damage(1.0, stone.shooter as Player)

        world.spawnParticle(
            Particle.BLOCK_CRACK,
            location,
            20,
            0.0,
            0.0,
            0.0,
            0.0,
            Material.COBBLESTONE.createBlockData(),
            true
        )
        world.playSound(location, Sound.BLOCK_STONE_BREAK, 1f, 1f)
    }



    //돌 블럭에 맞았을때
    @EventHandler
    fun ingroundStone(event : ProjectileHitEvent) {
        if(event.hitBlock == null) return

        if(event.entity !is Snowball) return
        if((event.entity as Snowball).item.type != Material.COBBLESTONE) return
        if(event.entity.shooter !is Player) return
        if(event.hitBlock !is Block) return

        val stone : Snowball = event.entity as Snowball
        val block : Block = event.hitBlock!!
        val world = stone.world
        val location = stone.location

        world.spawnParticle(
            Particle.BLOCK_CRACK,
            location,
            20,
            0.0,
            0.0,
            0.0,
            0.0,
            Material.COBBLESTONE.createBlockData(),
            true
        )
        world.playSound(location, Sound.BLOCK_STONE_BREAK, 1f, 1f)

        //맞은 블럭이 돌일때
        if(block.type != Material.COBBLESTONE) return

        val random = (Math.random()*100+1).toInt()
        if(random < 10) {
            block.breakNaturally(ItemStack(Material.NETHERITE_PICKAXE))
        }
    }



    //돌 코어에 맞았을때
    @EventHandler
    fun attackCoreStone(event : ProjectileHitEvent) {
        if(event.hitBlock == null) return

        if(event.entity !is Snowball) return
        if((event.entity as Snowball).item.type != Material.COBBLESTONE) return
        if(event.entity.shooter !is Player) return
        if(event.hitBlock !is Block) return

        val stone : Snowball = event.entity as Snowball
        val shooter = stone.shooter as Player
        val block = event.hitBlock!!
        val world = stone.world
        val location = stone.location


        //코어 맞았을때 감지

        //RED CORE
        if(block.location == red_core) {
            if(!blueTeam.contains(shooter)) return

            //GameEnd | BLUE WIN
            if(red_bar.progress <= 0.005) {
                for (players in Bukkit.getOnlinePlayers()) {
                    players.sendTitle("게임 종료!", "${ChatColor.BLUE}BLUE", 10, 100, 10)
                    players.teleport(Location(Bukkit.getWorld("world"), 0.5, 151.0, 0.5))
                    players.inventory.setItem(EquipmentSlot.HEAD, ItemStack(Material.AIR))
                    players.gameMode = GameMode.SURVIVAL
                    Bukkit.getScheduler().cancelTasks(JavaPlugin.getPlugin(StoneBattle::class.java))

                    red_bar.removePlayer(players)
                    blue_bar.removePlayer(players)
                    redTeam.clear()
                    blueTeam.clear()
                }
                return
            }
            red_bar.progress = red_bar.progress - 0.005
            val after_progress = red_bar.progress
            shooter.playSound(shooter.location, Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f, 1f)
            red_bar.addPlayer(shooter)

            Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(StoneBattle::class.java), {
                if(red_bar.progress == after_progress) {
                    red_bar.removePlayer(shooter)
                }
            }, 100)
            for(red in redTeam) {
                red.sendActionBar(Component.text("${ChatColor.RED}코어가 공격받고있습니다!"))
            }


        //BLUE CORE
        } else if(block.location == blue_core) {
            if(!redTeam.contains(shooter)) return

            //GameEnd | RED WIN
            if(blue_bar.progress <= 0.005) {
                for (players in Bukkit.getOnlinePlayers()) {
                    players.sendTitle("게임 종료!", "${ChatColor.RED}RED", 10, 100, 10)
                    players.teleport(Location(Bukkit.getWorld("world"), 0.5, 151.0, 0.5))
                    players.gameMode = GameMode.SURVIVAL
                    Bukkit.getScheduler().cancelTasks(JavaPlugin.getPlugin(StoneBattle::class.java))

                    red_bar.removePlayer(players)
                    blue_bar.removePlayer(players)
                    redTeam.clear()
                    blueTeam.clear()
                }
                return
            }
            blue_bar.progress = blue_bar.progress-0.005
            val after_progress = blue_bar.progress
            shooter.playSound(shooter.location, Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f, 1f)
            blue_bar.addPlayer(shooter)

            Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(StoneBattle::class.java), {
                if(blue_bar.progress == after_progress) {
                    blue_bar.removePlayer(shooter)
                }
            }, 100)
            for(blue in blueTeam) {
                blue.sendActionBar(Component.text("${ChatColor.RED}코어가 공격받고있습니다!"))
            }
        }
    }
}