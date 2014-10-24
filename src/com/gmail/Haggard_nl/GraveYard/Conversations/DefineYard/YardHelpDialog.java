package com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

import com.gmail.Haggard_nl.GraveYard.Conversations.HelpDialog;

public class YardHelpDialog extends FixedSetPrompt
{
  public YardHelpDialog()
  {
    super(new String[] { "info", "safe word", "back" });
  }

  public String getPromptText(ConversationContext arg0)
  {
    arg0.getForWhom().sendRawMessage(ChatColor.GREEN + "About what do you want some information?");
    return ChatColor.AQUA + "info, safe word, back";
  }

  protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
  {
    if (arg1.equals("back"))
      return new YardManageDialog(); //HelpDialog();
    if (arg1.equals("info"))
      arg0.getForWhom().sendRawMessage(ChatColor.YELLOW + "<Description needed>");
    else if (arg1.equals("safe word"))
      arg0.getForWhom().sendRawMessage(ChatColor.AQUA + "The safe word can be used by players inside a dungeon to stop or pause the dungeon.");
    return this;
  }
}