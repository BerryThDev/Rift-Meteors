package me.berry.oreMeteor.classes.meteor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CrashedMeteorWithItemStacks extends CrashedMeteor {
	private static final long serialVersionUID = -4061439070808408522L;
	private final Map<Integer, ItemStack> rewardsForSwing;
	private final Material ore;

	public CrashedMeteorWithItemStacks(CrashedMeteor crashedMeteor, Map<Integer, ItemStack> rewardsForSwing, Material ore) {
		super(crashedMeteor.getConfigName(), crashedMeteor.getOfficialName(), crashedMeteor.getLocation(), crashedMeteor.getPreviousBlock(), rewardsForSwing.size(), crashedMeteor.getHits(), crashedMeteor.getExpireAfter());
		this.rewardsForSwing = rewardsForSwing;
		this.ore = ore;
	}

	public ItemStack getRewardForSwing() {
		ItemStack rewards = rewardsForSwing.get(super.getHits());
		return rewards;
	}

	public void create() {
		this.getLocation().getBlock().setType(this.ore);
	}
}
