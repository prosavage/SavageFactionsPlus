package com.massivecraft.factions.zcore;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.massivecraft.factions.*;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.util.Util;
import com.massivecraft.factions.zcore.nbtapi.NBTItem;
import com.massivecraft.factions.zcore.persist.templates.ItemStackTemplate;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class PointStacker {

	public static ItemStack buildItemStack() {
		ItemStackTemplate pointStackerInfo = PointRaiding.stackerItem;
		ItemStack itemStack = new ItemBuilder(pointStackerInfo.getType())
				  .name(pointStackerInfo.getName())
				  .lore(pointStackerInfo.getLore())
				  .glowing(pointStackerInfo.isGlowing())
				  .build();
		NBTItem nbtItem = new NBTItem(itemStack);
		nbtItem.setBoolean("Stacker", true);
		return nbtItem.getItem();
	}

	public static boolean isPointStacker(ItemStack itemStack) {
		return new NBTItem(itemStack).hasKey("Stacker");
	}

	public static void createPointStacker(Faction faction, Location location, FPlayer fplayer) {
		createPointStackerHologram(faction, location, false);
		for (FPlayer fplyr : faction.getFPlayers()) {
			fplyr.msg(TL.STACKER_SUCCESS.toString().replace("{player}", fplayer.getName()));
		}
	}

	public static void createAllHolograms() {
		for (String id : PointRaiding.stackerLocations.keySet()) {
			// Bukkit.broadcastMessage("Doing " + id + "    " + Factions.getInstance().getFactionById(id).getTag() + "  " +  PointRaiding.stackerLocations.get(id));
			createPointStackerHologram(Factions.getInstance().getFactionById(id), PointRaiding.stackerLocations.get(id), true);
		}
	}


	public static void createPointStackerHologram(Faction faction, Location location, boolean fromStartup) {
		PointRaiding.stackerLocations.put(faction.getId(), location);
		Location holoLocation = location;
		if (!fromStartup) {
			holoLocation = location.add(0.5, PointRaiding.hologramConfiguration.getHologramYOffSet(), 0.5);
		}
		Hologram hologram = HologramsAPI.createHologram(SavageFactions.plugin, holoLocation);
		hologram.clearLines();
		for (String message : PointRaiding.hologramConfiguration.getHologramLines()) {
			hologram.appendTextLine(Util.color(message,
					  new Placeholder("{faction}", faction.getTag()),
					  new Placeholder("{points}", faction.getPoints() + "")));
		}
		if (PointRaiding.hologramConfiguration.isUsingMaterial()) {
			hologram.insertItemLine(hologram.size(), new ItemStack(PointRaiding.stackerItem.getType()));
		}
		SavageFactions.hologramMap.put(location, hologram);
	}



}
