package com.gmail.Haggard_nl.GraveYard;

import org.bukkit.Location;

	public interface API {

		public Boolean isDead(String playerName);

		public Location getGraveYardSpawn(String playerName);

		public String getVersion();
		
		public String getName();
		
		public boolean isEnabled();
}
