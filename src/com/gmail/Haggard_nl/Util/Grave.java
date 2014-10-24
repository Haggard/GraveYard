package com.gmail.Haggard_nl.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.material.Directional;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;

public class Grave{

	private List<String> graveBlocks = new ArrayList<String>();
	private Boolean flag;
	//
	private int Rx;
	private int Ry;
	private int Rz;
	private String Rm;
	private byte Rb;
	private BlockFace Rf;
	private int plotNr;
	private int bc =0;
	private String graveName = null;
	
	public Grave(Boolean flag, String GraveName){
		this.flag = flag;
		this.graveName = GraveName; 
		init();
	}

	// place a grave at the location
	// set metadata to system
	// save locations of sign and chests to Yml
	@SuppressWarnings("deprecation")
	public Boolean create(Location sLoc, int pointX, int pointZ, int nr){
		// place grave in area
		
		Location loc = sLoc.subtract(0, 1, 0);
		plotNr = nr;
		bc = 0;
				
		for(String s : graveBlocks){
			if(!splitStr(s)) { return false; }
			setBlock(loc.getBlock().getRelative((Rx + pointX), Ry, (Rz + pointZ))); // get block
			YAMLManager.getGraveYardConfig().set("grave." + graveName + "."  + plotNr + ".buried", "");
		}	
		return true;
	}

	@SuppressWarnings("deprecation")
	private void setBlock(Block b) {
//		SettingsManager.getGraveBlocksConfig().setLocation("grave." + graveName + "." + plotNr + "." + bc, b.getLocation());
		bc += 1;
		b.setType(Material.getMaterial(Rm));
		b.setMetadata("GraveOf", new FixedMetadataValue(GraveYardMain.getInstance(), "system"));
		if(Rb != 0){
			b.getState().getData().setData(Rb);
		}
		if (Rm.equalsIgnoreCase("SANDSTONE_STAIRS")){
			// To set the 4th bit to zero:
			byte val = (byte) (b.getData() & ~(1 << 3));
			b.getState().setRawData(val);
			
		}
		if (b.getState() instanceof Directional){
			
			if (Rm.equalsIgnoreCase("CHEST")){
				org.bukkit.material.Chest chest = new org.bukkit.material.Chest(0, b.getData());
				chest.setFacingDirection(Rf); // BlockFace.NORTH example
				b.setData(chest.getData());
			}else if ( Rm.equalsIgnoreCase("SKULL")){
				org.bukkit.material.Skull skull = new org.bukkit.material.Skull(0, b.getData());
				skull.setFacingDirection(Rf); // BlockFace.NORTH example
				b.setData(skull.getData());
			}else{
				Directional f = (Directional) b.getState().getData();
				f.setFacingDirection(Rf);
				
			}
		}
		
		if (Rm.equalsIgnoreCase("SKULL")){
			YAMLManager.getGraveYardConfig().setLocation("grave." + graveName + "." + plotNr + ".skullLoc", b.getLocation());
		}
		if (Rm.equalsIgnoreCase("WALL_SIGN")){ 
			YAMLManager.getGraveYardConfig().setLocation("grave." + graveName + "." + plotNr + ".signLoc", b.getLocation());
			Sign blocksign = (Sign)b.getState();
			blocksign.setRawData(determineDataOfDirection(Rf));
		    blocksign.setLine(0, "[R.I.P]");
		    blocksign.setLine(1, "       " );
		    blocksign.setLine(2, "       " );
		    blocksign.setLine(3, "Age:   ");
		    blocksign.update();
		} else if (Rm.equalsIgnoreCase("CHEST")){
			if (!YAMLManager.getGraveYardConfig().ContainsKey("grave." + graveName + "." + plotNr + ".Chest1Loc")){
				YAMLManager.getGraveYardConfig().setLocation("grave." + graveName + "." + plotNr + ".Chest1Loc", b.getLocation());
			}else{
				YAMLManager.getGraveYardConfig().setLocation("grave." + graveName + "." + plotNr + ".Chest2Loc", b.getLocation());
			}
		}
	}
	
	private Boolean splitStr(String s) {
		try {
			String[] parts = s.split(",");
			Rx = Integer.parseInt(parts[0]);
			Ry = Integer.parseInt(parts[1]);
			Rz = Integer.parseInt(parts[2]);
			Rm = parts[3];
			if (parts.length == 4){
				Rb = 0;
				Rf = null;
			} else if (parts.length == 5){
				Rb = 0;
				Rf = BlockFace.valueOf(parts[4]);
			} else if (parts.length == 6){
				Rb = Byte.parseByte(parts[4]);
				Rf = BlockFace.valueOf(parts[5]);
			}else{
				Rb = 0;
				Rf = BlockFace.valueOf(parts[4]);
			}
			
		} catch (NumberFormatException e) {
			MessageManager.getInstance().consoleError("[splitStr] " + s.toString() );
			return false;
		}
		return true;
	}

	public byte determineDataOfDirection(BlockFace bf){
		if(bf == null){
			MessageManager.getInstance().debugMessage("[determineDataOfDirection] bf = null");
			return(byte)0;
		}
		MessageManager.getInstance().debugMessage("[determineDataOfDirection] " + bf.toString());
	     if(bf.equals(BlockFace.NORTH))
	            return (byte)2;
	     else if(bf.equals(BlockFace.SOUTH))
	             return (byte)3;
	     else if(bf.equals(BlockFace.WEST))
	            return (byte)4;
	     else if(bf.equals(BlockFace.EAST))
	            return (byte)5;
	     return (byte)0;
	}

	private void init() {
		if(!this.flag){
			// grave build on z axe
				graveBlocks.add("1,0,0,SANDSTONE,NORTH"); // sandstone gaveplate
				graveBlocks.add("1,0,1,SANDSTONE,NORTH");
				graveBlocks.add("1,0,2,SANDSTONE,NORTH");
				
				graveBlocks.add("1,1,0,SANDSTONE,SOUTH"); // sandstone stairs headstone
				graveBlocks.add("1,2,0,SKULL,SOUTH"); // skull
				
				graveBlocks.add("1,1,1,WALL_SIGN,NORTH"); // wallsign				
				graveBlocks.add("1,-1,1,CHEST,EAST"); // chest 1
				graveBlocks.add("1,-1,2,CHEST,EAST"); // chest 2
			
		}else{
			// grave build on x axe
				graveBlocks.add("0,0,1,SANDSTONE,EAST"); // sandstone gaveplate
				graveBlocks.add("1,0,1,SANDSTONE,EAST");
				graveBlocks.add("2,0,1,SANDSTONE,EAST");
				
				graveBlocks.add("0,1,1,SANDSTONE,WEST"); // sandstone stairs headstone
				graveBlocks.add("0,2,1,SKULL,WEST"); // skull
				
				graveBlocks.add("1,1,1,WALL_SIGN,EAST"); // wallsign				
				graveBlocks.add("1,-1,1,CHEST,SOUTH"); // chest 1
				graveBlocks.add("2,-1,1,CHEST,SOUTH"); // chest 2
				
		}	
	}



}