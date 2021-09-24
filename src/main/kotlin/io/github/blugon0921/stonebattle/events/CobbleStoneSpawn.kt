package io.github.blugon0921.stonebattle.events

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class CobbleStoneSpawn : Listener {

    @EventHandler
    fun breakStone(event : BlockBreakEvent) {
        val player = event.player
        val breakTool = player.inventory.itemInMainHand
        val block = event.block
        val world = block.world
        val location = block.location.add(0.5, 0.0, 0.5)

        if(breakTool.type != Material.STONE_PICKAXE) return

        if(!block.getSideBlocksType().contains(Material.LAVA)) return
        if(!block.getSideBlocksType().contains(Material.WATER)) return


        val random = (Math.random()*7).toInt()

        for(i in 0..random) {
            val item = world.dropItem(location, ItemStack(Material.COBBLESTONE, 1))
            item.velocity = Vector((Math.random()*0.1)-0.05, 0.2, (Math.random()*0.1)-0.05)
        }
    }




    fun Block.getSideBlocksType() : MutableList<Material> {
        val block = this
        val location = block.location
        val blocks = mutableListOf<Material>()

        blocks.add(location.clone().add(1.0,0.0,0.0).block.type)
        blocks.add(location.clone().add(-1.0,0.0,0.0).block.type)
        blocks.add(location.clone().add(0.0,0.0,1.0).block.type)
        blocks.add(location.clone().add(0.0,0.0,-1.0).block.type)
        return blocks
    }
}