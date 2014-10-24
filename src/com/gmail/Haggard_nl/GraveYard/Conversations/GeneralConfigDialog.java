package com.gmail.Haggard_nl.GraveYard.Conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Conversations.DefineYard.YardManageDialog;

public class GeneralConfigDialog extends FixedSetPrompt
{
  public GeneralConfigDialog()
  {
    super(new String[] { "new","edit", "remove", "help", "info", "exit" });
  }

  public String getPromptText(ConversationContext arg0)
  {
    arg0.getForWhom().sendRawMessage(ChatColor.GREEN + "Welcome to the configuration of GraveYardMain! You can quit this at any point by saying '/exit' in the chat.");
    arg0.getForWhom().sendRawMessage(ChatColor.YELLOW + "Note: While you're in this configuration, you can't chat!");
    arg0.getForWhom().sendRawMessage(ChatColor.GREEN + "What do you want to manage?");
    return ChatColor.AQUA + "new, edit, remove, info, help, exit";
  }
  @Override
  protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
  {
    if (arg1.equals("game"))
      return new YardManageDialog();
    if (arg1.equals("help"))
      return new HelpDialog();
    if (arg1.equals("info")){
      arg0.getForWhom().sendRawMessage(ChatColor.GOLD + "----------------------------------------------");
      arg0.getForWhom().sendRawMessage(ChatColor.GOLD + "  Running GraveYardMain v" + GraveYardMain.getInstance().getDescription().getVersion());
      arg0.getForWhom().sendRawMessage(ChatColor.GOLD + "  Made by Haggard.nl");
      arg0.getForWhom().sendRawMessage(ChatColor.GOLD + "  (c)2014");
      arg0.getForWhom().sendRawMessage(ChatColor.GOLD + "----------------------------------------------");
      return this;
    }

    return END_OF_CONVERSATION;
  }
}