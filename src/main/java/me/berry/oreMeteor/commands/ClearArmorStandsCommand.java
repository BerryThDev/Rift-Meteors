package me.berry.oreMeteor.commands;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.utils.ArmorstandUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ClearArmorStandsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		Player player = (Player) commandSender;

		if(player.hasPermission("meteors.admin")) {
			OreMeteor oreMeteor = OreMeteor.getInstance();
			HashMap<UUID, MeteorArmorStand> meteorArmorStandHashMap = oreMeteor.getArmorStandHashMap();

			if(meteorArmorStandHashMap.size() == 0) {
				player.sendMessage(ChatColor.RED + "No armor stands found.");
				return true;
			}

			Iterator<Map.Entry<UUID, MeteorArmorStand>> standIt = meteorArmorStandHashMap.entrySet().iterator();

			while(standIt.hasNext()) {
				MeteorArmorStand meteorArmorStand = standIt.next().getValue();

				ArmorstandUtil.remove(meteorArmorStand.getEntId());
				standIt.remove();
			}

			player.sendMessage(ChatColor.GREEN + "Removed all armor stands.");

		} else player.sendMessage(ChatColor.RED + "No permission.");

		return true;
	}
}
