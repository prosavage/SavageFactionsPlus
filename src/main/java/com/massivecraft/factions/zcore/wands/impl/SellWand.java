package com.massivecraft.factions.zcore.wands.impl;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.SavageFactions;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.Placeholder;
import com.massivecraft.factions.util.Util;
import com.massivecraft.factions.zcore.nbtapi.NBTItem;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.wands.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class SellWand extends Wand {

    public SellWand(ItemStack wandItemStack, Player player, Chest chest) {
        this.wandItemStack = wandItemStack;
        this.player = player;
        this.chest = chest;
    }

    public static ItemStack buildItem(Integer uses, Integer tier) {
        ItemStack itemStack = Wand.buildItem();
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setBoolean("Sell", true);
        if (uses == null) {
            uses = Conf.sellWandUses;
            nbtItem.setInteger("Uses", uses);

        } else {
            nbtItem.setInteger("Uses", uses);
        }
        if (tier == null) {
            tier = 1;
            nbtItem.setInteger("Tier", 1);
        } else {
            nbtItem.setInteger("Tier", tier);
        }
        itemStack = nbtItem.getItem();
        return new ItemBuilder(itemStack)
                .name(Conf.sellWandItemName)
                .lore(Util.colorWithPlaceholders(Conf.sellWandItemLore
                        , new Placeholder("{uses}", uses + "")
                        , new Placeholder("{tier}", tier + "")))
                .build();
    }


    public static boolean isSellWand(ItemStack itemStack) {
        if (!Wand.isWand(itemStack)) {
            return false;
        }
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("Sell");
    }

    protected int getTier() {
        NBTItem nbtItem = new NBTItem(wandItemStack);
        if (!nbtItem.hasKey("Tier")) {
            return 1;
        }
        return nbtItem.getInteger("Tier");
    }

    public void run() {
        Bukkit.getScheduler().runTaskAsynchronously(SavageFactions.plugin, () -> {
            double multiplier = Conf.sellWandUpgradeTierRatio.get(getTier());
            int itemsSold = 0;
            double moneyEarned = 0;
            HashMap<Material, Double> costMap = Conf.costMap;
            chest.update();
            if (chest.getBlockInventory() == null) {
                updateWand();
            }
            Inventory inventory = chest.getBlockInventory();
            ArrayList<ItemStack> itemStacks = new ArrayList<>();
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) {
                    continue;
                }
                if (!costMap.containsKey(itemStack.getType())) {
                    itemStacks.add(itemStack);
                    continue;
                }
                moneyEarned += costMap.get(itemStack.getType()) * itemStack.getAmount();
                itemsSold += itemStack.getAmount();
                itemStacks.add(new ItemStack(Material.AIR));
            }
            inventory.setContents(itemStacks.toArray(new ItemStack[itemStacks.size()]));
            chest.update();
            if (moneyEarned > 0) {
                wandUsed = true;
            }
            if (wandUsed) {
                deposit(player, itemsSold, moneyEarned, multiplier);
            }
            updateWand();

        });
    }

    private void deposit(Player player, int items, double amount, double multiplier) {
        amount = amount * multiplier;
        SavageFactions.plugin.getEcon().depositPlayer(player, amount);
        player.sendMessage(Util.color(TL.WAND_DEPOSIT_MONEY.toString()
                .replace("{amount}", amount + "")
                .replace("{items}", items + "")
                .replace("{multiplier}", multiplier + "")));
    }


}
