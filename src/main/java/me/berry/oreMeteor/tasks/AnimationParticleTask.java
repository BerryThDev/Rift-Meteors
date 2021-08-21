package me.berry.oreMeteor.tasks;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimationParticleTask extends BukkitRunnable {
	public AnimationParticleTask() {}

	@Override
	public void run() {
		OreMeteor oreMeteor = OreMeteor.getInstance();

		HashMap<UUID, MeteorArmorStand> stands = oreMeteor.getArmorStandHashMap();
		if(stands.size() == 0) return;

		for (Map.Entry<UUID, MeteorArmorStand> entry : stands.entrySet()) {
			MeteorArmorStand meteorArmorStand = entry.getValue();
			Location loc = meteorArmorStand.getStandLocation();

			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(1, 2.2, 0), 0);
			loc.getWorld().spawnParticle(Particle.LAVA, loc.clone().add(1, 2.2, 0), 0);
		}
	}
}
