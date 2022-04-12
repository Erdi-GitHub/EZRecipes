package com.erdi.ezrecipes.api.event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Called when a player takes an item out of a furnace.
 * @author Erdi__
 */
public final class FurnaceItemCollectEvent implements RecipeEvent {
	private Player player;
	private Block furnace;
	private Material type;
	private int amount;
	
	public FurnaceItemCollectEvent(Player player, Block furnace, Material type, int amount) {
		this.player = player;
		this.furnace = furnace;
		this.type = type;
		this.amount = amount;
	}
	
	/**
	 * Gets the player who collected the item
	 * @return the player who collected the item
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the furnace associated with this event
	 * @return the furnace associated with this event
	 */
	public Block getFurnace() {
		return furnace;
	}
	
	/**
	 * Gets the Material of the collected item
	 * @return the material of the collected item
	 */
	public Material getType() {
		return type;
	}
	
	/**
	 * Gets the item count being collected
	 * @return the item count being collected
	 */
	public int getAmount() {
		return amount;
	}
}
