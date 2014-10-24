package com.gmail.Haggard_nl.GraveYard.Conversations;


import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

import com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard.YardHelpDialog;

public class HelpDialog extends FixedSetPrompt
{
  public HelpDialog()
  {
    super(new String[] { "games" });
  }

  public String getPromptText(ConversationContext arg0)
  {
    arg0.getForWhom().sendRawMessage(ChatColor.GREEN + "What do you need help with?");
    return ChatColor.AQUA + "games, system";
  }

  protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
  {
    if (arg1.equals("GraveYardMain"))
      return new YardHelpDialog();
/*    if (arg1.equals("npcs"))
      return new NPCHelpDialog();
    if (arg1.equals("triggers"))
      return new TriggerHelpDialog();
    if (arg1.equals("events"))
      return new EventHelpDialog();
*/    return this;
  }
}