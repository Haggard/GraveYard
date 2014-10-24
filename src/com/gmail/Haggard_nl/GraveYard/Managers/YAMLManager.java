package com.gmail.Haggard_nl.GraveYard.Managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;


public class YAMLManager {
    static Logger log = Logger.getLogger("Minecraft");

	private static YAMLManager configuration = new YAMLManager("config");
	private static YAMLManager graveyardConfig = new YAMLManager("graveyard");
	
	public static YAMLManager getConfig() {
		return configuration;
	}
	public static YAMLManager getGraveYardConfig() {
		return graveyardConfig;
	}

	/*****/
	
	private YAMLManager(String fileName) {

		if (!GraveYardMain.getInstance().getDataFolder().exists()){
			GraveYardMain.getInstance().getDataFolder().mkdir();
		}
		file = new File(GraveYardMain.getInstance().getDataFolder(), fileName + ".yml");
		
		if (!file.exists()) {
			try { 
				InputStream f = GraveYardMain.getInstance().getResource(fileName + ".yml");
				if (f != null){
					log("Copy stream " + fileName);
					copy(f, file);
					f.close();
				}else{
					log("Skip copy stream " + fileName);
				}
			}catch (Exception e) { e.printStackTrace(); }
			file = new File(GraveYardMain.getInstance().getDataFolder(), fileName + ".yml");
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	  /*
     * this copy(); method copies the specified file from your jar
     *     to your /plugins/<pluginName>/ folder
     */
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	  public static void log(String msg){
			log.info("[WarpPortals] " + msg);
	   }

	
	private File file;
	private FileConfiguration config;
// setter	
	public void setProperty(String path, Object value) {
		config.set(path, value);
		try { 
			config.save(file); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
// 	
	public void set(String path, Object value) {
		config.set(path, value);
		try { config.save(file); }
		catch (Exception e) { 
			MessageManager.getInstance().consoleError("[GraveYard][SM] " + e.getMessage());
		}
	}

	public ConfigurationSection createConfigurationSection(String path) {
		ConfigurationSection cs = config.createSection(path);
		try { config.save(file); }
		catch (Exception e) { e.printStackTrace(); }
		return cs;
	}
	
	public Set<String> getConfigurationSection(String path) {
	       return config.getConfigurationSection(path).getKeys(false);
	}

//	
	public Boolean ContainsKey(String path){
		return config.contains(path);
	}

	public String getString(String path){
		if (ContainsKey(path)){ 
			return config.getString(path);
		}else{
			return "";
		}
	}

	public int getInt(String path){
		if (ContainsKey(path)){ 
			return config.getInt(path);
		}else{
			return 0;
		}	
	}
	
	public Double getDouble(String path) {
		return config.getDouble(path);
	}

	public Long getLong(String path) {
		return config.getLong(path);
	}
	
	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String path) {
		return (T) config.get(path);
	}

	public List<String> getStringList(String path) {
		if (this.ContainsKey(path)){
			return config.getStringList(path);
		} else {
			return null;
		}
	}
	
	public void addStringList(String path, String value) {
		if (config.getStringList(path).size() < 1){
			this.set(path, value);
		} else {
			config.getStringList(path).add(value);
			try { config.save(file); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void delStringList(String path, String value) {
		config.getStringList(path).remove(value);
		try { config.save(file); }
		catch (Exception e) { e.printStackTrace(); }

	}
// de- and serialize locations	
// return string from location
   public void setLocation(String path, Location loc)   {
   	 String locSerialized = loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
     this.set(path, locSerialized);
    }

   // return location from string  
   public Location getLocation(String path) {
	   	String strLoc = getString(path);
	   	MessageManager.getInstance().debugMessage("[SM] Loc " + path);
	    String[] locString = strLoc.split(";");
	    Location loc = new Location(Bukkit.getWorld(locString[0]), Double.parseDouble(locString[1]), Double.parseDouble(locString[2]), Double.parseDouble(locString[3]));
	    if (locString.length == 5) {
		    loc.setPitch(Float.parseFloat(locString[4]));
		    loc.setYaw(Float.parseFloat(locString[5]));
	    }
	    return loc;
    }
   
 	public Map<String,Object> getMap(String path) {
 		Map<String,Object> m = new HashMap<String,Object>();
 	    for (String k : this.getConfigurationSection(path)) {
 	       m.put(k, this.get(path+ "." + k));
	    	MessageManager.getInstance().debugMessage("[SM] key " + k + " val " + m.get(k) );
       }
 	    return m;
	}
 	
 	public void setMap(String path, Map<String, Object> m){
 	    for (Entry<String, Object> s : m.entrySet()) {
 	    	this.set((path + "." + s.getKey()), s.getValue());
 	    	MessageManager.getInstance().debugMessage("[SM] key " + (path + "." + s.getKey()) + " val " + s.getValue() );
       }
 	}
 	
}