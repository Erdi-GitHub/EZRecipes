package com.erdi.ezrecipes.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import com.erdi.ezrecipes.api.RecipeApi;
import com.erdi.ezrecipes.api.event.FurnaceItemCollectEvent;
import com.erdi.ezrecipes.plugin.EZRecipes;

public class FurnaceExtractListener implements Listener {
	private RecipeApi api;
	
	public FurnaceExtractListener(EZRecipes instance) {
		api = instance.recipeApi();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFurnaceExtract(FurnaceExtractEvent event) {
		api.dispatchEvent(FurnaceItemCollectEvent.class, new FurnaceItemCollectEvent(event.getPlayer(), event.getBlock(), event.getItemType(), event.getItemAmount()));
	}
}
