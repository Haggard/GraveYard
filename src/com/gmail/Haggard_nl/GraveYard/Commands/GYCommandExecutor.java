package com.gmail.Haggard_nl.GraveYard.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Conversations.HelpDialog;
import com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard.YardManageDialog;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.Util.Utils;


public class GYCommandExecutor implements CommandExecutor {


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
	if(!(sender instanceof Player)){
		return false;
	}
	MessageManager.getInstance().consoleInfo("onCommand " + args.toString());
	
	final Player player = (Player) sender;	
    if (!cmd.getName().equalsIgnoreCase("GraveYardMain"))return true;
    if (args.length != 1){
    	MessageManager.getInstance().msgGood(sender, "GraveYardMain commands:");
    	MessageManager.getInstance().msgInfo(sender, "     /GraveYardMain config  Start GraveYardMain configuration");
    	MessageManager.getInstance().msgInfo(sender, "     /GraveYardMain help    Show help dialog");
    	MessageManager.getInstance().msgInfo(sender, "     /GraveYardMain reload  Reload GraveYardMain plugin ");
    	
    	return false;
    }
    
    if (args[0].equals("config")){
      if (!sender.isPermissionSet("graveyard.config")){
        if (!sender.isOp()) {
        	Bukkit.broadcastMessage("Not allowed to do that");	
        	return false;
        }
      } else if (!sender.hasPermission("graveyard.config")){
          return false;    	  
      }
      if (((sender instanceof Player)) || ((sender instanceof ConsoleCommandSender))){
        ConversationFactory f = GraveYardManager.getInstance().getConversationFactory();
        Conversation c = f.withEscapeSequence("/exit").withFirstPrompt(new YardManageDialog()).buildConversation((Conversable)sender);
        c.begin();
        return true;
      }
    } else {
      if (args[0].equals("reload")){
        if (!sender.isPermissionSet("graveyard.config")){
          if (!sender.isOp())
            return false;
        } else if (!sender.hasPermission("graveyard.config"))
          return false;
      }
      if (args[0].equals("help"))
      {
        if (((sender instanceof Player)) || ((sender instanceof ConsoleCommandSender)))
        {
          ConversationFactory f = GraveYardManager.getInstance().getConversationFactory();
          Conversation c = f.withEscapeSequence("/exit").withFirstPrompt(new HelpDialog()).buildConversation((Conversable)sender);
          c.begin();
          return true;
        }
      }
      if (args[0].equalsIgnoreCase("GoBack")){
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
				return false;
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
	
	    }else {
	        sender.sendMessage(ChatColor.RED + "No command like that."); 
	    }
    }
    return false;
  }
}