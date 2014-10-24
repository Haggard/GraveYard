package com.gmail.Haggard_nl.Util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * 
 * @name UUIDLibrary V2.0
 * @author Haggard_nl
 *
 */
public class UUIDLibrary {
	
	public String getUUIDString(Player player){
		return player.getUniqueId().toString();
	}

	public UUID getUUID(Player player){
		return player.getUniqueId();
	}

	@SuppressWarnings("deprecation")
	public String getUUIDStringFromName(String name){
		Player val = Bukkit.getServer().getPlayer(name);
		if((val != null) && (val instanceof Player)){
			String val2 = val.getUniqueId().toString();
			return val2;
		}
		return "";
	}

	public UUID getUUIDFromName(String name){
		try{
			return Bukkit.getServer().getPlayer(name).getUniqueId();
		} catch (Exception e){
		}
		return null;
	}

	public Player getPlayerFromUUIDString(String uuid){
		return Bukkit.getServer().getPlayer(UUID.fromString(uuid));
	}

	public Player getPlayerFromUUID(UUID uuid){
		return Bukkit.getServer().getPlayer(uuid);
	}

	public String getNameFromUUIDString(String uuid){
		return Bukkit.getServer().getPlayer(UUID.fromString(uuid)).getName();
	}

	public String getNameFromUUID(UUID uuid){
		return Bukkit.getServer().getPlayer(uuid).getName();
	}

	public Player getPlayerFromName(String name){
		return Bukkit.getServer().getPlayer(name);
	}

	public String getNameFromPlayer(Player p){
		return p.getName();
	}

}