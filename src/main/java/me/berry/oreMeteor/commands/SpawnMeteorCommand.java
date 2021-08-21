package me.berry.oreMeteor.commands;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.Meteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.utility.MessageType;
import me.berry.oreMeteor.utils.ArmorstandUtil;
import me.berry.oreMeteor.utils.ChatUtil;
import me.berry.oreMeteor.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class SpawnMeteorCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if(!(commandSender instanceof Player)) return true;

		Player player = (Player) commandSender;
		boolean canSpawn = false;

		if(player.hasPermission("meteor.admin")) if (args.length == 1) {
			Location playerLocation = player.getLocation();
			Location rounded = new Location(playerLocation.getWorld(), playerLocation.getBlockX(), playerLocation.getBlockY(), playerLocation.getBlockZ());

			while (!canSpawn) if (rounded.getBlock().getType() == Material.AIR) {
				if (rounded.getBlockY() <= 0) break;

				rounded.subtract(0, 1, 0);
			} else {
				canSpawn = true;
				rounded.add(0, 1, 0);
				break;
			}

			if (!canSpawn)
				player.sendMessage(ChatColor.RED + "Could not find a valid block to spawn meteor.");
			else {
				ConfigUtil configUtil = new ConfigUtil();
				Meteor meteor = configUtil.getMeteorByConfigName(args[0]);

				if (meteor == null) {
					player.sendMessage(ChatColor.RED + "Could not find meteor.");
					return true;
				}

				spawn(rounded, meteor);

				player.sendMessage(ChatColor.GREEN + "Spawned meteor at your location.");

				return true;
			}
		} else player.sendMessage(ChatColor.RED + "Please include the meteor's name.");
		else player.sendMessage(ChatColor.RED + "No permission.");

		return true;
	}

	private void spawn(Location spawnedAt, Meteor meteorClass) {
		ConfigUtil configUtil = new ConfigUtil();
		ArmorstandUtil armorstandUtil = new ArmorstandUtil();
		ChatUtil chatUtil = new ChatUtil();

		Map<Integer, ItemStack> rewards = configUtil.getRewards(meteorClass.getConfigName());

		CrashedMeteor crashedMeteor = new CrashedMeteor(meteorClass.getConfigName(), meteorClass.getName(), spawnedAt, spawnedAt.getBlock().getType(), rewards.size() + 1, 1, meteorClass.getExpiresAfter());
		Location locationToSpawn = spawnedAt.clone().add(20, 19, 0.5);
		int armorStand = armorstandUtil.spawnStand(locationToSpawn, spawnedAt, Material.getMaterial(configUtil.getOre(crashedMeteor.getConfigName())));
		float angle_C  = armorstandUtil.headVal(locationToSpawn, spawnedAt);
		MeteorArmorStand meteorArmorStand = new MeteorArmorStand(crashedMeteor, locationToSpawn, armorStand, angle_C);
		OreMeteor.getInstance().getArmorStandHashMap().put(UUID.randomUUID(), meteorArmorStand);

		chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.SPAWN_MESSAGE), MessageType.SPAWN_MESSAGE, meteorArmorStand);
	}
}
