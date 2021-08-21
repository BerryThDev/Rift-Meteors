package me.berry.oreMeteor.classes.meteor;

import me.berry.oreMeteor.classes.utility.FloatVec;
import org.bukkit.Location;

public class MeteorArmorStand extends CrashedMeteor {
	private static final long serialVersionUID = -2635339588533891372L;
	private transient Location standLocation;
	private final transient Location startLocation;
	private double progress;
	private final double max;
	private FloatVec headLoc;
	private final int entId;

	public MeteorArmorStand(CrashedMeteor crashedMeteor, Location standLocation, int entId, float headZVal) {
		super(crashedMeteor.getConfigName(), crashedMeteor.getOfficialName(), crashedMeteor.getLocation(), crashedMeteor.getPreviousBlock(), crashedMeteor.getMaxHits(), crashedMeteor.getHits(), crashedMeteor.getExpireAfter());
		this.standLocation = standLocation;
		this.progress = 0;
		this.startLocation = standLocation;
		this.entId = entId;
		this.headLoc = new FloatVec(0, 0, headZVal);
		// Dynamic distance calc could be used for a customizable x offset
		//this.max = Math.abs(Math.abs(crashedMeteor.getLocation().getBlockX()) - Math.abs(standLocation.getBlockX())) - 1.5;
		this.max = 19.5;
	}

	public FloatVec getHeadLoc() {
		return headLoc;
	}

	public void setHeadLoc(FloatVec headLoc) {
		this.headLoc = headLoc;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStandLocation(Location standLocation) {
		this.standLocation = standLocation;
	}

	public int getEntId() {
		return this.entId;
	}

	public double getMax() {
		return max;
	}

	public Location getStandLocation() {
		return standLocation;
	}

	public double getProgress() {
		return progress;
	}

	public void updateProgress() {
		this.progress = this.progress + 0.5;
	}
}
