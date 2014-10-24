
package com.gmail.Haggard_nl.GraveYard.Managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager.MessageType;
import com.gmail.Haggard_nl.Util.Cuboid;
import com.gmail.Haggard_nl.Util.MetadataUtil;
import com.gmail.Haggard_nl.Util.Utils;


public class GraveYardManager {

    /**
     * 
     */
    private Material graveMaterial = Material.SANDSTONE;
	private ConversationFactory cfactory = null;
    private static GraveYardMain plugin;
    private List<String> deadPlayers = new ArrayList<String>();
    private MetadataUtil m ;
    private List<Cuboid> graves = new ArrayList<Cuboid>();
    public List<String> openChest = new ArrayList<String>();

/**
 * 
 */
    public GraveYardManager() {
        plugin = GraveYardMain.getInstance();
        this.cfactory = new ConversationFactory(this.plugin);
        this.m = new MetadataUtil(this.plugin);
    }
/**
 * 
 */
    public void loadDeads() {
    	if(YAMLManager.getGraveYardConfig().ContainsKey("grave")){
			for (String graveyard : YAMLManager.getGraveYardConfig().getConfigurationSection("grave")){
		    	if(YAMLManager.getConfig().ContainsKey("settings.grave."+graveyard+".cuboidOuter")){
			    	Cuboid c = new Cuboid(YAMLManager.getConfig().getMap("settings.grave."+graveyard+".cuboidOuter"));
			    	graves.add(c);
			    	c.SetMetaData("GraveOf", "system");
			    	c = null;
		    	}
				for (String grave : YAMLManager.getGraveYardConfig().getConfigurationSection("grave." + graveyard)){
					String deadPlayer = YAMLManager.getGraveYardConfig().getString("grave." + graveyard + "." + grave + ".buried"); 
					if (deadPlayer.length() == 0){ 
						deadPlayer = "system"; 
					}
					if ((deadPlayer != null) && !(deadPlayer.equalsIgnoreCase("system"))){
						deadPlayers.add(deadPlayer);
					}
					restoreProtection(graveyard, grave, deadPlayer);
				}
			}
    	}
		MessageManager.getInstance().debugMessage("Pending deads " + deadPlayers.size() + deadPlayers.toString());
	}
 /**
  *    
  * @param plotNr
  * @param owner
  */
    private void restoreProtection(String graveId, String grave, String owner){
    	if(YAMLManager.getGraveYardConfig().ContainsKey("grave." + graveId + "." + grave + ".Chest1Loc")){
	     	Location loc = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + "." + grave + ".Chest1Loc");
			m.getInstance().set(loc.getBlock(),"GraveOf", owner);
			m.getInstance().set(loc.getBlock().getRelative(BlockFace.UP),"GraveOf", owner);
    	}else{
    		MessageManager.getInstance().debugMessage("graveId " + graveId + " grave " + grave );	
    		MessageManager.getInstance().debugMessage("grave." + graveId + "." + grave + ".Chest1Loc");
    	}
    	if(YAMLManager.getGraveYardConfig().ContainsKey("grave." + graveId + "." + grave + ".Chest2Loc")){
	    	Location loc2 = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + "." + grave + ".Chest2Loc");
			m.getInstance().set(loc2.getBlock(),"GraveOf", owner);
			m.getInstance().set(loc2.getBlock().getRelative(BlockFace.UP),"GraveOf", owner);
    	}else{
    		MessageManager.getInstance().debugMessage("grave." + graveId + "." + grave + ".Chest2Loc");	
    	}
    }
/**
 * 
 * Check is a location is in a graveyard
 * 
 * @param loc
 * @return
 */
    public Boolean locInGraveYard(Location loc){
		MessageManager.getInstance().debugMessage("Yards " + graves.size());
    	for (Cuboid c : graves){
    		MessageManager.getInstance().debugMessage("cuboid " + c.toString());
    		MessageManager.getInstance().debugMessage("in loc " + c.contains(loc));
    		if(c.contains(loc)){
    			return true;
    		}
    	}
    	return false;
    }

