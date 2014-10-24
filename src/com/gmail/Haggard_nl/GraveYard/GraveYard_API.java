package com.gmail.Haggard_nl.GraveYard;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.API;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.ServerCore.Managers.MessageManager;

public class GraveYard_API implements API {
		private GraveYardMain plugin;

		public GraveYard_API(GraveYardMain instance) {
		  this.plugin = instance;
		}
		
		@Override
		public String getVersion() {
			// TODO Auto-generated method stub
			return null;
		}
		
		// check if a player is dead
		@Override
		public Boolean isDead(String playerName){
			return GraveYardManager.getInstance().isBuriedPlayer(playerName);
		}
		// get grave spawn location
		@Override
		public Location getGraveYardSpawn(String playerName){
			String yardId = plugin.GraveYardManager.getPlayersGarveyard(playerName);
			if( yardId == null){
				return null;
			}
			return YAMLManager.getConfig().getLocation("settings.grave."+yardId+".GraveYard.Spawn");
		}

		@Override
		public String getName() {
			return "GraveYardMain";
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

}
