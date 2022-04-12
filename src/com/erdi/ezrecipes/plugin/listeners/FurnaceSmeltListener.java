package com.erdi.ezrecipes.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import com.erdi.ezrecipes.api.RecipeApi;
import com.erdi.ezrecipes.api.event.FurnaceCookEvent;
import com.erdi.ezrecipes.plugin.EZRecipes;

/**
 * @author Erdi__
 */
public class FurnaceSmeltListener implements Listener {
	private RecipeApi api;
	
	public FurnaceSmeltListener(EZRecipes instance) {
		api = instance.recipeApi();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFurnaceSmelt(FurnaceSmeltEvent event) {
		if(event.isCancelled())
			return;
		
		api.dispatchEvent(FurnaceCookEvent.class, new FurnaceCookEvent(event.getBlock(), event.getSource(), event.getResult()));
	}
}
