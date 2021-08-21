package me.berry.oreMeteor.classes.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;

public class SerializableLoc implements Serializable {
	private static final long serialVersionUID = 1981817036172166023L;
	private final String worldName;
	private final double x;
	private final double y;
	private final double z;

	public SerializableLoc(Location location) {
		this.worldName = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
	}

	public String getWorldName() {
		return worldName;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public Location getBukkitLocation() {
		return new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z);
	}
}
