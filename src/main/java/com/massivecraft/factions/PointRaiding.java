package com.massivecraft.factions;

import com.massivecraft.factions.util.MultiversionMaterials;
import com.massivecraft.factions.zcore.persist.templates.HologramTemplate;
import com.massivecraft.factions.zcore.persist.templates.ItemStackTemplate;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashMap;

public class PointRaiding {

    public static boolean POINT_RAIDING_ENABLED = true;
    public static ItemStackTemplate stackerItem = new ItemStackTemplate(
            MultiversionMaterials.DIAMOND_BLOCK.parseMaterial(),
            1,
            "&b&lPoint Stacker",
            Arrays.asList(
                    "&b ",
                    "&7Place in your Faction's Claim",
                    "&7Once placed, &bright-click&7 to begin..."
            ),
            true
    );
    public static double pointStackerCost = 5000;
	public static HologramTemplate hologramConfiguration = new HologramTemplate(
			  Arrays.asList(
						 "&b{faction}&b's Point Stacker",
						 "&f{points} Points"
			  ),
			  MultiversionMaterials.DIAMOND.parseMaterial(),
			  true,
			  2.5
	);

	public static HashMap<String, Location> stackerLocations = new HashMap<>();





    private static transient PointRaiding i = new PointRaiding();
    public static void load() {
	    SavageFactions.plugin.persist.loadOrSaveDefault(i, PointRaiding.class, "pointraiding");
    }

    public static void save() {
        SavageFactions.plugin.persist.save(i);
    }
}