/**
 * 
 * @param player
 * @return
 */
    public Boolean isBuriedPlayer(String player){
       	if(YAMLManager.getGraveYardConfig().ContainsKey("grave")){
			for (String graveyard : YAMLManager.getGraveYardConfig().getConfigurationSection("grave")){
				for( String grave :YAMLManager.getGraveYardConfig().getConfigurationSection("grave." + graveyard)){
					String dead = YAMLManager.getGraveYardConfig().getString("grave." + graveyard + "." + grave + ".buried"); 
					if (dead.length() == 0){ 
						dead = "system";
					}
					if(dead.equalsIgnoreCase(player)){
						return true;
					}
				}
			}
       	}
    	return false;
    }
/**
 * 
 * @param player
 * @return
 */
    public String getPlayersGarveId(String playerName){
// find existing grave
		if(YAMLManager.getGraveYardConfig().ContainsKey("grave")){
			for (String garveyard : YAMLManager.getGraveYardConfig().getConfigurationSection("grave")){
				for (String key : YAMLManager.getGraveYardConfig().getConfigurationSection("grave." + garveyard)){
					String deadPlayer = YAMLManager.getGraveYardConfig().getString("grave." + garveyard + "."+ key + ".buried"); 
					if (deadPlayer.length() == 0){ deadPlayer = "system"; }
					if(deadPlayer.equalsIgnoreCase(playerName)){
						return (garveyard + "." +Integer.parseInt(key));
					}
				}
	       	}
	   	}
	    return null;
    }
 
    
    public String getGarvePlayer(String playerName){
       	if(YAMLManager.getGraveYardConfig().ContainsKey("grave")){
			for (String garveyard : YAMLManager.getGraveYardConfig().getConfigurationSection("grave")){
 				for (String key : YAMLManager.getGraveYardConfig().getConfigurationSection("grave." + garveyard)){
					String deadPlayer = YAMLManager.getGraveYardConfig().getString("grave." + garveyard + "."+ key + ".buried"); 
					if (deadPlayer.length() == 0){
						deadPlayer = "system"; 
					}
					if(deadPlayer.equalsIgnoreCase(playerName)){
						return garveyard;
					}
				}
	       	}
       	}
		return null;
    }
    
    public String getPlayersGarveyard(String playerName){
		String guildn = null;
		if(plugin.PandG_api.isInGuild(playerName)){
			guildn = plugin.PandG_api.getPlayerGuild(playerName);
		}
		
		MessageManager.getInstance().debugMessage("Guildn " + guildn);
		if(guildn != null){
			// find grave taht is bound to the players-guild
			for (String graveId : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
				String GraveGuildn = YAMLManager.getConfig().getString("settings.grave."+ graveId + ".boundto");
				if(GraveGuildn.equalsIgnoreCase(guildn)){
					return graveId;
				}
			}
		}	
		// find unbound	graves (use also if no guild graveyard is set		
		for (String graveId : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
			MessageManager.getInstance().debugMessage("graveId " + graveId);
			
			String GraveGuildn = YAMLManager.getConfig().getString("settings.grave."+ graveId + ".boundto");
			if(GraveGuildn.equalsIgnoreCase("")){
				return graveId;
			}
		}
		
	    return null;
    }
