package com.gmail.Haggard_nl.GraveYard.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.MetadataUtil;

public class PlayerRespawnListener implements Listener{
	
	private GraveYardMain plugin;
	  
	public PlayerRespawnListener(GraveYardMain instance){
	    this.plugin = instance;
	  }
//Event will be called when player hits the "Respawn" button
	@EventHandler(priority=EventPriority.HIGHEST)
    public void onrespawn(final PlayerRespawnEvent event){
    	
    	final Player player = event.getPlayer();
    	
	   	if (!YAMLManager.getConfig().ContainsKey("settings.grave")){
//	   		if(YAMLManager.getConfig().getConfigurationSection("settings.grave").size() < 1){
	    		MessageManager.getInstance().debugMessage("No gravesyards set!");
	    		return;
//	   		}
	    }

	   	MetadataUtil m = new MetadataUtil(this.plugin);
		if ( ! m.getInstance().hasKey(player, "HasGrave")) {
    		MessageManager.getInstance().debugMessage("no death registered player!");
    		return;
    	}
    	
    	player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 2));
    	player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3));
    	Location loc = null;
    	String guildn = plugin.PandG_api.getPlayerGuild(player.getName());
    	if(guildn != null){
    	// check is guild has a private graveyard
    		for(String grave : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
    			String ggn = YAMLManager.getConfig().getString("settings.grave." + grave + ".boundto");
    			if(ggn.equalsIgnoreCase(guildn)){
    				loc = YAMLManager.getConfig().getLocation("settings.grave." + grave + ".GraveYard.Spawn");
    				break;
    			}
    		}
    	}
    	if (loc == null){
    		for(String grave : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
    			String ggn = YAMLManager.getConfig().getString("settings.grave." + grave + ".boundto");
    			if(ggn.equalsIgnoreCase("")){
    				loc = YAMLManager.getConfig().getLocation("settings.grave." + grave + ".GraveYard.Spawn");
    				break;
    			}
    		}
    	}
    	if(loc != null){
    		event.setRespawnLocation(loc);
            MessageManager.getInstance().debugMessage("Set spawnpoint in event!");
    	}else{
    		MessageManager.getInstance().debugMessage("Default Spawnpoint set in event!");
    	}
    }
}
