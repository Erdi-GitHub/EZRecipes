package com.erdi.ezrecipes.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.erdi.ezrecipes.api.RecipeApi;
import com.erdi.ezrecipes.api.RecipeManager;
import com.erdi.ezrecipes.api.event.FurnaceCookEvent;
import com.erdi.ezrecipes.api.event.FurnaceItemCollectEvent;
import com.erdi.ezrecipes.api.event.ItemCraftEvent;
import com.erdi.ezrecipes.plugin.listeners.FurnaceSmeltListener;
import com.erdi.ezrecipes.plugin.commands.CommandEZRecipes;
import com.erdi.ezrecipes.plugin.listeners.CraftItemListener;
import com.erdi.ezrecipes.plugin.listeners.FurnaceExtractListener;
import com.erdi.ezrecipes.plugin.util.Util;

/**
 * The EZRecipes main class
 * @author Erdi__
 */
public class EZRecipes extends JavaPlugin {
	private Logger logger = getLogger();
	
	private FileConfiguration config;
	
	private RecipeApi api;
	private RecipeManager manager;
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by the EZRecipes plugin itself!
	 */
	public void loadRecipes() {
		ConfigurationSection recipeSection = config.getConfigurationSection("recipes");
		
		for(String key : recipeSection.getKeys(false)) {
			ConfigurationSection recipe = recipeSection.getConfigurationSection(key);
			
			List<?> shape = recipe.getList("shape");
			
			if(shape == null) {
				logger.warning("Recipe " + key + " has no shape or has an invalid one");
				continue;
			}
			
			if(shape.size() == 0) {
				logger.warning("Recipe " + key + " has an invalid shape of size 0");
				continue;
			}
			
			if(!(shape.get(0) instanceof String)) {
				logger.warning("Recipe " + key + " must have a shape made up of strings");
				continue;
			}
			
			ConfigurationSection ingredientSection = recipe.getConfigurationSection("ingredients");
			if(ingredientSection == null) {
				logger.warning("Recipe " + key + " has no ingredients or has invalid ingredients");
				continue;
			}

			HashMap<Character, Material> ingredients = new HashMap<>();
			
			for(String ingredientKey : ingredientSection.getKeys(false)) {
				Material material = materialFromString(ingredientSection.getString(ingredientKey, ""));
				
				if(material == null) {
					logger.warning("Recipe " + key + " has invalid ingredient " + ingredientKey);
					continue;
				}
				
				ingredients.put(ingredientKey.charAt(0), material);
			}
			
			ConfigurationSection resultSection = recipe.getConfigurationSection("result");
			
			Material resultMaterial = materialFromString(resultSection.getString("material"));
			
			if(resultMaterial == null) {
				logger.warning("Recipe " + key + " has invalid result material");
				continue;
			}
			
			@SuppressWarnings("deprecation")
			ItemStack resultStack = new ItemStack(resultMaterial, resultSection.getInt("amount", 1), (short) resultSection.getInt("damage", 0), (byte) resultSection.getInt("rawdata", 0));
			
			if(!manager.registerRecipe(key, shape.toArray(new String[shape.size()]), (Map<Character, Material>) ingredients, resultStack)) {
				logger.warning("Recipe " + key + " couldn't be registered!");
			}
		}
		
		ConfigurationSection smeltingSection = config.getConfigurationSection("smeltingrecipes");
		
		for(String key : smeltingSection.getKeys(false)) {
			ConfigurationSection recipe = smeltingSection.getConfigurationSection(key);
			
			Material source = materialFromString(recipe.getString("source", ""));
			
			if(source == null) {
				logger.warning("Smelting Recipe " + key + " has an invalid source material");
				continue;
			}
			
			ConfigurationSection resultSection = recipe.getConfigurationSection("result");
			
			if(resultSection == null) {
				logger.warning("Smelting Recipe " + key + " doesn't have a result item");
				continue;
			}
			
			Material resultMaterial = materialFromString(resultSection.getString("material", ""));
			
			if(resultMaterial == null) {
				logger.warning("Smelting Recipe " + key + " has an invalid result material");
				continue;
			}

			@SuppressWarnings("deprecation")
			ItemStack result = new ItemStack(resultMaterial, resultSection.getInt("amount", 1), (short) resultSection.getInt("damage", 0), (byte) resultSection.getInt("rawdata", 0));
			
			if(!manager.registerSmeltingRecipe(key, source, result, resultSection.getInt("cookingtime", 10), (float) resultSection.getDouble("experience", 5))) {
				logger.warning("SmeltingRecipe " + key + " couldn't be registered!");
			}
		}
	}
	
