package me.berry.oreMeteor;

import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.meteor.MeteorTask;
import me.berry.oreMeteor.commands.ClearMeteorCommand;
import me.berry.oreMeteor.commands.ReloadCommand;
import me.berry.oreMeteor.commands.SpawnMeteorCommand;
import me.berry.oreMeteor.events.PlayerEvents;
import me.berry.oreMeteor.events.WorldEvents;
import me.berry.oreMeteor.tasks.*;
import me.berry.oreMeteor.utils.ConfigUtil;
import me.berry.oreMeteor.utils.DataUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OreMeteor extends JavaPlugin {
	private static OreMeteor instance;
	public static boolean saved = false;
	private HashMap<UUID, CrashedMeteor> crashedMeteorHashMap;
	private HashMap<String, MeteorTask> meteorTaskHashMap;
	private HashMap<UUID, MeteorArmorStand> armorStandHashMap;

	@Override
	public void onEnable() {
		instance = this;

		meteorTaskHashMap = new HashMap<>();
		armorStandHashMap = new HashMap<>();

		ConfigUtil configUtil = new ConfigUtil();
		configUtil.init();

		if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();

		File crashedFile = new File(this.getDataFolder(), "crashedMeteors.dat");

		DataUtil dataUtil = new DataUtil();
		crashedMeteorHashMap = (HashMap<UUID, CrashedMeteor>) dataUtil.initVars(crashedFile);

		configUtil.initMeteors();

		MeteorRunableTask meteorRunableTask = new MeteorRunableTask();
		meteorRunableTask.runTaskTimer(this, 0L, 20L);
		ParticleRunableTask particleRunableTask = new ParticleRunableTask();
		particleRunableTask.runTaskTimer(this, 0L, 0L);
		RotateMeteorTask rotateMeteorTask = new RotateMeteorTask();
		rotateMeteorTask.runTaskTimer(this, 0L, 0L);
		MoveMeteorTask moveMeteorTask = new MoveMeteorTask();
		moveMeteorTask.runTaskTimer(this, 3L, 3L);
		AnimationParticleTask animationParticleTask = new AnimationParticleTask();
		animationParticleTask.runTaskTimer(this, 20L, 0L);

		getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		getServer().getPluginManager().registerEvents(new WorldEvents(), this);

		this.getCommand("clearmeteors").setExecutor(new ClearMeteorCommand());
		this.getCommand("spawnmeteor").setExecutor(new SpawnMeteorCommand());
		this.getCommand("reloadmeteors").setExecutor(new ReloadCommand());
		this.getCommand("clearmeteorstands").setExecutor(new ReloadCommand());
	}

	@Override
	public void onDisable() {
		DataUtil dataUtil = new DataUtil();
		File crashedFile = new File(this.getDataFolder(), "crashedMeteors.dat");

		CompletableFuture<Boolean> completableFuture = CompletableFuture
				.supplyAsync(() -> dataUtil.saveDataFunction(crashedFile, crashedMeteorHashMap));

		completableFuture.thenAcceptAsync((res) -> {
			DataUtil.writing.getAndSet(false);
			saved = true;
		});
	}

	public static OreMeteor getInstance() {
		return instance;
	}

	public HashMap<UUID, MeteorArmorStand> getArmorStandHashMap() {
		return armorStandHashMap;
	}

	public HashMap<UUID, CrashedMeteor> getCrashedMeteorHashMap() {
		return crashedMeteorHashMap;
	}

	public HashMap<String, MeteorTask> getMeteorTaskHashMap() {
		return meteorTaskHashMap;
	}
}
