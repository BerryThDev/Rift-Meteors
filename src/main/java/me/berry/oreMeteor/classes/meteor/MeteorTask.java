package me.berry.oreMeteor.classes.meteor;

import java.util.Date;

public class MeteorTask extends Meteor {
	private long updatedAt;
	private final long startedAt;

	public MeteorTask(Meteor meteor) {
		super(meteor.getConfigName(), meteor.getName(), meteor.getWorld(), meteor.getWgRegion(), meteor.getOre(), meteor.getExpiresAfter(), meteor.getSpawnRateMax(), meteor.getSpawnRateMin());

		Date date = new Date();

		this.updatedAt = 0;
		this.startedAt = date.getTime();
	}

	public long getStartedAt() {
		return startedAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void currentUpdatedAt() {
		Date date = new Date();

		this.updatedAt = date.getTime();
	}

	public Meteor getMeteor() {
		return this;
	}
}
