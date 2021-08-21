package me.berry.oreMeteor.utils;

import me.berry.oreMeteor.classes.meteor.CrashedMeteor;
import me.berry.oreMeteor.classes.meteor.CrashedMeteorWithItemStacks;
import me.berry.oreMeteor.classes.meteor.MeteorArmorStand;
import me.berry.oreMeteor.classes.utility.MessageType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ChatUtil {
	public void sendChatFromConfig(String rawMessage, Player player, MessageType messageType, Object object) {
		String withColors = replaceColorCodes(rawMessage);
		String withPlaceholders = replacePlaceholders(withColors, messageType, object);

		ArrayList<String> finalMessage = prepareForSend(withPlaceholders);

		for(String message : finalMessage) player.sendMessage(message);
	}

	public void broadcastToServerFromConfig(String rawMessage, MessageType messageType, Object object) {
		String withColors = replaceColorCodes(rawMessage);
		String withPlaceholders = replacePlaceholders(withColors, messageType, object);

		ArrayList<String> finalMessage = prepareForSend(withPlaceholders);

		for(String message : finalMessage) Bukkit.getServer().broadcastMessage(message);
	}

	private String replaceColorCodes(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	private String replacePlaceholders(String message, MessageType messageType, Object data) {

		switch (messageType) {
			case EXPIRE_MESSAGE:
				CrashedMeteor castedDataExpire = (CrashedMeteor) data;
				return message.replace("==meteor_name==", castedDataExpire.getOfficialName());
			case SPAWN_MESSAGE:
				MeteorArmorStand castedDataSpawn = (MeteorArmorStand) data;
				return message.replace("==meteor_name==", castedDataSpawn.getOfficialName())
						.replace("==x==", String.valueOf(castedDataSpawn.getStandLocation().getBlockX()))
						.replace("==y==", String.valueOf(castedDataSpawn.getStandLocation().getBlockY()))
						.replace("==z==", String.valueOf(castedDataSpawn.getStandLocation().getBlockZ()));
			case HIT_MESSAGE:
				CrashedMeteor castedDataHit = (CrashedMeteor) data;

				return message.replace("==meteor_name==", castedDataHit.getOfficialName())
						.replace("==x==", String.valueOf(castedDataHit.getLocation().getBlockX()))
						.replace("==y==", String.valueOf(castedDataHit.getLocation().getBlockY()))
						.replace("==z==", String.valueOf(castedDataHit.getLocation().getBlockZ()));
			case REWARD_MESSAGE:
				CrashedMeteorWithItemStacks castedDataReward = (CrashedMeteorWithItemStacks) data;

				return message.replace("==meteor_name==", castedDataReward.getOfficialName())
						.replace("==item_reward==", enumToPrettyPrint(castedDataReward.getRewardForSwing().getType().name()))
						.replace("==item_amount==", String.valueOf(castedDataReward.getRewardForSwing().getAmount()))
						.replace("==meteor_current_durability==", String.valueOf(castedDataReward.getHits()))
						.replace("==meteor_overall_durability==", String.valueOf(castedDataReward.getMaxHits()));
			case CLAIMED_MESSAGE:
				CrashedMeteor castedDataClaimed = (CrashedMeteor) data;

				return message.replace("==meteor_name==", castedDataClaimed.getOfficialName())
						.replace("==meteor_harvester==", Bukkit.getPlayer(castedDataClaimed.getLastHitPlayerUUID()).getName());
			default:
				return message;
		}
	}

	private ArrayList<String> prepareForSend(String message) {
		ArrayList<String> lines = new ArrayList<>();
		String[] lineBreakSections = message.split(Pattern.quote("\n"));

		Collections.addAll(lines, lineBreakSections);

		return lines;
	}

	private String enumToPrettyPrint(String enumWords) {
		if(enumWords == null) return "";

		StringBuilder finalString = new StringBuilder();
		String[] words = enumWords.split("_");

		for(String word : words)
			if (word.length() != 0) {
				finalString.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
				finalString.append(" ");
			}

		return finalString.toString().trim();
	}
}
