package CustomListeners;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import CustomEvents.CreateGravesEvent;
import CustomEvents.RemoveGravesEvent;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.Cuboid;
import com.gmail.Haggard_nl.Util.SimpleRectanglePacker;
import com.gmail.Haggard_nl.Util.TerrainManager;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.FilenameException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;

public class GravesEventListener implements Listener {
	private GraveYardMain plugin; 
	
	public GravesEventListener(GraveYardMain instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void onRemoveGravesEvent(RemoveGravesEvent e) {
		
		MessageManager.getInstance().consoleGoodMsg("RemoveGravesEvent");
		// reload a schematic
		WorldEditPlugin wep = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
		if (wep != null) {
			MessageManager.getInstance().consoleInfoMsg("Worldedit found");
			try {
			  // reload at the given location
			  TerrainManager tm = new TerrainManager(wep, e.getPlayer().getWorld());
	//		  tm.loadSchematic(e.getSavefile());
			  // OR
				File saveFile = new File(plugin.getDataFolder(), e.getSavefile());
	
			  // reload at the same place it was saved
			  tm.loadSchematic(saveFile);
			} catch (MaxChangedBlocksException ex) {
			  // thrown by WorldEdit - the schematic is larger than the configured block limit for the player
				MessageManager.getInstance().consoleErrorMsg("the schematic is larger than the configured block limit for the player.  " + ex.getMessage());
			} catch (EmptyClipboardException ex) {
			// thrown by WorldEdit - should be self-explanatory
				MessageManager.getInstance().consoleErrorMsg("EmptyClipboardException.  " + ex.getMessage());
			} catch (FilenameException ex) {
			  // thrown by WorldEdit - it doesn't like the file name/location etc.
				MessageManager.getInstance().consoleErrorMsg("it doesn't like the file name/location etc.  " + ex.getMessage());
			} catch (DataException ex) {
			  // thrown by WorldEdit - problem with the data
				MessageManager.getInstance().consoleErrorMsg("problem with the data.  " + ex.getMessage());
			} catch (IOException ex) {
			  // problem with creating/writing to the file
				MessageManager.getInstance().consoleErrorMsg("problem with creating/writing to the file.  " + ex.getMessage());
			}
		}else{
			MessageManager.getInstance().consoleErrorMsg("Worldedit not found");
			
		}
       	YAMLManager.getConfig().set("settings.grave." + e.getgraveyardId(), null);
//       	YAMLManager.getGraveBlocksConfig().set("grave." + e.getgraveyardId(), null);
       	YAMLManager.getGraveYardConfig().set("grave." + e.getgraveyardId(), null);
       	World world = e.getPlayer().getServer().getWorld("world");//get the world
        List<Entity> entList = world.getEntities();//get all entities in the world
  // remove dropped items
        for(final Entity current : entList){//loop through the list
            if (current instanceof Item){
    			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
    				 
    				@Override
    				public void run() {
    				 	//make sure we aren't deleting mobs/players
    					Location pos = current.getLocation();
    	        		pos.getWorld().playEffect(pos,Effect.POTION_BREAK, 0);
    		            current.remove();//remove it
    				}
    			}, 15);
           }
        }
        // delete the schematic-file.
       	try{
     		File file = new File(plugin.getDataFolder(), e.getSavefile());
     		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete "+file.getName()+" is failed.");
    		}
    	}catch(Exception ex){
     		ex.printStackTrace();
    	}
	}
	 	 
	@EventHandler
	public void onCreateGravesEvent(CreateGravesEvent e) {
		
		MessageManager.getInstance().consoleGoodMsg("CreateGravesEvent");
		if(e.getPlayerName().equalsIgnoreCase("pokuit")) {
			e.setCancelled(true);
		}
	    // Create a restorepoint
        Location pOne = e.getPos1();
        Location pTwo = e.getPos2();
        Cuboid d = new Cuboid(pOne, pTwo);
        
		Location l1 = d.getLowerNE().subtract(1, 2, 1);// Location representing one corner of the region
		Location l2 = d.getUpperSW().add(1, 2, 1); // Location representing the corner opposite to <l1>
		// ensure WorldEdit is available
		WorldEditPlugin wep = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
		if (wep != null) {
			MessageManager.getInstance().debugMessage("Worldedit found");
//			// create a terrain manager object
//			TerrainManager tm = new TerrainManager(wep, e.getPlayer());
			// OR - without needing an associated Player
			TerrainManager tm = new TerrainManager(wep, e.getPlayer().getWorld());
			MessageManager.getInstance().consoleErrorMsg("io " + plugin.getDataFolder() + "  " + "Grave" + d.toVecString());
			// don't include an extension - TerrainManager will auto-add ".schematic"
			File saveFile = new File(plugin.getDataFolder(), "Grave" + d.toVecString());
			 
			// save the terrain to a schematic file
			try {
			  tm.saveTerrain(saveFile, l1, l2);
			} catch (FilenameException ex) {
			  // thrown by WorldEdit - it doesn't like the file name/location etc.
				MessageManager.getInstance().consoleErrorMsg("it doesn't like the file name/location etc.  " + ex.getMessage());
			} catch (DataException ex) {
			  // thrown by WorldEdit - problem with the data
				MessageManager.getInstance().consoleErrorMsg("problem with the data.  " + ex.getMessage());
			} catch (IOException ex) {
			  // problem with creating/writing to the file
				MessageManager.getInstance().consoleErrorMsg("problem with creating/writing to the file.  " + ex.getMessage());
			}
		}else{
			MessageManager.getInstance().consoleErrorMsg("Worldedit not found, no restore created");

		}
        
     // Create a restorepoint
        String graveName = d.toVecString();
        YAMLManager.getConfig().set("settings.grave."+graveName+".boundto", e.getGuildName());
        YAMLManager.getConfig().setMap("settings.grave."+graveName+".cuboidInner", d.serialize());
//        YAMLManager.getConfig().set("settings.grave."+graveName+".cuboidInner2", d.toString());
        d.lowerFill(2, (byte) 0);
        SimpleRectanglePacker SRP = new SimpleRectanglePacker(d);
        SRP.startPacking(2, 4, graveName); // a grave is 2 wide and 4 heigh
        // enlarge cuboid 1 block
        d = d.enlarge(1);
        // set border round graveyard
        d.fillEdges(44,(byte)5);
        // store size in yml
//        YAMLManager.getConfig().setMap("settings.grave."+graveName+".cuboidOuter", d.serialize());
//        YAMLManager.getConfig().set("settings.grave."+graveName+".cuboidOuter2", d.toString());
        // set owershp to system (prevent grieving
        YAMLManager.getConfig().setMap("settings.grave."+graveName+".cuboidOuter", d.serialize());
        d.SetMetaData("GraveOf", "system");
        // roundup
        YAMLManager.getConfig().setLocation("settings.grave."+graveName+".GraveYard.Spawn", e.getSpawnPos());

	}
} 

