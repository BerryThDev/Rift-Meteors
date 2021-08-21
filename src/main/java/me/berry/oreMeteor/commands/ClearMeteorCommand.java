package me.berry.oreMeteor.commands;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.utils.ArmorstandUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ClearMeteorCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		Player player = (Player) commandSender;

 		if(player.hasPermission("meteors.admin")) {
			OreMeteor oreMeteor = OreMeteor.getInstance();
			HashMap<UUID, MeteorArmorStand> meteorArmorStandHashMap = oreMeteor.getArmorStandHashMap();
			HashMap<UUID, CrashedMeteor> crashedMeteorHashMap = oreMeteor.getCrashedMeteorHashMap();

			if(meteorArmorStandHashMap.size() == 0 && crashedMeteorHashMap.size() == 0) {
				player.sendMessage(ChatColor.RED + "No meteors to clear.");
				return true;
			}

			Iterator<Map.Entry<UUID, MeteorArmorStand>> armorStandIt = meteorArmorStandHashMap.entrySet().iterator();

			while(armorStandIt.hasNext()) {
				MeteorArmorStand meteorArmorStand = armorStandIt.next().getValue();

				ArmorstandUtil.remove(meteorArmorStand.getEntId());
				armorStandIt.remove();
			}

			Iterator<Map.Entry<UUID, CrashedMeteor>> crashedMeteorIt = crashedMeteorHashMap.entrySet().iterator();

			while(crashedMeteorIt.hasNext()) {
				CrashedMeteor crashedMeteor = crashedMeteorIt.next().getValue();

				crashedMeteor.remove();
				crashedMeteor.getLocation().getBlock().setType(Material.AIR);
				crashedMeteorIt.remove();
			}

			player.sendMessage(ChatColor.GREEN + "All crashed meteors and falling meteors have been cleared.");
		} else player.sendMessage(ChatColor.RED + "No permission.");
		return true;
	}
}
