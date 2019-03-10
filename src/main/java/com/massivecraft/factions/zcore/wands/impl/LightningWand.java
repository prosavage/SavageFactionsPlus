package com.massivecraft.factions.zcore.wands.impl;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.util.Util;
import com.massivecraft.factions.zcore.nbtapi.NBTItem;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.wands.Wand;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class LightningWand extends Wand {

	private Entity target;

	public LightningWand(ItemStack wandItemStack, Player player, Entity entity) {
		this.wandItemStack = wandItemStack;
		this.player = player;
		this.target = entity;
	}

	public static ItemStack buildItem(Integer uses) {
		ItemStack itemStack = Wand.buildItem();
		NBTItem nbtItem = new NBTItem(itemStack);
		nbtItem.setBoolean("Lightning", true);
		if (uses == null) {
			uses = Conf.lightningWandUses;
			nbtItem.setInteger("Uses", uses);
		} else {
			nbtItem.setInteger("Uses", uses);
		}
		itemStack = nbtItem.getItem();
		return new ItemBuilder(itemStack)
				  .name(Conf.lightningWandItemName)
				  .lore(Util.colorWithPlaceholders(Conf.lightningWandItemLore
							 , new Placeholder("{uses}", uses + "")))
				  .build();
	}


	public static boolean isLightningWand(ItemStack itemStack) {
		if (!Wand.isWand(itemStack)) {
			return false;
		}
		NBTItem nbtItem = new NBTItem(itemStack);
		return nbtItem.hasKey("Lightning");
	}


	@Override
	public void run() {
		if (target.getType() != EntityType.CREEPER) {
			player.sendMessage(TL.WAND_LIGHTNING_WRONGENTITY.toString());
			return;
		}
		target.getWorld().strikeLightningEffect(target.getLocation());
		Creeper creeper = (Creeper) target;
		creeper.setPowered(true);
		player.sendMessage(TL.WAND_LIGHTNING_SUCCESS.toString());
		updateWand();
	}

}
