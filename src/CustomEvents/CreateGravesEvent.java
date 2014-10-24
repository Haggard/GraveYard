package CustomEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.Location;
 
/**
*
* An Event that fires when a player Selects something with the World sniper wand
*
* @see Event
*
*/
public class CreateGravesEvent extends Event implements Cancellable {
 
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	String p; // Player Stored as a string (the players name) to prevent memory leak
	Location pos1;
	Location pos2;
	Location spawn_pos;
	String guildName;
	 
	/**
	* Constructor of a WorldSniperWandSelectionEvent * * Between pos1 and pos2 is the selection *
	* @param p - Player that is selecting Something WIth the wand * @param pos1 - The First Position
	* @param pos2 - The Second Position
	*/
	public CreateGravesEvent(Player p, Location pos1, Location pos2, Location spawn_pos, String guildname) {
		this.p = p.getName();
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.guildName = guildname;
		this.spawn_pos = spawn_pos;
		this.cancelled = false;
	}
	 
	public Location getPos1() {
		return pos1;
	}
	 
	public Location getPos2() {
		return pos2;
	}
	
	public Location getSpawnPos() {
		return spawn_pos;
	}

	public String getPlayerName() {
		return p;
	}

	public String getGuildName() {
		return guildName;
	}

	public Player getPlayer() throws NullPointerException {
		return Bukkit.getServer().getPlayer(p);
	}
	 
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	 
	public static HandlerList getHandlerList() {
		return handlers;
	}
	 
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	 
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	 
	 
}