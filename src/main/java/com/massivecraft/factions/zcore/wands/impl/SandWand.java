package com.massivecraft.factions.zcore.wands.impl;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.Particles.ParticleEffect;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.util.Util;
import com.massivecraft.factions.zcore.nbtapi.NBTItem;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.wands.Wand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SandWand extends Wand {


   private Block block;

   public SandWand(ItemStack wandItemStack, Player player, Block block) {
      this.wandItemStack = wandItemStack;
      this.player = player;
      this.block = block;
   }

   public static ItemStack buildItem(Integer uses) {
      ItemStack itemStack = Wand.buildItem();
      NBTItem nbtItem = new NBTItem(itemStack);
      nbtItem.setBoolean("Sand", true);
      if (uses == null) {
         uses = Conf.sandWandUses;
         nbtItem.setInteger("Uses", uses);
      } else {
         nbtItem.setInteger("Uses", uses);
      }
      itemStack = nbtItem.getItem();
      return new ItemBuilder(itemStack)
              .name(Conf.sandWandItemName)
              .lore(Util.colorWithPlaceholders(Conf.sandWandItemLore
                      , new Placeholder("{uses}", uses + "")))
              .build();
   }


   public static boolean isSandWand(ItemStack itemStack) {
      if (!Wand.isWand(itemStack)) {
         return false;
      }
      NBTItem nbtItem = new NBTItem(itemStack);
	   return nbtItem.hasKey("Sand");
   }


   public void run() {
      if (!Conf.sandWandItemsToRemove.contains(block.getType())) {
         this.player.sendMessage(TL.WAND_THISBLOCK_ISINVALID.toString());
	      updateWand();
         return;
      }
      ArrayList<Block> validBlocks = new ArrayList<>();
      validBlocks.add(block);
      Block loopBlock = block.getLocation().add(0, 1, 0).getBlock();
      validBlocks.add(loopBlock);
      int maxY = 1;
      while (Conf.sandWandItemsToRemove.contains(loopBlock.getType())) {
         maxY++;
         loopBlock = loopBlock.getLocation().add(0, 1, 0).getBlock();

         validBlocks.add(loopBlock);
      }
      if (validBlocks.size() > 0) {
         wandUsed = true;
      }
      Location location = block.getLocation();
      showHelixAtLocation(location, maxY);
      for (Block removalBlock : validBlocks) {
         removalBlock.setType(Material.AIR);
      }
      updateWand();
   }


   private void showHelixAtLocation(Location location, int maxY) {
      int radius = 1;
      for (double y = 0; y <= maxY; y += 0.05) {
         double x = radius * Math.cos(y);
         double z = radius * Math.sin(y);
         ParticleEffect.REDSTONE
                 .display(new ParticleEffect.OrdinaryColor(255, 255, 255)
                         , new Location(location.getWorld(), location.getX() + x, location.getY() + y, location.getZ() + z), location.getWorld().getPlayers());
      }
   }


}
