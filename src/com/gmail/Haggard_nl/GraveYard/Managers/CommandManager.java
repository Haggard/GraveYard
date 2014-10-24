package com.gmail.Haggard_nl.GraveYard.Managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.Commands.Config;
import com.gmail.Haggard_nl.GraveYard.Commands.GoBack;
import com.gmail.Haggard_nl.GraveYard.Commands.Help;
import com.gmail.Haggard_nl.GraveYard.Commands.MyCommand;

public class CommandManager implements CommandExecutor {

	private ArrayList<MyCommand> cmds = new ArrayList<MyCommand>();
	
	public void setup() {
		cmds.add(new Config());
		cmds.add(new GoBack());
		cmds.add(new Help());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			MessageManager.getInstance().msgBad(sender, "Only players can use Run commands!");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("graveyard")) {
			if (args.length == 0) {
				for (MyCommand mc : cmds) MessageManager.getInstance().msgInfo(p, "/gy " + aliases(mc) + " " + mc.getUsage() + " - " + mc.getMessage());
				return true;
			}
			
			MyCommand c = getCommand(args[0]);
			
			if (c == null) {
				MessageManager.getInstance().msgBad(sender, "That command doesn't exist!");
				return true;
			}
			
			Vector<String> a = new Vector<String>(Arrays.asList(args));
			a.remove(0);
			args = a.toArray(new String[a.size()]);
			c.onCommand(p, args);
			
			return true;
		}
		return true;
	}
	
	private String aliases(MyCommand cmd) {
		String fin = "";
		
		for (String a : cmd.getAliases()) {
			fin += a + " | ";
		}
		
		return fin.substring(0, fin.lastIndexOf(" | "));
	}
	
	private MyCommand getCommand(String name) {
		for (MyCommand cmd : cmds) {
			if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
			for (String alias : cmd.getAliases()) if (name.equalsIgnoreCase(alias)) return cmd;
		}
		return null;
	}
}