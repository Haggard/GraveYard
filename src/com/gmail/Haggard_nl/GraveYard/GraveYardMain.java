package com.gmail.Haggard_nl.GraveYard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import CustomListeners.GravesEventListener;

import com.gmail.Haggard_nl.GraveYard.Commands.GYCommandExecutor;
import com.gmail.Haggard_nl.GraveYard.Listeners.BlockBreakListener;
import com.gmail.Haggard_nl.GraveYard.Listeners.LoginListener;
import com.gmail.Haggard_nl.GraveYard.Listeners.PlayerInteractListener;
import com.gmail.Haggard_nl.GraveYard.Listeners.PlayerRespawnListener;
import com.gmail.Haggard_nl.GraveYard.Listeners.onPlayerDeathListener;
import com.gmail.Haggard_nl.GraveYard.Managers.CommandManager;
import com.gmail.Haggard_nl.GraveYard.Managers.GraveYardManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.ServerCore.Permission.Permission;
import com.gmail.Haggard_nl.ServerCore.RanksAndLevels.RanksAndLevels;



public class GraveYardMain extends JavaPlugin {
  public static final int prefix = 0;
  File configFile;
  public boolean debug = false;
  FileConfiguration config;


  public static Permission perms = null;
  private static GraveYardMain instance ;
  private static boolean isDebugging = false;
  public static final String Version = "1.0";
  public static RanksAndLevels PandG_api = null;

  public onPlayerDeathListener PDL = new onPlayerDeathListener(this);
  public BlockBreakListener BBL = new BlockBreakListener(this);
  public PlayerInteractListener PIL = new PlayerInteractListener(this);
  public PlayerRespawnListener PRL = new PlayerRespawnListener(this);
  public LoginListener LL = new LoginListener(this);
  public GravesEventListener GEL = new GravesEventListener(this);
  public static GraveYard_API API = null;
  public GraveYardManager GraveYardManager;

  public void onDisable() {
  }

  public void onEnable() {
  
    this.GraveYardManager = new GraveYardManager();
    
    setupAPI();
    instance = this;
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(PDL, this);
    pm.registerEvents(BBL, this);
    pm.registerEvents(PIL, this);
    pm.registerEvents(PRL, this);
    pm.registerEvents(LL, this);
    pm.registerEvents(GEL, this);
    
    
    
    if (!setupPermissions()) {
      MessageManager.getInstance().log(String.format("[%s] - Disabled due to no dependency found!", new Object[] { getDescription().getName() }));
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
	if (!setupPandG() ) {
		MessageManager.consoleError(String.format("[%s] - Disabled PartiesAndGuilds support due to no dependency found!", getDescription().getName()));
		PandG_api = null;
		return;
	}
    isDebugging = YAMLManager.getConfig().getBoolean("Debug.enabled");

   	CommandManager cm = new CommandManager();
   	cm.setup();
   	getCommand("GraveYard").setExecutor(cm);    
    GraveYardManager.loadDeads();
    

  }
  

  public static Plugin getPlugin() {
    return Bukkit.getServer().getPluginManager().getPlugin("GraveYardMain");
  }

  private boolean setupPermissions() {
    RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
    perms = (Permission)rsp.getProvider();
    return perms != null;
  }

	public Boolean setupPandG() {
	    RegisteredServiceProvider<RanksAndLevels> rsp = getServer().getServicesManager().getRegistration(RanksAndLevels.class);
	    PandG_api = (RanksAndLevels)rsp.getProvider();
	    return PandG_api != null;
	}
 
//Functions
  public Boolean useGraveWorld(String wn){
	  List<String> l =  YAMLManager.getConfig().getStringList("settings.noGraveWorld");
	  if (l == null) { 
		  MessageManager.getInstance().debugMessage("noGraveWorld for " + wn + " null!");
		  return true; }
	  if(l.size() > 0){
		  for (String w: l){
			  if (w.equalsIgnoreCase(wn) || (w.toLowerCase() == wn.toLowerCase())) return false;
		  }
	  }
	  MessageManager.getInstance().debugMessage("World " + wn + " not found!");
	  return true;
  }
	    
    public Vector Loc2Vector(Location loc){
 	   return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }
    
    public String Vector2String(Vector vect){
 	   return "" + vect.getX() + vect.getY() + vect.getZ();
    }
    public String getPos(Location loc){
 	   return "" + (int)loc.getX() + (int)loc.getZ();
    }
    
    public int getHeight(Location loc){
 	   return (int)loc.getY();
    }

    public static GraveYardMain getInstance() {
        return instance;
    }

    public static boolean isDebugMessages() {
		return isDebugging;
	}

	public static String getPluginVersion() {
		return Version;
	}
	
	public void setupAPI(){
		API = new GraveYard_API(this);
//		Bukkit.getServicesManager().register(API.class, api, this, ServicePriority.Normal);
	}
}
