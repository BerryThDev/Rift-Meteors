package me.berry.oreMeteor.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.UUID;

public class ArmorstandUtil {
 	public int spawnStand(Location spawnLocation, Location endLocation, Material headMat) {
		float angle_C = headVal(spawnLocation, endLocation);

		// Refer to wiki.vg to see exactly how the packets work and what they require
		// Generates the random id of the stand
		int id = (int)(Math.random() * Integer.MAX_VALUE);

		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

		// Sets up the first packet that spawns the stand in the center of the broke block
		PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

		// Sets the values of the stand
		packet.getModifier()
				.write(0, id)
				.write(1, UUID.randomUUID())
				.write(2, 30)
				.write(3, spawnLocation.getX())
				.write(4, spawnLocation.getY())
				.write(5, spawnLocation.getZ());

		// Sends the packet
		protocolManager.broadcastServerPacket(packet);

		// Sets up the second packet
		PacketContainer packet2 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
		packet2.getModifier().writeDefaults();
		packet2.getIntegers().write(0, id);
		WrappedDataWatcher dataWatcher = new WrappedDataWatcher(packet2.getWatchableCollectionModifier().read(0));

		// Modifies the invisible modifier
		WrappedDataWatcher.WrappedDataWatcherObject isInvisibleIndex = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
		dataWatcher.setObject(isInvisibleIndex, (byte) 0x20);

		// Sets up the angles for the head
		EulerAngle eulerAngle = new EulerAngle(Math.toRadians(0), Math.toRadians(0), angle_C);
		Vector3F vector3f = new Vector3F((float) Math.toDegrees(eulerAngle.getX()), (float) Math.toDegrees(eulerAngle.getY()), (float) Math.toDegrees(eulerAngle.getZ()));

		// Sets the values that were created above
		WrappedDataWatcher.WrappedDataWatcherObject standHeadIndex = new WrappedDataWatcher.WrappedDataWatcherObject(12, WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass()));
		dataWatcher.setObject(standHeadIndex, vector3f);

		packet2.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());

		// Sends the packet
		protocolManager.broadcastServerPacket(packet2);

		// Sets up the entity equipment packet
		PacketContainer packet3 = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
		// Sets up all the needed values
		packet3.getIntegers().write(0, id);
		packet3.getItemSlots().write(0, EnumWrappers.ItemSlot.HEAD);
		ItemStack head = new ItemStack(headMat);
		packet3.getItemModifier().write(0, head);

		// Sends the packet
		protocolManager.broadcastServerPacket(packet3);

		return id;
	}

	public float headVal(Location spawnLocation, Location endLocation) {
		int x1 = endLocation.getBlockX();
		int y1 = endLocation.getBlockY();
		int z1 = endLocation.getBlockZ();

		int x2 = spawnLocation.getBlockX();
		int y2 = endLocation.getBlockY();
		int z2 = spawnLocation.getBlockZ();

		int x3 = spawnLocation.getBlockX();
		int y3 = spawnLocation.getBlockY();
		int z3 = spawnLocation.getBlockZ();

		MathUtil mathUtil = new MathUtil();

		return mathUtil.angle_triangle(x3, x2, x1, y3, y2, y1, z3, z2, z1);
	}

	public static void remove(int id) {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		int[] entIdArray = new int[1];
		entIdArray[0] = id;

		PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
		packet.getIntegerArrays().write(0, entIdArray);

		protocolManager.broadcastServerPacket(packet);
	}
}
