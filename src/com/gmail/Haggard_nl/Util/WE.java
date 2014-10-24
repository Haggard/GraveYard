package com.gmail.Haggard_nl.Util;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.FilenameException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;

public class WE {
/*
public void Save(Location l1, Location l2){
			Location l11 = l1;// Location representing one corner of the region
			Location l21 = l2; // Location representing the corner opposite to <l1>
			// ensure WorldEdit is available
			WorldEditPlugin wep = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
			if (wep == null) {
			  // then don't try to use TerrainManager!
			}
			 
			// create a terrain manager object
			TerrainManager tm = new TerrainManager(wep, player);
			// OR - without needing an associated Player
			TerrainManager tm = new TerrainManager(wep, world);
			 
			// don't include an extension - TerrainManager will auto-add ".schematic"
			File saveFile = new File(plugin.getDataFolder(), "backup1");
			 
			// save the terrain to a schematic file
			try {
			  tm.saveTerrain(saveFile, l1, l2);
			} catch (FilenameException e) {
			  // thrown by WorldEdit - it doesn't like the file name/location etc.
			} catch (DataException e) {
			  // thrown by WorldEdit - problem with the data
			} catch (IOException e) {
			  // problem with creating/writing to the file
			}
			 
	}

public void load(){
	// reload a schematic
	try {
	  // reload at the given location
	  Location location = new Location(x, y, z);
	  tm.loadSchematic(saveFile, location);
	  // OR
	  // reload at the same place it was saved
	  tm.loadSchematic(saveFile);
	} catch (FilenameException e) {
	  // thrown by WorldEdit - it doesn't like the file name/location etc.
	} catch (DataException e) {
	  // thrown by WorldEdit - problem with the data
	} catch (IOException e) {
	  // problem with opening/reading the file
	} catch (MaxChangedBlocksException e) {
	  // thrown by WorldEdit - the schematic is larger than the configured block limit for the player
	} catch (EmptyClipboardException e) {
	// thrown by WorldEdit - should be self-explanatory
	}
}

*/
}