/**
 * 
 * @param player
 * @param key
 */
    public void fillExistingGrave(Player player, String Yard_and_plotNr){
    	if(!YAMLManager.getGraveYardConfig().ContainsKey("grave." + Yard_and_plotNr + ".signLoc")){
    		MessageManager.getInstance().debugMessage("Grave not found error grave." + Yard_and_plotNr + ".signLoc");
    		return;
    	}
		Location loc = YAMLManager.getGraveYardConfig().getLocation("grave." + Yard_and_plotNr + ".signLoc");
		if (loc != null){
		    Block testForSignBlock = loc.getBlock();
		    Sign blocksign = (Sign)testForSignBlock.getState();
		    blocksign.setLine(0, "[R.I.P]");
		    blocksign.setLine(1, player.getName());
		    blocksign.setLine(2, Utils.getDateTime());
		    blocksign.setLine(3, "Age: " + Utils.convertTicks(player.getTicksLived()));
		    blocksign.update();
		    m.getInstance().set(blocksign, "GraveOf", player.getName());
			Location loc_a = YAMLManager.getGraveYardConfig().getLocation("grave." + Yard_and_plotNr + ".Chest1Loc");
			Location loc_b = YAMLManager.getGraveYardConfig().getLocation("grave." + Yard_and_plotNr + ".Chest2Loc");
			if ((loc_a != null) && (loc_b != null)) {
				Block c1 = loc_a.getBlock();
				Block c2 = loc_b.getBlock();
				m.getInstance().set(c1,"GraveOf", player.getName());
				m.getInstance().set(c2, "GraveOf", player.getName());
				m.getInstance().set(c1.getRelative(0, 1, 0), "GraveOf", player.getName());
				m.getInstance().set(c2.getRelative(0, 1, 0),"GraveOf", player.getName());
				Chest chest_b = null;
				if (c2.getType() == Material.CHEST) {
					  chest_b = (Chest) c2.getState();
				}
				if (c1.getType() == Material.CHEST) {
					  Chest chest_a = (Chest) c1.getState();
					  putInventoryInChests(player, chest_a, chest_b );
				}
				 loc = YAMLManager.getGraveYardConfig().getLocation("grave." + Yard_and_plotNr + ".skullLoc");
					if (loc != null){
					    Block matSkullBlock = loc.getBlock();
					    org.bukkit.material.Skull matSkull = new org.bukkit.material.Skull(Material.SKULL_ITEM);
//							    matSkull.setFacingDirection(BlockFace.WEST);
						org.bukkit.block.Skull s = (org.bukkit.block.Skull)matSkullBlock.getState();
					    s.setSkullType(SkullType.PLAYER);
					    s.setOwner(player.getName());
					    s.setData(matSkull);
					    s.update();
					    m.getInstance().set(matSkullBlock, "GraveOf", player.getName());
					}				
					
					
			}else{
				MessageManager.getInstance().debugMessage("Chest a or b is null.");
				return;
			}
			deadPlayers.add(player.getName());
	    	YAMLManager.getGraveYardConfig().set("grave." + Yard_and_plotNr + ".buried", player.getName());
		    	return;
			}
		}
 /**
  *    
  * @return
  */
	public ConversationFactory getConversationFactory(){
    	return this.cfactory;
    }
/**
 * 
 * @return
 */
    public static GraveYardManager getInstance() {
        return new GraveYardManager();
    }
/**
 * 
 * @param player
 */
	public void setGrave(Player player) {
       	if(!YAMLManager.getGraveYardConfig().ContainsKey("grave")){
       		YAMLManager.getGraveYardConfig().createConfigurationSection("grave");
       	}
		String graveyard = getPlayersGarveyard(player.getName());
		
		MessageManager.getInstance().debugMessage("Yard " +graveyard );
		for( String key :YAMLManager.getGraveYardConfig().getConfigurationSection("grave."+graveyard)){
			String deadPlayer = YAMLManager.getGraveYardConfig().getString("grave." + graveyard+ "." + key + ".buried");
			if ((deadPlayer == null) || (deadPlayer.length() == 0)){
				Location loc = YAMLManager.getGraveYardConfig().getLocation("grave."  + graveyard+ "." + key + ".signLoc");
				if (loc != null){
				    Block testForSignBlock = loc.getBlock();
				    if(testForSignBlock.getState() instanceof Sign){
					    Sign blocksign = (Sign)testForSignBlock.getState();
					    blocksign.setLine(0, "[R.I.P]");
					    blocksign.setLine(1, player.getName());
					    blocksign.setLine(2, Utils.getDateTime());
					    blocksign.setLine(3, "Age: " + Utils.convertTicks(player.getTicksLived()));
					    blocksign.update();
					    m.getInstance().set(blocksign, "GraveOf", player.getName());
						Location loc_a = YAMLManager.getGraveYardConfig().getLocation("grave."  + graveyard+ "." + key + ".Chest1Loc");
						Location loc_b = YAMLManager.getGraveYardConfig().getLocation("grave."  + graveyard+ "." + key + ".Chest2Loc");
						if ((loc_a != null) && (loc_b != null)) {
							Block c1 = loc_a.getBlock();
							Block c2 = loc_b.getBlock();
							m.getInstance().set(c1,"GraveOf", player.getName());
							m.getInstance().set(c2, "GraveOf", player.getName());
							m.getInstance().set(c1.getRelative(0, 1, 0), "GraveOf", player.getName());
							m.getInstance().set(c2.getRelative(0, 1, 0),"GraveOf", player.getName());
							Chest chest_b = null;
							if (c2.getType() == Material.CHEST) {
								  chest_b = (Chest) c2.getState();
							}
							if (c1.getType() == Material.CHEST) {
								  Chest chest_a = (Chest) c1.getState();
								  putInventoryInChests(player, chest_a, chest_b );
							}
							 loc = YAMLManager.getGraveYardConfig().getLocation("grave."  + graveyard+ "." + key + ".skullLoc");
								if (loc != null){
								    Block matSkullBlock = loc.getBlock();
								    org.bukkit.material.Skull matSkull = new org.bukkit.material.Skull(Material.SKULL_ITEM);
	//							    matSkull.setFacingDirection(BlockFace.WEST);
									org.bukkit.block.Skull s = (org.bukkit.block.Skull)matSkullBlock.getState();
								    s.setSkullType(SkullType.PLAYER);
								    s.setOwner(player.getName());
								    s.setData(matSkull);
								    s.update();
								    m.getInstance().set(matSkullBlock, "GraveOf", player.getName());
								}				
								
								
						}else{
							MessageManager.getInstance().debugMessage("Chest a or b is null.");
							return;
						}
						deadPlayers.add(player.getName());
				    	YAMLManager.getGraveYardConfig().set("grave."  + graveyard+ "." + key + ".buried", player.getName());
				    	return;
					}
				}
			}
			MessageManager.getInstance().msg(player, MessageType.BAD, "All graves as in use! You won't be burried and you lose your stuff! Sorry....");
		}
	}
