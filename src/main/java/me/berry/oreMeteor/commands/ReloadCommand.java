package me.berry.oreMeteor.commands;

import me.berry.oreMeteor.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		Player player = (Player) commandSender;

		if(player.hasPermission("meteors.admin")) {
			player.sendMessage(ChatColor.GREEN + "Reloaded config.");
			new ConfigUtil().initMeteors();
		}
		else player.sendMessage(ChatColor.RED + "No permission.");

		return true;
	}
}
