package com.massivecraft.factions.listeners;


import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.massivecraft.factions.*;
import com.massivecraft.factions.zcore.PointStacker;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PointStackerListener implements Listener {

	@EventHandler
	public void onPointStackerPlace(BlockPlaceEvent event) {
		if (!FactionsBlockListener.playerCanBuildDestroyBlock(event.getPlayer(), event.getBlockPlaced().getLocation(), "build", false)
				  || !PointStacker.isPointStacker(event.getItemInHand())) {
			return;
		}
		FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
		Faction myFaction = fPlayer.getFaction();
		Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlockPlaced().getLocation()));
		Location location = event.getBlockPlaced().getLocation();
		if (myFaction.isWilderness()
				  || factionAt != myFaction) {
			fPlayer.msg(TL.STACKER_INVALIDPLACEMENT);
			event.setCancelled(true);
			return;
		}

		PointStacker.createPointStacker(myFaction, location, fPlayer);
	}

	@EventHandler
	public void pointStackerBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != PointRaiding.stackerItem.getType()
				  || PointRaiding.stackerLocations.values().contains(event.getBlock().getLocation())) {
			return;
		}
		FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
		Faction factionAt = Board.getInstance().getFactionAt(new FLocation(event.getBlock().getLocation()));
		Faction faction = fPlayer.getFaction();
		if (faction != factionAt) {
			fPlayer.msg(TL.STACKER_CANNOTBREAK);
			event.setCancelled(true);
			return;
		}
		Location location = event.getBlock().getLocation();
		Hologram hologram = SavageFactions.hologramMap.get(location.add(0.5, PointRaiding.hologramConfiguration.getHologramYOffSet(), 0.5));
		PointRaiding.stackerLocations.remove(faction.getId());
		hologram.delete();
	}


}
