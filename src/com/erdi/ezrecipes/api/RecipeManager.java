package com.erdi.ezrecipes.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Used to register recipes. Also has utility methods, such as {@link #isCustomRecipe(Recipe)} and {@link #getCustomRecipeName(Recipe)}
 * @author Erdi__
 */
public final class RecipeManager {
	private JavaPlugin plugin;
	private HashMap<String, Recipe> recipes = new HashMap<>();
	
	RecipeManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Register a recipe
	 * @param name The name of the recipe. If null, a random name is generated
	 * @param shape The shape of the recipe. Should replicate a 3x3 grid
	 * @param ingredients The ingredients of the recipe
	 * @param result The result of this recipe
	 * @return true if the recipe was successfully added
	 */
	@SuppressWarnings("deprecation")
	public boolean registerRecipe(@Nonnull String name, @Nonnull String[] shape, @Nonnull Map<Character, Material> ingredients, @Nonnull ItemStack result) {
		if(name == null)
			name = getRandomName();

		if(shape == null)
			throw new IllegalArgumentException("shape paramater cannot be null");
		
		if(ingredients == null)
			throw new IllegalArgumentException("ingredients parameter cannot be null");
		
		if(result == null)
			throw new IllegalArgumentException("result parameter cannot be null");
		
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, name), result);
		
		recipe.shape(shape);
		System.out.println(Arrays.toString(shape));
		
		ingredients.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		});

		ingredients.forEach((key, ingredient) ->
			recipe.setIngredient(key, ingredient, Short.MAX_VALUE)
		); // while this uses a magic data value, it's the only way to set a wildcard data value
		
		recipes.put(name, recipe);
		
		return Bukkit.addRecipe(recipe);
	}
	
	/**
	 * Register a smelting recipe without specifying a cooking time or experiences
	 * @param name The name of the recipe. If null, a random name is generated
	 * @return true if the recipe was successfully added
	 */
	public boolean registerSmeltingRecipe(@Nonnull String name, @Nonnull Material source, @Nonnull ItemStack result) {
		if(name == null)
			name = getRandomName();
		
		FurnaceRecipe recipe = new FurnaceRecipe(result, source);
		recipes.put(name, recipe);
		
		return Bukkit.addRecipe(recipe);
	}
	
	/**
	 * Register a smelting recipe.<br>
	 * In case the server is a legacy server (1.13 or lower), cookingTime and/or experience will be ignored if it does not exist in Bukkit's {@link FurnaceRecipe} class
	 * @param name The name of the recipe. If null, a random name is generated
	 * @param source The item required to smelt the result
	 * @param result The resulting item
	 * @param cookingTime The time needed to smelt the source. Ignored in legacy versions (1.13 and lower)
	 * @param experience The experience given after smelting. Ignored in 1.8.x
	 * @return true if the recipe was successfully added
	 */
	public boolean registerSmeltingRecipe(@Nonnull String name, @Nonnull Material source, @Nonnull ItemStack result, int cookingTime, float experience) {
		FurnaceRecipe recipe = new FurnaceRecipe(result, source);
		recipes.put(name, recipe);
		
		try {
			FurnaceRecipe.class.getMethod("setExperience", Float.class).invoke(recipe, experience);
		} catch(Exception e) {}
		
		try {
			FurnaceRecipe.class.getMethod("setCookingTime", Integer.class).invoke(recipe, cookingTime);
		} catch(Exception e) {}
		
		return Bukkit.addRecipe(recipe);
	}
	
	/**
	 * Register a recipe using a randomly generated name
	 * @param items
	 * @param result
	 * @see #registerRecipe(String, String[], Map, ItemStack)
	 * @return true if the recipe was successfully added
	 */
	public boolean registerRecipe(@Nonnull String[] items, @Nonnull Map<Character, Material> ingredients, @Nonnull ItemStack result) {
		return registerRecipe(getRandomName(), items, ingredients, result);
	}
	
	/**
	 * Checks if the given Recipe is a custom recipe registered by the plugin that owns this RecipeManager
	 * @param recipe
	 * @return true if the recipe is a recipe registered by the plugin that owns this RecipeManager
	 */
	public boolean isCustomRecipe(Recipe recipe) {
		return recipes.containsValue(recipe);
	}
	
	/**
	 * If the recipe has been registered by the plugin that owns this RecipeManager, this returns the recipe's name. Else, this returns null
	 * @param recipe
	 * @return the name of the recipe registered by the plugin that owns this RecipeManager if it exists, else null
	 */
	public String getCustomRecipeName(Recipe recipe) {
		if(recipe == null || !isCustomRecipe(recipe))
			return null;
		
		for(Map.Entry<String, Recipe> entry : recipes.entrySet())
			if(entry.getValue().equals(recipe))
				return entry.getKey();
		
		return null;
	}
	
	HashMap<String, Recipe> getRecipes() {
		return recipes;
	}
	
	/**
	 * Gets the plugin that owns this RecipeManager
	 * @return the plugin that owns this RecipeManager
	 */
	@Nonnull
	public JavaPlugin getHostPlugin() {
		return plugin;
	}
	
	private static String getRandomName() {
		byte[] bytes = new byte[7];
		new Random().nextBytes(bytes);
		
		return new String(bytes);
	}
}
