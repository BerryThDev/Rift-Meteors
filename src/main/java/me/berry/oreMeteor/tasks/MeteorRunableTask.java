package me.berry.oreMeteor.tasks;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.Meteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.meteor.MeteorTask;
import me.berry.oreMeteor.classes.utility.MessageType;
import me.berry.oreMeteor.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MeteorRunableTask extends BukkitRunnable {
	public MeteorRunableTask() {}

	@Override
	public void run() {
		OreMeteor oreMeteor = OreMeteor.getInstance();

		for(Map.Entry<String, MeteorTask> entry : oreMeteor.getMeteorTaskHashMap().entrySet()) {
			MeteorTask meteorTask = entry.getValue();
			ScheduleUtil scheduleUtil = new ScheduleUtil();
			MathUtil mathUtil = new MathUtil();
			long frequency;

			if(meteorTask.getSpawnRate() == 0) {
				long randValue = mathUtil.randBetweenLong(scheduleUtil.findRunTime(meteorTask.getSpawnRateMin()),scheduleUtil.findRunTime(meteorTask.getSpawnRateMax()));
				meteorTask.setSpawnRate(randValue);
				frequency = randValue;
			} else frequency = meteorTask.getSpawnRate();

			if(meteorTask.getUpdatedAt() == 0) {
				if (shouldSpawnMeteor(meteorTask.getStartedAt(), frequency)) {
					meteorTask.currentUpdatedAt();
					spawn(meteorTask);
				}
			} else if (meteorTask.getUpdatedAt() == 1) meteorTask.currentUpdatedAt();
			else if (shouldSpawnMeteor(meteorTask.getUpdatedAt(), frequency)) {
				meteorTask.currentUpdatedAt();
				spawn(meteorTask);
			}
		}

		removeExpiredMeteors();
	}

	private void removeExpiredMeteors() {
		Iterator<Map.Entry<UUID, CrashedMeteor>> crashedMeteorIt = OreMeteor.getInstance().getCrashedMeteorHashMap().entrySet().iterator();

		while(crashedMeteorIt.hasNext()) {
			CrashedMeteor crashedMeteor = crashedMeteorIt.next().getValue();
			ScheduleUtil scheduleUtil = new ScheduleUtil();
			ConfigUtil configUtil = new ConfigUtil();
			ChatUtil chatUtil = new ChatUtil();

			long expireTime = scheduleUtil.findRunTime(crashedMeteor.getExpireAfter());

			if(crashedMeteor.getUpdatedAt() == 0) {
				if (shouldRemoveMeteor(crashedMeteor.getStartAt(), expireTime)) {
					crashedMeteor.currentUpdatedAt();
					crashedMeteorIt.remove();
					crashedMeteor.remove();
					chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.EXPIRE_MESSAGE), MessageType.EXPIRE_MESSAGE, crashedMeteor);
				}
			} else if (shouldRemoveMeteor(crashedMeteor.getUpdatedAt(), expireTime)) {
				crashedMeteor.currentUpdatedAt();
				crashedMeteor.remove();
				crashedMeteorIt.remove();
				chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.EXPIRE_MESSAGE), MessageType.EXPIRE_MESSAGE, crashedMeteor);
			}
		}
	}

	private boolean shouldRemoveMeteor(long start, long expireTime) {
		Date date = new Date();
		return (start + expireTime) <= date.getTime();
	}

	private boolean shouldSpawnMeteor(long start, long frequency) {
		Date date = new Date();

		if((start + frequency) <= date.getTime()) {
			OreMeteor.saved = false;
			return true;
		} else return false;

	}

	public void spawn(Meteor meteorClass) {
		ConfigUtil configUtil = new ConfigUtil();
		ChatUtil chatUtil = new ChatUtil();
		ArmorstandUtil armorstandUtil = new ArmorstandUtil();

		Location spawnedAt = new WorldGuardUtil().spawnMeteorAt(meteorClass);

		if(spawnedAt != null) {
			Map<Integer, ItemStack> rewards = configUtil.getRewards(meteorClass.getConfigName());

			CrashedMeteor crashedMeteor = new CrashedMeteor(meteorClass.getConfigName(), meteorClass.getName(), spawnedAt, spawnedAt.getBlock().getType(), rewards.size() + 1, 1, meteorClass.getExpiresAfter());
			Location locationToSpawn = spawnedAt.clone().add(20, 19, 0.5);
			int armorStand = armorstandUtil.spawnStand(locationToSpawn, spawnedAt, Material.getMaterial(configUtil.getOre(crashedMeteor.getConfigName())));
			float angle_C = armorstandUtil.headVal(locationToSpawn, spawnedAt);
			MeteorArmorStand meteorArmorStand = new MeteorArmorStand(crashedMeteor, locationToSpawn, armorStand, angle_C);
			OreMeteor.getInstance().getArmorStandHashMap().put(UUID.randomUUID(), meteorArmorStand);

			chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.SPAWN_MESSAGE), MessageType.SPAWN_MESSAGE, meteorArmorStand);
		}
	}
}