/**
 * 
 * @param player
 */
	public void removeGrave(Player player) {
		String graveId = this.getPlayersGarveId(player.getName());
		
		YAMLManager.getGraveYardConfig().set("grave." + graveId + ".buried","");
		if(deadPlayers.contains(player.getName())){
			deadPlayers.remove(player.getName());
		}
		m.remove(player, "HasGrave");
		
		Location loc = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + ".signLoc");
		if (loc != null){
		    Block testForSignBlock = loc.getBlock();
		    Sign blocksign = (Sign)testForSignBlock.getState();
		    blocksign.setLine(0, "[R.I.P]");
		    blocksign.setLine(1, "");
		    blocksign.setLine(2, "");
		    blocksign.setLine(3, "");
		    blocksign.update();
		    blocksign.setMetadata("GraveOf", new FixedMetadataValue(this.plugin, "system"));;
			Location loc_a = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + ".Chest1Loc");
			Location loc_b = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + ".Chest2Loc");
			if ((loc_a != null) && (loc_b != null)) {
				Block c1 = loc_a.getBlock();
				Block c2 = loc_b.getBlock();
				m.getInstance().remove(c1,"GraveOf");
				m.getInstance().remove(c2, "GraveOf");
				Chest chest_b = null;
				if (c2.getType() == Material.CHEST) {
					chest_b = (Chest) c2.getState();
					chest_b.getInventory().clear();
					c2.getRelative(0, 1, 0).setType(graveMaterial);
					m.getInstance().set(c2.getRelative(0, 1, 0),"GraveOf", "system");
				}
				if (c1.getType() == Material.CHEST) {
					Chest chest_a = (Chest) c1.getState();
					chest_a.getInventory().clear();
					c1.getRelative(0, 1, 0).setType(graveMaterial);
					m.getInstance().set(c1.getRelative(0, 1, 0),"GraveOf", "system");
				}
			}else{
		    	MessageManager.getInstance().debugMessage("Chest a or b is null.");
				return;
			}
			loc = YAMLManager.getGraveYardConfig().getLocation("grave." + graveId + ".skullLoc");
			if (loc != null){
			    Block matSkullBlock = loc.getBlock();
			    matSkullBlock.setType(Material.AIR);
//					    org.bukkit.material.Skull matSkull = new org.bukkit.material.Skull(Material.SKULL_ITEM);
//					    matSkull.setFacingDirection(BlockFace.WEST);
//						org.bukkit.block.Skull s = (org.bukkit.block.Skull)matSkullBlock.getState();
//					    s.setSkullType(SkullType.SKELETON);
//					    s.setData(matSkull);
//					    s.update();
			    matSkullBlock.setType(Material.SKULL);
			    m.getInstance().set(matSkullBlock, "GraveOf", "system");
			}
		}
	}
	
