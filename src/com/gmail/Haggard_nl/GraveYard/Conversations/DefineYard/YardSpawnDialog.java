package com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard;

import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.Cuboid;
import com.gmail.Haggard_nl.Util.SimpleRectanglePacker;
import com.gmail.Haggard_nl.Util.Utils;

public class YardSpawnDialog extends ValidatingPrompt {
	  @Override	
	  public String getPromptText(ConversationContext context){
	    if (context.getSessionData("arena_pos1") == null){
	    	return ChatColor.GREEN + "Please enter pos1 to set the first loaction of the arena:";
	    }
	    return ChatColor.GREEN + "Type 'done' when ready";
	  }
	  
	  @Override
	  protected Prompt acceptValidatedInput(ConversationContext context, String input){

	    if (input.equals("cancel")){
	      context.setSessionData("arena_pos1", null);
	      context.setSessionData("set_done", null);
	      return new YardManageDialog();
	    } else if (context.getSessionData("arena_pos1") == null){
	        if (input.equals("back")){
	        	return new YardManageDialog();
	        }
	        if (input.equals("pos1")){
	        	Player p = (Player)context.getForWhom();
	        	context.setSessionData("arena_pos1", p.getLocation());
	        }else {
	        	context.setSessionData("arena_pos1", Utils.string2Location(input));
	        }
	    }else if (context.getSessionData("set_done") == null){
	        if (input.equals("back")){
	            context.setSessionData("arena_pos2", null);
	            return this;
	        }
	      context.setSessionData("set_done", "true");  
	      Location pointOne = (Location)context.getSessionData("arena_pos1");
	      YAMLManager.getConfig().setLocation("settings.GraveYard.Spawn", pointOne);
	      context.getForWhom().sendRawMessage(ChatColor.GREEN + "-----------------------------------------------------");
	      context.getForWhom().sendRawMessage(ChatColor.GREEN + "                 GraveYardMain Spawn Set!");
	      context.setSessionData("arena_pos1", null);
	      context.setSessionData("set_done", null);  
	     return END_OF_CONVERSATION; //new GameManageDialog();
	    }
	    return this;
	  }
		@Override
	  protected boolean isInputValid(ConversationContext context, String input){
	    if ((input.equals("back")) || (input.equals("cancel"))){ return true; }
	    
	    if (context.getSessionData("arena_pos1") == null){
	        if (!input.equals("pos1")) {
	          if (Utils.string2Location(input) == null) {
	            context.getForWhom().sendRawMessage(ChatColor.RED + "Invalid location data pos1.");
	            return false;
	          }
	          return true;
	        }
	    }
	    return true;
	  }  
	}
