package me.berry.oreMeteor.classes.meteor;

import me.berry.oreMeteor.classes.utility.SerializableLoc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class CrashedMeteor implements Serializable {
	private static final long serialVersionUID = 1897074356337236418L;
	private final String configName, officialName;
	private final SerializableLoc location;
	private final Material previousBlock;
	private int hits;
	private final int maxHits;
	private long updatedAt;
	private final long startAt;
	private final String expireAfter;
	private UUID lastHitPlayerUUID;

	public CrashedMeteor(String configName, String officialName, Location location, Material previousBlock, int maxHits, int hits, String expireAfter) {
		SerializableLoc serializableLoc = new SerializableLoc(location);
		Date date = new Date();

		this.location = serializableLoc;
		this.previousBlock = previousBlock;
		this.maxHits = maxHits;
		this.expireAfter = expireAfter;
		this.officialName = officialName;
		this.lastHitPlayerUUID = null;
		this.updatedAt = 0;
		this.hits = hits;
		this.startAt = date.getTime();
		this.configName = configName;
	}

	public Location getLocation() {
		return this.location.getBukkitLocation();
	}

	public String getConfigName() {
		return configName;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void currentUpdatedAt() {
		Date date = new Date();

		this.updatedAt = date.getTime();
	}

	public long getStartAt() {
		return startAt;
	}

	public String getOfficialName() {
		return officialName;
	}

	public String getExpireAfter() {
		return expireAfter;
	}

	public UUID getLastHitPlayerUUID() {
		return lastHitPlayerUUID;
	}

	public void setLastHitPlayerUUID(UUID lastHitPlayerUUID) {
		this.lastHitPlayerUUID = lastHitPlayerUUID;
	}

	public Material getPreviousBlock() {
		return previousBlock;
	}

	public int getHits() {
		return hits;
	}

	public int getMaxHits() {
		return maxHits;
	}

	public void increaseHits() {
		this.hits = this.hits + 1;
	}

	public void remove() {
		Location locationForBlock = new Location(Bukkit.getWorld(this.location.getWorldName()), this.location.getX(), this.location.getZ(), this.location.getZ());
		locationForBlock.getBlock().setType(Material.AIR, false);
	}
}
