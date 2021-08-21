package me.berry.oreMeteor.utils;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.berry.oreMeteor.classes.meteor.Meteor;
import org.bukkit.*;

public class WorldGuardUtil {
	private boolean canSpawn = false;

	public Location spawnMeteorAt(Meteor meteorConfigClass) {
		World world = Bukkit.getServer().getWorld(meteorConfigClass.getWorld());
		WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
		RegionContainer container = worldGuardPlugin.getRegionContainer();
		RegionManager regions = container.get(world);

		if (regions != null) {
			MathUtil mathUtil = new MathUtil();
			ProtectedRegion region = regions.getRegion(meteorConfigClass.getWgRegion());

			assert region != null;

			try {
				int pointAX = (int) region.getMaximumPoint().getX();
				int pointAZ = (int) region.getMaximumPoint().getZ();

				int pointBX = (int) region.getMinimumPoint().getX();
				int pointBY = (int) region.getMinimumPoint().getY();
				int pointBZ = (int) region.getMinimumPoint().getZ();

				int randX = mathUtil.randBetween(pointBX, pointAX);
				int randZ = mathUtil.randBetween(pointBZ, pointAZ);

				int pointY = safeY(new Location(world, randX, pointBY, randZ));

				if(pointY == -1) {
					System.out.println("Error finding proper space for meteor spawning at min point");

					return null;
				}

				return new Location(world, randX, pointY, randZ);
			} catch (NullPointerException e) {
				System.out.println(ChatColor.RED + "Could not find a world guard region for meteor: " + meteorConfigClass.getConfigName());
				return null;
			}


			/*if(canSpawnOnGround(new Location(world, randX, pointBY, randZ))) {
				int pointY = .getBlockY();

				return new Location(world, randX, pointY, randZ);
			} else {
				System.out.println(ChatColor.RED + "Couldnt spawn meteor.");
				return null;
			}*/
		}

		return new Location(world, 0, 0, 0);
	}

	private int safeY(Location point) {
		Location tempPoint = point.clone();
		int i = 0;

		while (!canSpawn) {
			if(i == 255) break;

			if(i != 0) tempPoint.add(0, 1, 0);

			if (canSpawnOnGround(tempPoint)) {
				canSpawn = true;
				break;
			}
			i++;
		}

		if(canSpawn) return tempPoint.getBlockY();
		else return -1;
	}

	private boolean canSpawnOnGround(Location point) {
		if(point.getBlock().getType() == Material.AIR)
			if (point.clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR)
				if (point.clone().add(0, 1, 0).getBlock().getType() == Material.AIR) return true;
				else return false;
			else return false;
		else return false;
	}
}
