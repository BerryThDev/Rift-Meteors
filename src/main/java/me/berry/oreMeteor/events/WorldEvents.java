package me.berry.oreMeteor.events;

import me.berry.oreMeteor.OreMeteor;
import me.berry.oreMeteor.utils.DataUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class WorldEvents implements Listener {
	@EventHandler
	public void onWorldSave(WorldSaveEvent e) {
		if(!OreMeteor.saved) {
			File crashedFile = new File(OreMeteor.getInstance().getDataFolder(), "crashedMeteors.dat");
			DataUtil dataUtil = new DataUtil();

			CompletableFuture<Boolean> completableFuture = CompletableFuture
					.supplyAsync(() -> dataUtil.saveDataFunction(crashedFile, OreMeteor.getInstance().getCrashedMeteorHashMap()));

			completableFuture.thenAcceptAsync((res) -> {
				DataUtil.writing.getAndSet(false);
				OreMeteor.saved = true;
			});

		}
	}
}
