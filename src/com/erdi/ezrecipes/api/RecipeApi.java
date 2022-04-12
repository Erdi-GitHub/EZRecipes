package com.erdi.ezrecipes.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.erdi.ezrecipes.api.event.RecipeEvent;
import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author Erdi__
 */
public final class RecipeApi {
	private HashMap<String, RecipeManager> managers = new HashMap<>();
	private RecipeEventHandler handler = new RecipeEventHandler();
	
	public RecipeManager getRecipeManager(JavaPlugin plugin) {
		String pluginName = plugin.getName();
		
		if(managers.containsKey(pluginName))
			return managers.get(pluginName);
		
		RecipeManager manager = new RecipeManager(plugin);
		
		managers.put(pluginName, manager);
		
		return manager;
	}
	
	/**
	 * Get the recipes registered by the given plugin
	 */
	public HashMap<String, Recipe> getRecipes(JavaPlugin plugin) {
		return getRecipeManager(plugin).getRecipes();
	}
	
	/**
	 * Listen to a specific event
	 * @param <T> The type of the event
	 * @param type The event class (e.g. FurnaceCookEvent.class)
	 * @param consumer The listener
	 */
	public <T extends RecipeEvent> void registerListener(Class<T> type, Consumer<T> consumer) {
		handler.listen(type, consumer);
	}
	
	/**
	 * Dispatches an event. This method should be ignored by plugins.
	 * 
	 * @param <T> The type of the event
	 * @param type The event class
	 * @param event The event itself
	 */
	public <T extends RecipeEvent> void dispatchEvent(Class<T> type, T event) {
		handler.dispatch(type, event);
	}

	/**
	 * Generate a map of ingredients
	 * @return a map of ingredients
	 */
	public Map<Character, Material> mapIngredients(
			char k1, Material v1
	) {
		return ImmutableMap.of(k1, v1);
	}

	/**
	 * Generate a map of ingredients
	 * @return a map of ingredients
	 */
	public Map<Character, Material> mapIngredients(
			char k1, Material v1,
			char k2, Material v2
	) {
		return ImmutableMap.of(k1, v1, k2, v2);
	}

	/**
	 * Generate a map of ingredients
	 * @return a map of ingredients
	 */
	public Map<Character, Material> mapIngredients(
			char k1, Material v1,
			char k2, Material v2,
			char k3, Material v3
	) {
		return ImmutableMap.of(k1, v1, k2, v2, k3, v3);
	}

	/**
	 * Generate a map of ingredients
	 * @return a map of ingredients
	 */
	public Map<Character, Material> mapIngredients(
			char k1, Material v1,
			char k2, Material v2,
			char k3, Material v3,
			char k4, Material v4
	) {
		return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
	}

	/**
	 * Generate a map of ingredients
	 * @return a map of ingredients
	 */
	public Map<Character, Material> mapIngredients(
			char k1, Material v1,
			char k2, Material v2,
			char k3, Material v3,
			char k4, Material v4,
			char k5, Material v5
	) {
		return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
	}

	/**
	 * Generate a map of ingredients <br>
	 * The arguments passed to this method should be in char-Material pairs:
	 * 
	 * <pre><code>
	 * 	mapIngredients(
	 *		'a', Material.APPLE,
	 *		'b', Material.DIAMOND,
	 *		'c', Material.BAKED_POTATO,
	 *		'd', Material.ANVIL,
	 *		'e', Material.BEACON,
	 *		'f', Material.BED
	 * 	);
	 * </code></pre>
	 * 
	 * @see #mapIngredients(char, Material, char, Material)
	 */
	public Map<Character, Material> mapIngredients(Object ...args) {
		ImmutableMap.Builder<Character, Material> builder = ImmutableMap.builder();
		
		for(int i = 0; i < args.length; i++) {
			if((i + 1) % 2 == 0)
				builder.put((Character) args[i - 1], (Material) args[i]);
		}
		
		return builder.build();
	}
}
