package com.massivecraft.factions.zcore.persist.templates;

import org.bukkit.Material;

import java.util.List;

public class ItemStackTemplate {

    private Material type;
    private int amount;
    private String name;
    private List<String> lore;
    private boolean glowing;

    public ItemStackTemplate(Material type, int amount, String name, List<String> lore, boolean glowing) {
        this.type = type;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.glowing = glowing;
    }


    public Material getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public boolean isGlowing() {
        return glowing;
    }
}
