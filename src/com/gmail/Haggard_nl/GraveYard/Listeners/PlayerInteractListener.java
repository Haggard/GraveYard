package com.gmail.Haggard_nl.GraveYard.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.Util.MetadataUtil;

public class PlayerInteractListener implements Listener {
  private GraveYardMain plugin;

  public PlayerInteractListener(GraveYardMain p){
    this.plugin = p;
  }

  
  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
	  	MetadataUtil m = new MetadataUtil(this.plugin);
	    final Player player = (Player)event.getPlayer();
	    Chest c;
	    MessageManager.getInstance().debugMessage("chest inventory " + player.getName());

	    if ((event.getInventory().getHolder() instanceof Chest)) {
	    	MessageManager.getInstance().debugMessage("onInventoryOpen chest");
	    	c = (Chest)event.getInventory().getHolder();
	    }else if ((event.getInventory().getHolder() instanceof DoubleChest)){
	    	MessageManager.getInstance().debugMessage("onInventoryOpen Double chest");
	    	DoubleChest dc = (DoubleChest)event.getInventory().getHolder();
	        c = (Chest)dc.getLeftSide();
	    }else{
			MessageManager.getInstance().debugMessage("Not a chest");
	    	return;
	    }
//	    // check if chest is on a graveyard, return if not
//	    if (! plugin.GraveYardManager.locInGraveYard(c.getLocation())){
//	    	return;
//	    }
	    
	    if ( m.getInstance().hasKey(c,"GraveOf")) {
	      String GraveOf = ((MetadataValue)c.getMetadata("GraveOf").get(0)).asString();
	      if (!GraveOf.equalsIgnoreCase(player.getName())) {
	        player.sendMessage(ChatColor.RED + "No grave robbing allowed!");
	        player.sendMessage(ChatColor.GREEN + "You get curset!");
	        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2));
	        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
	        player.setVelocity(player.getLocation().getDirection().multiply(-1));

	        event.setCancelled(true);
	        return;
	      }	else {
	    	  MessageManager.getInstance().debugMessage("Owner OK!!");
	    	  // give player his stuff back 
    	    	MessageManager.getInstance().debugMessage("give player his stuff back");
//	    	        c = (Chest)event.getInventory().getHolder();
    	      if((event.getInventory().getHolder() instanceof Chest)){
    	    	MessageManager.getInstance().debugMessage("single chest");
	        	GraveYardManager.getInstance().putChestsInventoryToPlayer(player, c, null);
    	      } else if((event.getInventory().getHolder() instanceof DoubleChest)) {
    	    	 MessageManager.getInstance().debugMessage("doublechest");
    	        DoubleChest dc = (DoubleChest)event.getInventory().getHolder();
    	        Chest left = (Chest)dc.getLeftSide();
    	        Chest right = (Chest)dc.getRightSide();
    	        GraveYardManager.getInstance().putChestsInventoryToPlayer(player, left, right);
    	      }else{
      			MessageManager.getInstance().debugMessage("Owner ok, not chest or double chest");
 	      	  }
	    	m.getInstance().remove(player, "HasGrave");  
	    	plugin.GraveYardManager.openChest.add(player.getName());
	    }
    }
  }
  
  
  @EventHandler
  public void onInventoryClosed(InventoryCloseEvent e) {
    final Player p = (Player)e.getPlayer();
    
    MessageManager.getInstance().debugMessage("[onInventoryClosed] chest inventory " + p.getName());

    if(	! plugin.GraveYardManager.openChest.contains(p.getName())){
    	return;
    }
    
    if ((e.getInventory().getHolder() instanceof Chest)) {
    	MessageManager.getInstance().debugMessage("onInventoryClosed chest");
    	Chest c = (Chest)e.getInventory().getHolder();
      	if (!emptyChest(p, c).booleanValue()) MessageManager.getInstance().debugMessage("Chest not empty"); 
    } else if ((e.getInventory().getHolder() instanceof DoubleChest)) {
    	MessageManager.getInstance().debugMessage("doublechest");
    	DoubleChest c = (DoubleChest)e.getInventory().getHolder();
    	Chest left = (Chest)c.getLeftSide();
    	Chest right = (Chest)c.getRightSide();
    	if (!emptyChest(p, left).booleanValue()) { MessageManager.getInstance().debugMessage("Left not empty"); return; }
    	if (!emptyChest(p, right).booleanValue()) MessageManager.getInstance().debugMessage("Right not empty"); 
    }else { 
    	return; 
    }
    
    plugin.GraveYardManager.openChest.remove(p.getName());
    
    MessageManager.getInstance().debugMessage("Chest empty start timer");
    
    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
      public void run() {
    	MessageManager.getInstance().debugMessage("Remove grave");
        GraveYardManager.getInstance().removeGrave(p);
      }
    }
    , 60L);
  }

  private Boolean emptyChest(Player p, Chest b)
  {
    if (!isOwner(p, b.getBlock()).booleanValue()) return Boolean.valueOf(false);

    ItemStack[] items = b.getInventory().getContents();
    for (ItemStack item : items) {
      if (item != null) {
    	  MessageManager.getInstance().debugMessage("Chest not empty!!");
        return Boolean.valueOf(false);
      }
    }
    return Boolean.valueOf(true);
  }

  private Boolean isOwner(Player p, Block b){
    if (b.hasMetadata("GraveOf")) {
      String GraveOf = ((MetadataValue)b.getMetadata("GraveOf").get(0)).asString();
      MessageManager.getInstance().debugMessage("[isOwner] p: " + p.getName() + " o: " + GraveOf);
      if (GraveOf.equalsIgnoreCase(p.getName())){
        return Boolean.valueOf(true);
      }
    } else {
    	MessageManager.getInstance().debugMessage("No grave metadata");
    }
    return Boolean.valueOf(false);
  }
}