	private Material materialFromString(String s) {
		Material material = Material.getMaterial(s);
		
//		if(material == null || material.name().startsWith("LEGACY")) {
//			try {
//				for(Method method : Material.class.getMethods())
//					if(method.getName().equals("getMaterial"))
//						if(method.getParameterCount() == 2)
//							return (Material) method.invoke(null, s, true);
//				
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		return material;
	}
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by the EZRecipes plugin itself!
	 */
	public String getPrefix() {
		return Util.color("&7[&3EZ &2Recipes&7] &r");
	}
	
	private File materialsFile = new File(getDataFolder(), "materials.txt");
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by Bukkit!
	 */
	@Override
	public void onLoad() {
		api = new RecipeApi();
		manager = api.getRecipeManager(this);
	}
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by Bukkit!
	 */
	@Override
	public void onEnable() {
		if(!materialsFile.exists())
			saveResource(materialsFile.getName(), false);
		
		saveDefaultConfig();
		
		config = getConfig();

		logger.info("Allowed material types of recipe items can be found at https://helpch.at/docs/" + Bukkit.getBukkitVersion().split("-")[0] + "/org/bukkit/Material.html");
		logger.info("Loading recipes...");
		
		try {
			loadRecipes();
			logger.info("Successfully loaded crafting recipes!");
		} catch(Exception e) {
			e.printStackTrace();
			logger.severe("An error occurred while loading crafting recipes. Stack-trace printed.");
		}

		if(debugEnabled()) {
			api.registerListener(FurnaceCookEvent.class, event -> {
				logger.info("EVENT: " + event.getClass());
				logger.info("");
				logger.info(event.getFurnace().toString());
				logger.info(event.getSource().toString());
				logger.info(event.getResult().toString());
			});
			
			api.registerListener(ItemCraftEvent.class, event -> {
				logger.info("EVENT: " + event.getClass());
				logger.info("");
				logger.info("Crafter: " + event.getCrafter());
				logger.info("Recipe: " + event.getRecipe());
				logger.info("Result: " + event.getResult());
			});
			
			api.registerListener(FurnaceItemCollectEvent.class, event -> {
				logger.info("EVENT: " + event.getClass());
				logger.info("");
				logger.info("Amount: " + event.getAmount());
				logger.info("Furnace: " + event.getFurnace());
				logger.info("Player: " + event.getPlayer());
				logger.info("Type: " + event.getType());
			});
		}
		
		PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new CraftItemListener(this), this);
		pluginManager.registerEvents(new FurnaceSmeltListener(this), this);
		pluginManager.registerEvents(new FurnaceExtractListener(this), this);
		
		getCommand("ezrecipes").setExecutor(new CommandEZRecipes(this));;
	}
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by Bukkit!
	 */
	@Override
	public void onDisable() {
		api = null;
	}
	
	/**
	 * PLEASE IGNORE THIS<br>
	 * it should be only called by the EZRecipes plugin itself!
	 */
	public boolean debugEnabled() {
		return config.getBoolean("debug", false);
	}
	
	/**
	 * Get the Recipe API for use in other plugins
	 * @return the Recipe API
	 */
	public RecipeApi recipeApi() {
		return api;
	}
}
