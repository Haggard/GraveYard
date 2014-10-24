package com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import CustomEvents.RemoveGravesEvent;

import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.Cuboid;




public class YardRemoveDialog extends ValidatingPrompt {

	@Override	
	public String getPromptText(ConversationContext context) {
/*    if (context.getSessionData("arena_name") == null) {
      return ChatColor.GREEN + "Please enter the name of the arena to remove:";
    }
*/    return ChatColor.YELLOW + "Are you sure you want to delete the GraveYardMain? Write 'delete' in the chat to confirm.";
	}
	
	  @Override
	  protected Prompt acceptValidatedInput(ConversationContext context, String input) {
	    if (input.equals("cancel")){
	      context.setSessionData("arena_name", null);
	      return new YardManageDialog();
	    }
	    
	    
	    if (input.equals("delete")) {
	        try {
	        	// check if player is on a GraveYardMain
	            if(YAMLManager.getConfig().ContainsKey("settings.grave")){
	            	for(String graveyardId : YAMLManager.getConfig().getConfigurationSection("settings.grave")){
	            		Cuboid d = new Cuboid(YAMLManager.getConfig().getMap("settings.grave."+graveyardId+".cuboidInner"));
	
	            		if(d.contains(((Player) context.getForWhom()).getLocation())){
	            		      Player p = (Player) context.getForWhom();
	            		      RemoveGravesEvent e = new RemoveGravesEvent(p, graveyardId, d.getLowerX(), d.getLowerY(), d.getLowerZ());
	            		      //Calls The Event
	            		      p.getServer().getPluginManager().callEvent(e);
	            		       
	            		      //Checks If the event was cancelled by any other plugin or in another part of this plugin
	            		      if(!e.isCancelled()) {
	            		      //OTHER SELECTION STUFF
	            		      p.sendMessage("Sucessfully removed Graveyard");
	            		      } else {
	            		      p.sendMessage("Sorry removing Graveyard didn't succeed!");
	            		      }
	            			return END_OF_CONVERSATION;
	                    }
	            	}
	            	context.getForWhom().sendRawMessage("Nothing to undo. You're not standing on a GraveYardMain!");
	            	return END_OF_CONVERSATION;
	            }
	        }catch(Exception e) {
	           	context.getForWhom().sendRawMessage("[Error] An error occured removing a GraveYardMain!");
	            return END_OF_CONVERSATION;
	        }
	    }
	    return this;        	
	  }

	@Override
	protected boolean isInputValid(ConversationContext context, String input){
	    if ((input.equals("back")) || (input.equals("cancel"))){
	    	return true; 
	    }
	    if ((input.equals("del")) || (input.equals("delete"))){
	    	return true; 
	    }
		return false;
	}
}