package com.erdi.ezrecipes.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import com.erdi.ezrecipes.api.RecipeApi;
import com.erdi.ezrecipes.api.event.ItemCraftEvent;
import com.erdi.ezrecipes.plugin.EZRecipes;

/**
 * @author Erdi__
 */
public class CraftItemListener implements Listener {
	private RecipeApi api;
	
	public CraftItemListener(EZRecipes instance) {
		api = instance.recipeApi();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraftItem(CraftItemEvent event) {
		if(event.isCancelled())
			return;

		api.dispatchEvent(ItemCraftEvent.class, new ItemCraftEvent(event.getWhoClicked(), event.getCurrentItem(), event.getRecipe()));
	}
}
