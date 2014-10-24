package com.gmail.Haggard_nl.GraveYard.Managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public abstract interface DataBaseManager {
// connection
	public abstract Boolean existsTable(String string);
	public abstract void closeDB();
	public abstract boolean CheckConnection();
	public abstract Boolean execute(String query);
	public abstract Boolean insert(String string);
	public abstract Boolean delete(String query);
	public abstract Boolean update(String query);
	public abstract ArrayList<HashMap<String, Object>> GetMapList(String sqlString);
// Yards
	public abstract boolean setYard(String worldName, String Spawn,String cuboidInner, 
			String cuboidOuter, String boundTo, String undoFile);	
	public abstract boolean updateYard(String worldName, String Spawn,String cuboidInner, 
			String cuboidOuter, String boundTo, String undoFile);
	public abstract boolean deleteYard(String worldName, String cuboidInner);
	public abstract Integer getYardId(String worldName, String cuboidInner);
// Graves
//		Grave (graveId INTEGER PRIMARY KEY, yardId INTEGER NOT NULL, garvenumber INTEGER NOT NULL, chest1 varchar(60) NOT NULL, chest2 varchar(60) NOT NULL, skull varchar(60) NOT NULL, sign varchar(60) NOT NULL, UNIQUE (chest1, chest2, skull, sign));");
	public abstract boolean setGrave(Integer yardId, Integer garveNumber, String playerUUID,
			String chest1Loc, String chest2Loc, String skullLoc, String signLoc);	
	public abstract boolean updatetGrave(Integer yardId, Integer garveNumber, String playerUUID,
			String chest1Loc, String chest2Loc, String skullLoc, String signLoc);
		
// Coffins		
	public abstract boolean clearCoffin(Integer yardId, String worldName, String playerUUID);	


}
