package io.github.blugon0921.stonebattle.command

import io.github.blugon0921.stonebattle.StoneBattle.Companion.yaml
import io.github.blugon0921.stonebattle.team.Blue.Companion.blueTeam
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_bar
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_core
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_join
import io.github.blugon0921.stonebattle.team.Blue.Companion.blue_spawn
import io.github.blugon0921.stonebattle.team.Red.Companion.redTeam
import io.github.blugon0921.stonebattle.team.Red.Companion.red_bar
import io.github.blugon0921.stonebattle.team.Red.Companion.red_core
import io.github.blugon0921.stonebattle.team.Red.Companion.red_join
import io.github.blugon0921.stonebattle.team.Red.Companion.red_spawn
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class Kommand : CommandExecutor,TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(command.name == "sb") {
            if(sender !is Player) return false

            if(args[0] == "setting" && args.size == 3) {
                val player = sender
                val location = player.location

                if(args[1] == "red") {
                    if(args[2] == "join") {
                        red_join = location
                        yaml.set("red.join", location)
                        player.sendMessage("${ChatColor.RED}RED${ChatColor.WHITE}팀의 팀 설정 진영을 설정했습니다")
                    } else if(args[2] == "core") {
                        red_core = location
                        if(player.getTargetBlock(5) == null) return false
                        yaml.set("red.core", player.getTargetBlock(5))
                        player.sendMessage("${ChatColor.RED}RED${ChatColor.WHITE}팀의 코어를 설정했습니다")
                    } else if(args[2] == "spawn") {
                        red_spawn = location
                        yaml.set("red.spawn", location)
                        player.sendMessage("${ChatColor.RED}RED${ChatColor.WHITE}팀의 스폰을 설정했습니다")
                    }
                } else if(args[1] == "blue") {
                    if(args[2] == "join") {
                        blue_join = location
                        yaml.set("blue.join", location)
                        player.sendMessage("${ChatColor.BLUE}BLUE${ChatColor.WHITE}팀의 팀 설정 진영을 설정했습니다")
                    } else if(args[2] == "core") {
                        blue_core = location
                        if(player.getTargetBlock(5) == null) return false
                        yaml.set("blue.core", player.getTargetBlock(5))
                        player.sendMessage("${ChatColor.BLUE}BLUE${ChatColor.WHITE}팀의 코어를 설정했습니다")
                    } else if(args[2] == "spawn") {
                        blue_spawn = location
                        yaml.set("blue.spawn", location)
                        player.sendMessage("${ChatColor.BLUE}BLUE${ChatColor.WHITE}팀의 스폰을 설정했습니다")
                    }
                }
            } else if(args.size == 1 && args[0] == "start") {
                red_bar.progress = 1.0
                blue_bar.progress = 1.0

                for(players in Bukkit.getOnlinePlayers()) {
                    //RED TEAM
                    if(redTeam.contains(players)) {
                        players.teleport(red_spawn)
                        red_bar.addPlayer(players)

                    //BLUE TEAM
                    } else if(blueTeam.contains(players)) {
                        players.teleport(blue_spawn)
                        blue_bar.addPlayer(players)
                    }
                }
            } else if(args.size == 1 && args[0] == "teamlist") {
                val player = sender

                val redTeams = arrayListOf<String>()
                val blueTeams = arrayListOf<String>()

                for(r in redTeam) {
                    redTeams.add(r.name)
                }
                for(b in blueTeam) {
                    blueTeams.add(b.name)
                }

                player.sendMessage("${ChatColor.RED}RED TEAM${ChatColor.WHITE} : $redTeams")
                player.sendMessage("${ChatColor.BLUE}BLUE TEAM${ChatColor.WHITE} : $blueTeams")
            }
        }
        return false
    }



    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        if(command.name == "sb") {
            //args 1
            if(args.size == 1) {
                val r1 = arrayListOf("setting", "start", "teamlist")

                val r = mutableListOf<String>()
                for(r2 in r1) {
                    if(r2.lowercase().startsWith(args[0].lowercase())) {
                        r.add(r2)
                    }
                }
                return r

            //args 2
            } else if(args.size == 2 && args[0] == "setting") {
                val r1 = arrayListOf("red", "blue")

                val r = mutableListOf<String>()
                for(r2 in r1) {
                    if(r2.lowercase().startsWith(args[1].lowercase())) {
                        r.add(r2)
                    }
                }
                return r

            //args 3
            } else if(args.size == 3 && args[0] == "setting") {
                val r1 = arrayListOf("core", "spawn", "join")

                val r = mutableListOf<String>()
                for(r2 in r1) {
                    if(r2.lowercase().startsWith(args[2].lowercase())) {
                        r.add(r2)
                    }
                }
                return r

            //start
            } else if(args[0] == "start" || args[0] == "teamlist") {
                return Collections.emptyList()
            }
        }
        return null
    }
}