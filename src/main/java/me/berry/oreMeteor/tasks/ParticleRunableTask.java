package me.berry.oreMeteor.tasks;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleRunableTask extends BukkitRunnable {
	public ParticleRunableTask() {}

	@Override
	public void run() {
		HashMap<UUID, CrashedMeteor> meteors = OreMeteor.getInstance().getCrashedMeteorHashMap();

		if(meteors.size() == 0) return;

		for(Map.Entry<UUID, CrashedMeteor> entry : meteors.entrySet()) {
			CrashedMeteor crashedMeteor = entry.getValue();
			Location loc = crashedMeteor.getLocation().add(0.5, 0, 0.5);

			loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 0);
			loc.getWorld().spawnParticle(Particle.LAVA, loc, 0);

			loc.getWorld().playSound(loc, Sound.BLOCK_FURNACE_FIRE_CRACKLE, 1F, 1F);
		}
	}
}
