package me.berry.oreMeteor.tasks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.utility.FloatVec;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RotateMeteorTask extends BukkitRunnable {
	public RotateMeteorTask() {}

	@Override
	public void run() {
		OreMeteor oreMeteor = OreMeteor.getInstance();
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

		HashMap<UUID, MeteorArmorStand> stands = oreMeteor.getArmorStandHashMap();
		if(stands.size() == 0) return;

		for(Map.Entry<UUID, MeteorArmorStand> entry : stands.entrySet()) {
			MeteorArmorStand armorStand = entry.getValue();

			float x = armorStand.getHeadLoc().getX();
			float y = armorStand.getHeadLoc().getY() + 12F;
			float z = armorStand.getHeadLoc().getZ();

			PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);

			packet.getModifier().writeDefaults();
			packet.getIntegers().write(0, armorStand.getEntId());

			WrappedDataWatcher dataWatcher = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));

			EulerAngle eulerAngle = new EulerAngle(Math.toRadians(x), y, Math.toRadians(z));

			Vector3F vector3f = new Vector3F((float) Math.toDegrees(eulerAngle.getX()), (float) eulerAngle.getY(), (float) Math.toDegrees(eulerAngle.getZ()));

			WrappedDataWatcher.WrappedDataWatcherObject standHeadIndex = new WrappedDataWatcher.WrappedDataWatcherObject(12, WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass()));
			dataWatcher.setObject(standHeadIndex, vector3f);

			packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

			protocolManager.broadcastServerPacket(packet);

			armorStand.setHeadLoc(new FloatVec(x, y, z));
		}
	}
}
