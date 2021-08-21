package me.berry.oreMeteor.tasks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteorWithItemStacks;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.utility.MessageType;
import me.berry.oreMeteor.utils.ArmorstandUtil;
import me.berry.oreMeteor.utils.ChatUtil;
import me.berry.oreMeteor.utils.ConfigUtil;
import me.berry.oreMeteor.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MoveMeteorTask extends BukkitRunnable {
	public MoveMeteorTask() {}

	@Override
	public void run() {
		OreMeteor oreMeteor = OreMeteor.getInstance();
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

		HashMap<UUID, MeteorArmorStand> stands = oreMeteor.getArmorStandHashMap();
		if(stands.size() == 0) return;

		Iterator<Map.Entry<UUID, MeteorArmorStand>> armorStandIt = stands.entrySet().iterator();

		while(armorStandIt.hasNext()) {
			MeteorArmorStand armorStand = armorStandIt.next().getValue();

			if (armorStand.getMax() <= armorStand.getProgress()) {
				ConfigUtil configUtil = new ConfigUtil();
				ChatUtil chatUtil = new ChatUtil();

				armorStandIt.remove();
				ArmorstandUtil.remove(armorStand.getEntId());

				Map<Integer, ItemStack> rewards = configUtil.getRewards(armorStand.getConfigName());

				CrashedMeteorWithItemStacks crashedMeteorWithItemStacks = new CrashedMeteorWithItemStacks(armorStand, rewards, Material.getMaterial(configUtil.getOre(armorStand.getConfigName())));
				crashedMeteorWithItemStacks.create();
				OreMeteor.getInstance().getCrashedMeteorHashMap().put(UUID.randomUUID(), armorStand);

				chatUtil.broadcastToServerFromConfig(configUtil.getMessage(MessageType.HIT_MESSAGE), MessageType.HIT_MESSAGE, armorStand);

				continue;
			}

			double y = armorStand.getStandLocation().getY();
			double x = armorStand.getStandLocation().getX();
			double z = armorStand.getStandLocation().getZ();

			Location toTp;

			toTp = new Location(armorStand.getStandLocation().getWorld(), MathUtil.correction(armorStand.getStandLocation().getX(), armorStand.getLocation().getX()), MathUtil.correction(armorStand.getStandLocation().getY(), armorStand.getLocation().getY()), armorStand.getStandLocation().getZ());

			armorStand.setStandLocation(toTp);

			PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);

			packet.getModifier()
					.write(0, armorStand.getEntId())
					.write(1, x)
					.write(2, y)
					.write(3, z);

			protocolManager.broadcastServerPacket(packet);

			armorStand.updateProgress();
		}
	}
}
