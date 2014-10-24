package com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import CustomEvents.CreateGravesEvent;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.Cuboid;
import com.gmail.Haggard_nl.Util.Utils;


public class YardCreateDialog extends ValidatingPrompt {
	
  @Override	
  public String getPromptText(ConversationContext context){
    if (context.getSessionData("grave_pos1") == null && context.getSessionData("grave_rows") == null){
      return ChatColor.GREEN + "Please enter pos1 to set the first loaction or give the amount of grave rows:";
    }
    if (context.getSessionData("grave_pos2") == null && context.getSessionData("grave_columns") == null){
        return ChatColor.GREEN + "Please enter pos2 to set the second loaction or give the amount of grave columns:";
    }
    if (context.getSessionData("spawn_pos") == null){
        return ChatColor.GREEN + "Please specify the player spawn position. You can insert 'here' to use the current location.";
      }
    if (context.getSessionData("guildname") == null){
        return ChatColor.GREEN + "Please enter a guildname. Enter 'none' to leave unused.";
      }
    return ChatColor.GREEN + "Please enter preview to preview or done to finalize";
  }
  
  @Override
  protected Prompt acceptValidatedInput(ConversationContext context, String input){

    if (input.equalsIgnoreCase("cancel")){
      context.setSessionData("grave_pos1", null);
      context.setSessionData("grave_pos2", null);
      context.setSessionData("grave_rows", null);
      context.setSessionData("grave_columns", null);
      context.setSessionData("spawn_pos", null);
      context.setSessionData("guildname", null);
      context.setSessionData("preview", null);
      context.setSessionData("set_done", null);
      return new YardManageDialog();
    } else if (context.getSessionData("grave_pos1") == null && context.getSessionData("grave_rows") == null){
        if (input.equalsIgnoreCase("back")){
        	return new YardManageDialog();
        }
        if (input.equalsIgnoreCase("pos1")){
        	Player p = (Player)context.getForWhom();
        	context.setSessionData("grave_pos1", Utils.location2String(p.getLocation()));
        }else {
        	Player p = (Player)context.getForWhom();
        	context.setSessionData("grave_pos1", Utils.location2String(p.getLocation()));
        	context.setSessionData("grave_rows", Double.parseDouble(input));
        }
   }else if (context.getSessionData("grave_pos2") == null && context.getSessionData("grave_columns") == null ){
        if (input.equalsIgnoreCase("back")){
            context.setSessionData("grave_pos1", null);
            context.setSessionData("grave_rows", null);
            return this;
        }
        Player p = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("pos2")){
            context.setSessionData("grave_pos2", Utils.location2String(p.getLocation()));
        }else {
          	context.setSessionData("grave_columns", Double.parseDouble(input));
          	Location pos1 = Utils.string2Location((String) context.getSessionData("grave_pos1"));
          	Double x = (Double) context.getSessionData("grave_rows") * 4;
          	Double z = (Double) context.getSessionData("grave_columns") * 2;
          	Location pos2 = pos1;
          	
          	String facing = Utils.getDirection(p);
          	if(facing.equalsIgnoreCase("North")){
          		context.getForWhom().sendRawMessage(ChatColor.GREEN + "North");
          		pos2 = pos2.subtract(x, 0, z);
          	}
          	if(facing.equalsIgnoreCase("South")){
          		context.getForWhom().sendRawMessage(ChatColor.GREEN + "South");
          		pos2 = pos2.add(x, 0, z);
          	}
          	if(facing.equalsIgnoreCase("East")){
          		context.getForWhom().sendRawMessage(ChatColor.GREEN + "East");
          		pos2 = pos2.add(x, 0, 0);
          		pos2 = pos2.subtract(0, 0, z);
         	}
          	if(facing.equalsIgnoreCase("West")){
          		context.getForWhom().sendRawMessage(ChatColor.GREEN + "West");
          		pos2 = pos2.subtract(x, 0, 0);
          		pos2 = pos2.add(0, 0, z);
          	}
          	context.setSessionData("grave_pos2", Utils.location2String(pos2));
        }
  
        
    }else if (context.getSessionData("spawn_pos") == null){
        if (input.equalsIgnoreCase("back")){
            context.setSessionData("grave_pos2", null);
            context.setSessionData("grave_columns",null);
            return this;
        }
        if (input.equalsIgnoreCase("here")){
            Player p = (Player)context.getForWhom();
            context.setSessionData("spawn_pos", Utils.location2String(p.getLocation()));
        }else {       	
            return this;
        }   
    }else if (context.getSessionData("guildname") == null){	
        if (input.equalsIgnoreCase("back")){
            context.setSessionData("spawn_pos", null);
            return this;
        }
        if (input.equalsIgnoreCase("none")){
	        if(GraveYardMain.getInstance().GraveYardManager.hasGeneralGraveYard()){
	    		context.getForWhom().sendRawMessage(ChatColor.GREEN + "General Graveyard already set. Give a guidname or quit!");
	    		context.setSessionData("guildname", null);
	    		return this;
	    	}else{
		        context.setSessionData("guildname", "");
	    	}
        } else if (GraveYardMain.PandG_api.isGuildnameVaild(input)){
            context.setSessionData("guildname", input);
        }else {
    		context.getForWhom().sendRawMessage(ChatColor.GREEN + "Guildname " + input + " not found. Give a valid guidname, 'none' or quit!");
    		context.setSessionData("guildname", null);
    		return this;
        }        	
    }else if (context.getSessionData("set_done") == null){
        if (input.equalsIgnoreCase("back")){
            context.setSessionData("guildname", null);
            return this;
        }
        if (input.equalsIgnoreCase("preview")){
 	  	    Location pOne = Utils.string2Location((String)context.getSessionData("grave_pos1")).add(0,1,0);
		    Location pTwo = Utils.string2Location(((String) context.getSessionData("grave_pos2"))).add(0,2,0);
            final Cuboid cb = new Cuboid(pOne, pTwo);

            	cb.setPreview(20,  (byte) 0);
            Bukkit.getScheduler().runTaskLater(GraveYardMain.getInstance(), new Runnable() {
				 
				@Override
				public void run() {
					cb.resetPreview(20,  (byte) 0);
				}
			}, YAMLManager.getConfig().getInt("settings.previewInSec") * 20);

            return this;
        }
        if (input.equalsIgnoreCase("redefine")){
        	context.setSessionData("grave_pos2", null);
        	context.setSessionData("grave_pos2", null);
        	return this;
        }
        if (!(input.equalsIgnoreCase("done"))){
            return this;
        }     
	      context.setSessionData("set_done", "true"); 

	      Location pointOne = Utils.string2Location((String)context.getSessionData("grave_pos1"));
	      Location pointTwo = Utils.string2Location((String)context.getSessionData("grave_pos2"));
	      Location spawn_pos = Utils.string2Location((String)context.getSessionData("spawn_pos"));
	      String guildName = (String)context.getSessionData("guildname");
	
	      /**
	       *  Trigger custom event from here and create a listener to create the grave
	       */
	      Player p = (Player)context.getForWhom();
	
	      CreateGravesEvent e = new CreateGravesEvent(p, pointOne, pointTwo, spawn_pos, guildName);
	      
	    //Calls The Event
	    	p.getServer().getPluginManager().callEvent(e);
	     
	    //Checks If the event was cancelled by any other plugin or in another part of this plugin
	    if(!e.isCancelled()) {
	    //OTHER SELECTION STUFF
	    	p.sendMessage("Sucessfully Created");
	    } else {
	    	p.sendMessage("Sorry Graveyard Not created");
	    	return END_OF_CONVERSATION;
	    }
	  
	  context.getForWhom().sendRawMessage(ChatColor.GREEN + "-----------------------------------------------------");
      context.getForWhom().sendRawMessage(ChatColor.GREEN + "                 GraveYardMain created!");
      context.getForWhom().sendRawMessage(ChatColor.GREEN + "-----------------------------------------------------");
      context.setSessionData("grave_pos1", null);
      context.setSessionData("grave_pos2", null);
      context.setSessionData("grave_rows", null);
      context.setSessionData("grave_columns", null);
      context.setSessionData("spawn_pos", null);
      context.setSessionData("guildname", null);
      context.setSessionData("set_done", null);

     return END_OF_CONVERSATION; //new GameManageDialog();
    }
    return this;
  }
	@Override
  protected boolean isInputValid(ConversationContext context, String input){
    if ((input.equalsIgnoreCase("back")) || (input.equalsIgnoreCase("cancel"))){
    	return true; 
    }
    if (context.getSessionData("grave_pos1") == null && context.getSessionData("grave_rows") == null){
        if (!input.equalsIgnoreCase("pos1")) {
        	try{
        	   Double i  = Double.parseDouble(input);
        	   context.getForWhom().sendRawMessage("r = true");
        	   return true;
        	} catch (Exception e){
        	   return false;
        	}
        }
        return true;
    } 
    if (context.getSessionData("grave_pos2") == null && context.getSessionData("grave_columns") == null) {
        if (!input.equalsIgnoreCase("pos2")) {
        	try{
        		Double i  = Double.parseDouble(input);
        	   context.getForWhom().sendRawMessage("c = true");

         	   return true;
         	} catch (Exception e){
         	   return false;
         	}
        }
        return true;
    }
    return true;
  } 

}
