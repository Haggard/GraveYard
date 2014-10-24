package com.gmail.Haggard_nl.GraveYard.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.Util.Utils;


public class GoBack extends MyCommand {

//	private UUIDLibrary ulib = new UUIDLibrary();
	GraveYardMain plugin = GraveYardMain.getInstance();	

	public void onCommand(final Player player, String[] args) {
		ItemStack book = null;
		ItemStack[] items = player.getInventory().getContents().clone();
		int slot = 0;
		BookMeta bm = null;
		String title = null;
		for (ItemStack i : items) {
			if (i != null) {
				if (i.getType() == Material.WRITTEN_BOOK) {
					ItemStack s = new ItemStack(i);
					bm = (BookMeta) s.getItemMeta();

					MessageManager.getInstance().debugMessage("Title " + bm.getTitle());
					MessageManager.getInstance().debugMessage("Author " + bm.getAuthor());
					MessageManager.getInstance().debugMessage("Pages " + bm.getPageCount());
					title = bm.getTitle();
					if(title.equalsIgnoreCase("Hearthstone")){	
						book = i.clone();
						break;
					}else{
						title = null;
					}
				}
			}
			slot++;
		}

		if (title == null) {
			MessageManager.getInstance().debugMessage("Book is null");
			MessageManager.getInstance().msgInfo(player, "You need a Hearthstone to use this command!");
			return;
		}

		int scrollSize = bm.getPageCount();
		String[] l = bm.getPage(3).split(";");
		String locStr = "";
		for (String v : l){
			String ls[] =v.split("="); 
			locStr = locStr + ls[1] + ";";
		}
		player.getInventory().removeItem(book);
		player.updateInventory();
	  final Location fd = Utils.string2Location(locStr);
	
	  player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 2));
	  player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3));
	  player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300, 3));
	
	  Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GraveYardMain.getInstance(), new Runnable() {
	    public void run() {
	      player.teleport(fd);
	    }
	  }
	  , 60L);  

    }		  

	public GoBack() {
		super("Gobakc to the place you died.", "", "GoBack");
	}
}