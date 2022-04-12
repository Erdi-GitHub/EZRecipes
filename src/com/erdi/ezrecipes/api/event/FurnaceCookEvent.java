package com.erdi.ezrecipes.api.event;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Called when a furnace cooks an item
 * @author Erdi__
 */
public final class FurnaceCookEvent implements RecipeEvent {
	private Block furnace;
	private ItemStack source;
	private ItemStack result;
	
	public FurnaceCookEvent(Block furnace, ItemStack source, ItemStack result) {
		this.furnace = furnace;
		this.source = source;
		this.result = result;
	}
	
	/**
	 * Returns the furnace which caused this event
	 * @return the furnace which caused this event
	 */
	public Block getFurnace() {
		return furnace;
	}
	
	/**
	 * Gets the smelted ItemStack for this event
	 * @return the smelted ItemStack
	 */
	public ItemStack getSource() {
		return source;
	}
	
	/**
	 * Gets the resultant ItemStack for this event
	 * @return the resultant ItemStack
	 */
	public ItemStack getResult() {
		return result;
	}
}
