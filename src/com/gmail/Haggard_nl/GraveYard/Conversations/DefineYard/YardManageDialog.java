package com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.Conversations.GeneralConfigDialog;
import com.gmail.Haggard_nl.Util.Utils;

public class YardManageDialog extends ValidatingPrompt
{
  private final String[] options = { "create", "undo", "spawn", "facing" };

@Override
  public String getPromptText(ConversationContext context)
  {
    context.getForWhom().sendRawMessage(ChatColor.GREEN + "---- GaveYard configuration ----");
   context.getForWhom().sendRawMessage(ChatColor.GREEN + "What do you want to do?");
    return ChatColor.AQUA + "create, undo, spawn, facing, exit";
  }
@Override
  protected Prompt acceptValidatedInput(ConversationContext context, String input)
  {
    if (input.equals("create")){
      return new YardCreateDialog();
    }
    if (input.equals("spawn")){
      return new YardSpawnDialog();
    }
    if (input.equals("facing")){
    	Player p = (Player) context.getForWhom();
    	context.getForWhom().sendRawMessage(ChatColor.BLUE +"------------------------------------------------------");
    	context.getForWhom().sendRawMessage(ChatColor.GREEN + "Cardinal " + Utils.getCardinalDirection(p));
    	context.getForWhom().sendRawMessage(ChatColor.GREEN + "Direction " + Utils.getDirection(p));
    	
      return this;
    }
    if (input.equals("undo")){
      return new YardRemoveDialog();
    }
    return END_OF_CONVERSATION;
  }

@Override
  protected boolean isInputValid(ConversationContext arg0, String arg1)
  {
    if (arg1.contains(" ")){
      String[] splitt = arg1.split("\\ ");
      if (!splitt[0].equals("list")){
        return false;
      }
      if (splitt.length > 2) {
        return false;
      }
      try {
        Integer.parseInt(splitt[1]);
        return true;
      } catch (Exception e) {
        return false;
      }
    }

    for (String option : this.options){
      if (option.equals(arg1)){
        return true;
      }
    }
    return false;
  }
}