package com.gmail.Haggard_nl.GraveYard.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard.YardManageDialog;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;


public class Config extends MyCommand {

//	private UUIDLibrary ulib = new UUIDLibrary();
	GraveYardMain plugin = GraveYardMain.getInstance();	

	public void onCommand(Player player, String[] args) {

	      if (!player.isPermissionSet("graveyard.config")){
	          if (!player.isOp()) {
	        	  MessageManager.getInstance().msgBad(player, "Not allowed to do that");	
	          	return;
	          }
	        } else if (!player.hasPermission("graveyard.config")){
	            return ;    	  
	        }
	        if (((player instanceof Player)) || ((player instanceof ConsoleCommandSender))){
	          ConversationFactory f = GraveYardManager.getInstance().getConversationFactory();
	          Conversation c = f.withEscapeSequence("/exit").withFirstPrompt(new YardManageDialog()).buildConversation((Conversable)player);
	          c.begin();
	          return;
	        }
		  
	}
	public Config() {
		super("Configurate a GraveYard.", "", "config");
	}
}