package com.erdi.ezrecipes.api.event;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * Called when a player ({@link HumanEntity}) crafts an item
 * @author Erdi__
 */
public final class ItemCraftEvent implements RecipeEvent {
	private HumanEntity crafter;
	private ItemStack result;
	private Recipe recipe;
	
	public ItemCraftEvent(HumanEntity crafter, ItemStack result, Recipe recipe) {
		this.crafter = crafter;
		this.result = result;
		this.recipe = recipe;
	}
	
	/**
	 * Returns the person who crafted
	 * @return the person who crafted
	 */
	public HumanEntity getCrafter() {
		return crafter;
	}
	
	/**
	 * Gets the result of the crafting event
	 * @return the result of the crafting event
	 */
	public ItemStack getResult() {
		return result;
	}
	
	/**
	 * Returns a copy of the current recipe on the crafting matrix
	 * @return a copy of the current recipe on the crafting matrix.
	 */
	public Recipe getRecipe() {
		return recipe;
	}
}
