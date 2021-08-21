package me.berry.oreMeteor.classes.meteor;

import org.bukkit.Material;

public class Meteor {
	private final String configName;
	private final String name;
	private final String world;
	private long spawnRate;
	private final String spawnRateMax;
	private final String spawnRateMin;
	private final String wgRegion;
	private final String expiresAfter;
	private final Material ore;

	public Meteor(String configName, String name, String world, String wgRegion, Material ore, String expiresAfter, String spawnRateMax, String spawnRateMin) {
		this.wgRegion = wgRegion;
		this.world = world;
		this.expiresAfter = expiresAfter;
		this.name = name;
		this.ore = ore;
		this.configName = configName;
		this.spawnRateMax = spawnRateMax;
		this.spawnRateMin = spawnRateMin;
		this.spawnRate = 0;
	}

	public long getSpawnRate() {
		return spawnRate;
	}

	public void setSpawnRate(long spawnRate) {
		this.spawnRate = spawnRate;
	}

	public String getSpawnRateMax() {
		return spawnRateMax;
	}

	public String getSpawnRateMin() {
		return spawnRateMin;
	}

	public String getConfigName() {
		return configName;
	}

	public String getExpiresAfter() {
		return expiresAfter;
	}

	public Material getOre() {
		return ore;
	}

	public String getName() {
		return name;
	}

	public String getWorld() {
		return world;
	}

	public String getWgRegion() {
		return wgRegion;
	}
}
