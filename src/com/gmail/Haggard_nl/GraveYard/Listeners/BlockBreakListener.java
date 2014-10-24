package com.gmail.Haggard_nl.GraveYard.Listeners;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;

public class BlockBreakListener implements Listener {
  private GraveYardMain plugin;

  public BlockBreakListener(GraveYardMain instance){
    this.plugin = instance;
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Block b = event.getBlock();
    Player player = event.getPlayer();

    if (b.hasMetadata("GraveOf")) {
      String GraveOf = ((MetadataValue)b.getMetadata("GraveOf").get(0)).asString();
      MessageManager.getInstance().debugMessage(GraveOf);
      if (GraveOf.equalsIgnoreCase("system")) {
          player.sendMessage(ChatColor.RED + "No use of digging here.");
          event.setCancelled(true);
          return;    	  
      } else if (!GraveOf.equalsIgnoreCase(player.getName())) {
        player.sendMessage(ChatColor.RED + "No grave robbing allowed!");
        player.sendMessage(ChatColor.GREEN + "You get curset!");
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
        player.setVelocity(player.getLocation().getDirection().multiply(-1));

        event.setCancelled(true);
        return;
      }
    }else{
        MessageManager.getInstance().debugMessage("No GraveOf metadata");

    }
  }
  
}
