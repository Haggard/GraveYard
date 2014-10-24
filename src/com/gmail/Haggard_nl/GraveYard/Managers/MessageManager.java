package com.gmail.Haggard_nl.GraveYard.Managers;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;


public class MessageManager {

	public enum MessageType {
		
		INFO(ChatColor.GRAY),
		GOOD(ChatColor.GOLD),
		BAD(ChatColor.RED);
		
		private ChatColor color;


		MessageType(ChatColor color) {
			this.color = color;
		}
		
		public ChatColor getColor() {
			return color;
		}
	}

	Logger log = Logger.getLogger("Minecraft");

	private MessageManager() { }
	
	private static MessageManager instance = new MessageManager();
	
	public static MessageManager getInstance() {
		return instance;
	}
	
	private String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "GraveYard" + ChatColor.GRAY + "] " + ChatColor.RESET;


	public void log(String msg) {
			this.log.info(msg);
	}
	
	public void sendRawMessage(Player player, String msg){
		player.sendRawMessage(msg);
	}
	
	public void msg(CommandSender sender, MessageType type, String... messages) {
		for (String msg : messages) {
			sender.sendMessage(prefix + type.getColor() + msg);
		}
	}
	public void msg2(CommandSender sender, MessageType type, String... messages) {
		for (String msg : messages) {
			sender.sendMessage(type.getColor() + msg);
		}
	}

	public void msgGood(CommandSender sender, String... messages) {
			msg2(sender, MessageType.GOOD, messages);
	}

	public void msgBad(CommandSender sender, String... messages) {
		msg2(sender, MessageType.BAD, messages);
	}

	public void msgInfo(CommandSender sender, String... messages) {
		msg2(sender, MessageType.INFO, messages);
	}

	public void broadcast(MessageType type, String... messages) {
		log("players " + Bukkit.getServer().getOnlinePlayers().length);
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			log("1 - " + p.getName());
			for (String msg : messages) {
				log("1 - " + p.getName());
				p.sendMessage(prefix + type.getColor() + msg);
			}
		}
	}
	
	//Displays a welcome message to the user
	public String welcomeMessage(String playerName) {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "Welcome " + playerName + "!";
	}
	
	//Public message to display when player disconnects
	public String disconnectMessage(String playerName) {
		return ChatColor.RED + "" + ChatColor.BOLD + playerName + " disconnected!";
	}
	
	//Displays a random tip about the current miniGame
	public void randomTip(String randomTip) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + " TIP >> " + randomTip);
		}
	}
	
	//Show Debug Message
	public void debugMessage(String debugMsg) {
		if(GraveYardMain.isDebugMessages() == true) {
			Bukkit.broadcastMessage(ChatColor.RED + "[GraveYard} <<Debug>> " + ChatColor.BOLD + debugMsg);
		}
	}
		
	// send colored messages to console
    public static void printlnfx(String message) {
          Server server = Bukkit.getServer();
          ConsoleCommandSender console = server.getConsoleSender();
          console.sendMessage(message);
    }
	
    public static void consoleInfo(String message){
    	printlnfx(ChatColor.BLUE + "[GY info] " + message);
    }
    public static void consoleError(String message){
    	printlnfx(ChatColor.RED + "[GY error] " + message);
    }
    public static void consoleGood(String message){
    	printlnfx(ChatColor.GREEN + "[GY good] " + message);
    }

    public static void consoleInfoMsg(String message){
    	printlnfx(ChatColor.BLUE + message);
    }
    public static void consoleErrorMsg(String message){
    	printlnfx(ChatColor.RED + message);
    }
    public static void consoleGoodMsg(String message){
    	printlnfx(ChatColor.GREEN + message);
    }
	
	public void inArenaMessage(List<Player> players, String message) {
		for(Player p : players) {
			p.sendMessage(message);
		}
	}



}