/**
 * 	
 * @param chest
 * @return
 */
	private int slotsUsed(Chest chest){
		int items = 0;
		int usedSlots = 0;
		Inventory inventory = chest.getInventory();
		for(ItemStack i : inventory.getContents()){
		    if(i != null)
		    	usedSlots ++;
		}
		return usedSlots;
	}
/**
 * 	
 * @param player
 * @param graveChestA
 * @param graveChestB
 */
	public void putInventoryInChests(Player player, Chest graveChestA, Chest graveChestB){
	    PlayerInventory players_inventory = player.getInventory();
	    ItemStack[] items = players_inventory.getContents();
	    graveChestA.getInventory().addItem(player.getEquipment().getArmorContents());
	    int cnt = slotsUsed(graveChestA);
	    for (ItemStack item : items) {
	      if (item != null){ 
    		  if (cnt < 27) {
		        graveChestA.getInventory().addItem(new ItemStack[] { item });
		        cnt++;
		      }else{
		        graveChestB.getInventory().addItem(new ItemStack[] { item });	
		        cnt ++;
		      }
	      }
	    }
	    players_inventory.clear();
	    player.getEquipment().clear();
	  }
/**
 * 
 * @param player
 * @param graveChestA
 * @param graveChestB
 */
	@SuppressWarnings("deprecation")
	public void putChestsInventoryToPlayer(Player player, Chest graveChestA, Chest graveChestB){
		if ((graveChestA != null) && (slotsUsed(graveChestA) > 0)){
			for (ItemStack item : graveChestA.getInventory().getContents()){
				if (item != null){
					player.getInventory().addItem(new ItemStack[] { item });
				}
			}
			graveChestA.getInventory().clear();
		}else{
			MessageManager.getInstance().debugMessage("Single chest empty or null");
		}
		if ((graveChestB != null) && (slotsUsed(graveChestB) > 0)){
			for (ItemStack item : graveChestB.getInventory().getContents()){
				if (item != null){
					player.getInventory().addItem(new ItemStack[] { item });
				}
			}
			graveChestB.getInventory().clear();
		}else{
			MessageManager.getInstance().debugMessage("doublechest empty or null");
		}
		player.updateInventory();
	}
	
	public Location getPlayersSpawnLocation(String playerName) {
		// get guildname is player is in a guild
		String guildn = null;
		if(plugin.PandG_api.isInGuild(playerName)){
			guildn = plugin.PandG_api.getPlayerGuild(playerName);
		}
		// get spawnpoint
		String graveId = getGarvePlayer(playerName);
		// player not dead
		if(graveId == null){
			return null;
		}
		return YAMLManager.getConfig().getLocation("settings.grave." + graveId + ".GraveYard.Spawn");
	}
	
	
	
	public boolean playerCanBeBurried(String playerName) {
		String guildn = null;
		if(plugin.PandG_api.isInGuild(playerName)){
			guildn = plugin.PandG_api.getPlayerGuild(playerName);
		}
		if(guildn != null){
			// find grave taht is bound to the players-guild
			for (String graveId : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
				String GraveGuildn = YAMLManager.getConfig().getString("settings.grave."+ graveId + ".boundto");
				if(GraveGuildn.equalsIgnoreCase(guildn)){
					return true;
				}
			}
		}else{
			// find unbound	graves		
			for (String graveId : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
				String GraveGuildn = YAMLManager.getConfig().getString("settings.grave."+ graveId + ".boundto");
				if(GraveGuildn.equalsIgnoreCase("")){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean hasGeneralGraveYard() {
		if (YAMLManager.getConfig().ContainsKey("settings.grave")){
			for(String graveId: YAMLManager.getConfig().getConfigurationSection("settings.grave")){
				String GraveGuildn = YAMLManager.getConfig().getString("settings.grave."+ graveId + ".boundto");
				if(GraveGuildn.equalsIgnoreCase("")){
					return true;
				}
			}
		}
		return false;
	}	
 
}
