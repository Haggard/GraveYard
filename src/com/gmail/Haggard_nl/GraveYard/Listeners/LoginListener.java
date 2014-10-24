package com.gmail.Haggard_nl.GraveYard.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.Util.MetadataUtil;


public class LoginListener implements Listener {

	private GraveYardMain plugin;

	public LoginListener(GraveYardMain instance){
	  this.plugin = instance;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		MetadataUtil m = new MetadataUtil(this.plugin);
		final Player player = event.getPlayer();
		// has grave
		if(! GraveYardManager.getInstance().isBuriedPlayer(player.getName())){
	        MessageManager.getInstance().debugMessage("No grave at login!");
			return;
		}

		m.getInstance().set(player, "HasGrave", true);
		if((Bukkit.getPluginManager().getPlugin("LoginCheck") == null) || !(Bukkit.getPluginManager().getPlugin("LoginCheck").isEnabled())) {
			Location loc = GraveYardManager.getInstance().getPlayersSpawnLocation(player.getName());
	        if (loc != null){
	        	player.teleport(loc);
	        }
	        MessageManager.getInstance().debugMessage("Set spawnpoint in event!");
		}
	}
	
}