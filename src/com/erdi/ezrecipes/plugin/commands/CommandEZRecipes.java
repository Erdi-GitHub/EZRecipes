package com.erdi.ezrecipes.plugin.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.erdi.ezrecipes.plugin.EZRecipes;

/**
 * @author Erdi__
 */
public class CommandEZRecipes implements TabExecutor {
	private EZRecipes plugin;
	
	public CommandEZRecipes(EZRecipes instance) {
		plugin = instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1)
			return Arrays.asList("reload", "rl");

		return Arrays.asList();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0)
			return false;
		
		System.out.println(Arrays.toString(args));
		
		if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
			sender.sendMessage("Reloading...");
			
			Bukkit.resetRecipes();
			plugin.reloadConfig();
			try {
				plugin.loadRecipes();
				sender.sendMessage("Reloaded!");
			} catch(Exception e) {
				sender.sendMessage("Exception occurred! Stack printed.");
				e.printStackTrace();
				
				return true;
			}
			
			return true;
		}
		
		return false;
	}
}
