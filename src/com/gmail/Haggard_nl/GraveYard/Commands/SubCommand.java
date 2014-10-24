package com.gmail.Haggard_nl.GraveYard.Commands;


import org.bukkit.entity.Player;

public abstract interface SubCommand
{

    public abstract boolean onCommand(Player player, String[] args);
    
}