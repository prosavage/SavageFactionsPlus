package com.massivecraft.factions.zcore.persist.templates;

import org.bukkit.Material;

import java.util.List;

public class HologramTemplate {
	private List<String> hologramLines;
	private Material hologramMaterial;
	private boolean useMaterial;
	private double hologramYOffSet;

	public HologramTemplate(List<String> hologramLines, Material hologramMaterial, boolean useMaterial, double hologramYOffSet) {
		this.hologramLines = hologramLines;
		this.hologramMaterial = hologramMaterial;
		this.useMaterial = useMaterial;
		this.hologramYOffSet = hologramYOffSet;
	}


	public List<String> getHologramLines() {
		return hologramLines;
	}

	public Material getHologramMaterial() {
		return hologramMaterial;
	}

	public boolean isUsingMaterial() {
		return useMaterial;
	}

	public double getHologramYOffSet() {
		return hologramYOffSet;
	}
}
