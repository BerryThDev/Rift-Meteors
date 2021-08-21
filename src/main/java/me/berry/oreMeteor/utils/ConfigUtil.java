package me.berry.oreMeteor.utils;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.Meteor;
import me.berry.oreMeteor.classes.meteor.MeteorTask;
import me.berry.oreMeteor.classes.utility.MessageType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
	OreMeteor meteorPlugin = OreMeteor.getInstance();

	public void init() {
		meteorPlugin.saveDefaultConfig();
	}

	public void initMeteors() {
		File configFile = new File(OreMeteor.getInstance().getDataFolder(), "config.yml");
		FileConfiguration mainConfig = YamlConfiguration.loadConfiguration(configFile);

		for(String meteor : mainConfig.getConfigurationSection("Meteors").getKeys(false)) {
			Meteor meteorClass = getMeteorByConfigName(meteor);
			MeteorTask meteorTask = new MeteorTask(meteorClass);

			meteorPlugin.getMeteorTaskHashMap().clear();
			meteorPlugin.getMeteorTaskHashMap().put(meteorClass.getName(), meteorTask);
		}
	}

	public Map<Integer, ItemStack> getRewards(String meteor) {
		File configFile = new File(OreMeteor.getInstance().getDataFolder(), "config.yml");
		FileConfiguration settings = YamlConfiguration.loadConfiguration(configFile);

		Map<Integer, ItemStack> rewardMap = new HashMap<>();

		for(String reward : settings.getConfigurationSection("Meteors." + meteor + ".rewards").getKeys(false)) {
			Integer swing = Integer.parseInt(reward);

			ItemStack rewardItem = new ItemStack(Material.getMaterial(settings.getString("Meteors." + meteor + ".rewards." + reward + ".item")));
			int amount = settings.getInt("Meteors." + meteor + ".rewards." + reward + ".amount");
			rewardItem.setAmount(amount);

			rewardMap.put(swing, rewardItem);
		}

		return rewardMap;
	}

	public String getOre(String meteor) {
		File configFile = new File(OreMeteor.getInstance().getDataFolder(), "config.yml");
		FileConfiguration settings = YamlConfiguration.loadConfiguration(configFile);

		return settings.getString("Meteors." + meteor + ".ore");
	}

	public String getMessage(MessageType messageType) {
		File configFile = new File(OreMeteor.getInstance().getDataFolder(), "config.yml");
		FileConfiguration settings = YamlConfiguration.loadConfiguration(configFile);

		String message;

		switch (messageType) {
			case EXPIRE_MESSAGE:
				message = settings.getString("Messages.expire-message");
				break;
			case HIT_MESSAGE:
				message = settings.getString("Messages.hit-message");
				break;
			case REWARD_MESSAGE:
				message = settings.getString("Messages.reward-message");
				break;
			case CLAIMED_MESSAGE:
				message = settings.getString("Messages.claimed-message");
				break;
			case SPAWN_MESSAGE:
				message = settings.getString("Messages.spawn-message");
				break;
			default:
				message = "";
		}

		return message;
	}

	public Meteor getMeteorByConfigName(String meteor) {
		File configFile = new File(OreMeteor.getInstance().getDataFolder(), "config.yml");
		FileConfiguration mainConfig = YamlConfiguration.loadConfiguration(configFile);

		String name = mainConfig.getString("Meteors." + meteor + ".name");
		String world = mainConfig.getString("Meteors." + meteor + ".world");
		String wgName = mainConfig.getString("Meteors." + meteor + ".world-guard-region");
		String spawnRateMin = mainConfig.getString("Meteors." + meteor + ".delay-min");
		String spawnRateMax = mainConfig.getString("Meteors." + meteor + ".delay-max");
		String expireAfter = mainConfig.getString("Meteors." + meteor + ".expire-after");
		String itemString = mainConfig.getString("Meteors." + meteor + ".ore");

		if(name == null || world == null || wgName == null || spawnRateMax == null || spawnRateMin == null || expireAfter == null || itemString == null)
			return null;

		Material ore = Material.getMaterial(itemString);

		return new Meteor(meteor, name, world, wgName, ore, expireAfter, spawnRateMax, spawnRateMin);
	}
}
