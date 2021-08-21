package me.berry.oreMeteor.events;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteorWithItemStacks;
import me.berry.oreMeteor.classes.utility.MessageType;
import me.berry.oreMeteor.utils.ChatUtil;
import me.berry.oreMeteor.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {
	@EventHandler()
	public void onInteractWithBlock(BlockBreakEvent e) {
		HashMap<UUID, CrashedMeteor> crashedMeteorHashMap = OreMeteor.getInstance().getCrashedMeteorHashMap();
		if (crashedMeteorHashMap.size() == 0) return;

		for (Map.Entry<UUID, CrashedMeteor> entry : crashedMeteorHashMap.entrySet()) {
			CrashedMeteor crashedMeteor = entry.getValue();
			Location location = crashedMeteor.getLocation();

			if (location.equals(e.getBlock().getLocation())) {
				Player player = e.getPlayer();

				if (!player.hasPermission("meteors.break." + crashedMeteor.getConfigName())) {
					e.setCancelled(true);
					player.sendMessage(ChatColor.RED + "No permission to break this meteor.");
				}

				ChatUtil chatUtil = new ChatUtil();
				ConfigUtil configUtil = new ConfigUtil();

				CrashedMeteorWithItemStacks crashedMeteorWithItemStacks = new CrashedMeteorWithItemStacks(crashedMeteor, configUtil.getRewards(crashedMeteor.getConfigName()), Material.getMaterial(configUtil.getOre(crashedMeteor.getConfigName())));

				crashedMeteor.setLastHitPlayerUUID(player.getUniqueId());

				ItemStack rewardItem = crashedMeteorWithItemStacks.getRewardForSwing();
				player.getInventory().addItem(rewardItem);

				chatUtil.sendChatFromConfig(configUtil.getMessage(MessageType.REWARD_MESSAGE), player, MessageType.REWARD_MESSAGE, crashedMeteorWithItemStacks);

				crashedMeteor.increaseHits();

				if (crashedMeteor.getHits() >= crashedMeteor.getMaxHits()) {
					chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.CLAIMED_MESSAGE), MessageType.CLAIMED_MESSAGE, crashedMeteor);

					crashedMeteor.remove();
					crashedMeteorHashMap.remove(entry.getKey());

					crashedMeteor.getLocation().getBlock().setType(crashedMeteor.getPreviousBlock());
				}

				e.setCancelled(true);
				break;
			}
		}
	}

}
