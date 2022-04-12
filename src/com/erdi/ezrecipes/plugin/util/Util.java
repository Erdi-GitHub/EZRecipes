package com.erdi.ezrecipes.plugin.util;

import org.bukkit.ChatColor;

/**
 * @author Erdi__
 */
public final class Util {
	private Util() {}
	
	public static String color(String messageText) {
		return ChatColor.translateAlternateColorCodes('&', messageText);
	}
}
