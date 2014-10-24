package com.gmail.Haggard_nl.GraveYard.Listeners;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.com.mojang.authlib.exceptions.AuthenticationException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.Util.MetadataUtil;

public class onPlayerDeathListener implements Listener{
  private GraveYardMain plugin;
  private MetadataUtil m;
  
  public onPlayerDeathListener(GraveYardMain instance){
    this.plugin = instance;
	m = new MetadataUtil(this.plugin);

  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
	MessageManager.getInstance().debugMessage(" onPlayerDeath");
    if (!(event.getEntity() instanceof Player)){
    	return;
    }
    Player player = event.getEntity();
    if (this.plugin.useGraveWorld(player.getWorld().getName())) {
    	if(!GraveYardManager.getInstance().playerCanBeBurried(player.getName())){
        	m.getInstance().set(player, "HasGrave", Boolean.valueOf(false));
        	// remove droppings when using a grave
        	event.getDrops().clear(); 
    	}
    	MessageManager.getInstance().debugMessage("it's still working..");
    	createHearthBook(player);
    	String Yard = GraveYardManager.getInstance().getGarvePlayer(player.getName());
    	if (Yard == null){
	    	GraveYardManager.getInstance().setGrave(player);
    	}else{
    		String Yard_and_plotNr = GraveYardManager.getInstance().getPlayersGarveId(player.getName());
    		GraveYardManager.getInstance().fillExistingGrave(player, Yard_and_plotNr);
    	}
    	m.getInstance().set(player, "HasGrave", Boolean.valueOf(true));
    	// remove droppings when using a grave
    	event.getDrops().clear(); 
    } else {
    	m.getInstance().remove(player, "HasGrave");
    	MessageManager.getInstance().debugMessage("world in list so Graves off");
    }
  }

  
  @SuppressWarnings("deprecation")
private void createHearthBook(Player player){
    List<String> pages = new ArrayList<String>();
    pages.add("Return to the location where you died. \n\ncommand = /gy goback");
    pages.add("");
    pages.add("world=" + player.getWorld().getName() + "; x=" + player.getLocation().getX() + "; y=" + player.getLocation().getY() + "; z=" + player.getLocation().getZ() + "; pitch=" + player.getLocation().getPitch() + "; yaw=" + player.getLocation().getYaw());
    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bm = (BookMeta)book.getItemMeta();
    bm.setTitle("Hearthstone");
    bm.setAuthor("ScrollBook");
    bm.setPages(pages);
    book.setItemMeta(bm);
    player.getInventory().addItem(new ItemStack[] { book });
    player.updateInventory();
    m.getInstance().set(player, "HasHearthstone", Boolean.valueOf(true));
  }
}
