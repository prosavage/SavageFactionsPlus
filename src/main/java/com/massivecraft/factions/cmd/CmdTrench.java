package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.MultiversionMaterials;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.zcore.nbtapi.NBTItem;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdTrench extends FCommand {


    public CmdTrench() {
        super();
        this.aliases.add("trench");

        this.requiredArgs.add("player");
        this.requiredArgs.add("pick,shovel");
        this.requiredArgs.add("radius");

        this.permission = Permission.GIVETRENCH.node;
        this.disableOnLock = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeColeader = false;
        senderMustBeAdmin = false;

    }

    @Override
    public void perform() {
        Player player = argAsPlayer(0);
        String toolType = args.get(1);
        if (!toolType.equalsIgnoreCase("pick") && !toolType.equalsIgnoreCase("shovel")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "The tooltype needs to be pick or shovel"));
            return;
        }
        int radius = argAsInt(2);
        if (radius % 2 == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "The radius needs to be an odd number."));
            return;
        }
        FPlayer target = FPlayers.getInstance().getByPlayer(player);
        Placeholder radiusPlaceholder = new Placeholder("{radius}", radius + "");
        Placeholder itemTypePlaceholder = new Placeholder("{tooltype}", toolType);
        Placeholder playerPlaceholder = new Placeholder("{player}", player.getName());

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), createTrenchTool(toolType, radius));
            target.msg(TL.COMMAND_TRENCH_NOSPACE);
        } else {
            player.getInventory().addItem(createTrenchTool(toolType, radius));
        }
        target.msg(TL.COMMAND_TRENCH_GIVEN.toString()
                .replace(radiusPlaceholder.getKey(), radiusPlaceholder.getValue())
                .replace(itemTypePlaceholder.getKey(), itemTypePlaceholder.getValue())
                .replace(playerPlaceholder.getKey(), playerPlaceholder.getValue()));
        fme.msg(TL.COMMAND_TRENCH_SUCCESS.toString()
                .replace(radiusPlaceholder.getKey(), radiusPlaceholder.getValue())
                .replace(itemTypePlaceholder.getKey(), itemTypePlaceholder.getValue())
                .replace(playerPlaceholder.getKey(), playerPlaceholder.getValue()));


    }

    private ItemStack createTrenchTool(String tooltype, int radius) {
        Material trenchMaterial;
        if (tooltype.equalsIgnoreCase("pick")) {
            tooltype = "Pickaxe";
            trenchMaterial = MultiversionMaterials.DIAMOND_PICKAXE.parseMaterial();
        } else {
            tooltype = "Shovel";
            trenchMaterial = MultiversionMaterials.DIAMOND_SHOVEL.parseMaterial();
        }
        Placeholder radiusPlaceholder = new Placeholder("{radius}", radius + "");
        Placeholder itemTypePlaceholder = new Placeholder("{tooltype}", tooltype);
        ItemStack trenchItemStack = new ItemStack(trenchMaterial, 1);
        ItemStack trenchItem = new ItemBuilder(trenchItemStack).name(Conf.trenchToolName
                .replace(radiusPlaceholder.getKey(), radiusPlaceholder.getValue())
                .replace(itemTypePlaceholder.getKey(), itemTypePlaceholder.getValue()))
                .lore(Conf.trenchToolLore, radiusPlaceholder, itemTypePlaceholder).build();
        for (String rawEnchant : Conf.trenchToolEnchantments.keySet()) {
            Enchantment enchantment = Enchantment.getByName(rawEnchant.toUpperCase());

            trenchItem = new ItemBuilder(trenchItem).enchant(enchantment, Conf.trenchToolEnchantments.get(rawEnchant)).build();
        }
        NBTItem nbtTrenchItem = new NBTItem(trenchItem);
        nbtTrenchItem.setBoolean("trench", true);
        nbtTrenchItem.setInteger("radius", radius);
        return nbtTrenchItem.getItem();
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TRENCH_DESCRIPTION;
    }


}
