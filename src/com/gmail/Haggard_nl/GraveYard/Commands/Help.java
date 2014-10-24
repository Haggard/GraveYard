package com.gmail.Haggard_nl.GraveYard.Commands;

import org.bukkit.entity.Player;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;


public class Help extends MyCommand {

//	private UUIDLibrary ulib = new UUIDLibrary();
	GraveYardMain plugin = GraveYardMain.getInstance();	

	public void onCommand(Player player, String[] args) {

		  MessageManager.getInstance().msgGood(player, " ---- GraveYArd Help ---- ");
		  MessageManager.getInstance().msgInfo(player, " - /gy to show all commands.");
		  MessageManager.getInstance().msgInfo(player, " - ");
		  MessageManager.getInstance().msgInfo(player, " - Admin tools:");
		  MessageManager.getInstance().msgInfo(player, " - create and edit a graveard:");
		  MessageManager.getInstance().msgInfo(player, " - /gy config.");
		  MessageManager.getInstance().msgInfo(player, " - ");
		  MessageManager.getInstance().msgInfo(player, " - To use heardthStone:");
		  MessageManager.getInstance().msgInfo(player, " - open the book");
		  MessageManager.getInstance().msgInfo(player, " - or");
		  MessageManager.getInstance().msgInfo(player, " - /gy goback");
		  MessageManager.getInstance().msgInfo(player, " - return to the place you died.");
		  
	}
	public Help() {
		super("Show help information.", "", "help");
	}
}