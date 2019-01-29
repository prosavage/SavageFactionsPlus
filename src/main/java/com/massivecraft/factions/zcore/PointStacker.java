package com.massivecraft.factions.zcore;

import com.massivecraft.factions.PointRaiding;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.zcore.persist.templates.ItemStackTemplate;
import org.bukkit.inventory.ItemStack;

public class PointStacker {

    public static ItemStack buildItemStack() {
        ItemStackTemplate pointStackerInfo = PointRaiding.stackerItem;
        return new ItemBuilder(pointStackerInfo.getType())
                .name(pointStackerInfo.getName())
                .lore(pointStackerInfo.getLore())
                .glowing(pointStackerInfo.isGlowing())
                .build();
    }


}
