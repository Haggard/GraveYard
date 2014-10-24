package com.gmail.Haggard_nl.Util;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.NumberConversions;

import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
 
public class MetadataUtil {
    static Plugin plugin;
    NumberConversions nc;
 
    private static MetadataUtil instance;  
    
    public MetadataUtil(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }
 
     public MetadataValue getMetadata(Metadatable obj, String key){
        for(MetadataValue value : obj.getMetadata(key)){
            if (value.getOwningPlugin().equals(plugin)){
                return value;
            }
        }
        return null;
    }
 
 
    public void set(Metadatable m, String key, Object value) {
        m.setMetadata(key, new FixedMetadataValue(plugin, value));
    }
 
    public void remove(Metadatable m, String key) {
        if (hasKey(m, key)) {
            m.removeMetadata(key, plugin);
        }
    }
 
    public boolean hasKey(Metadatable m, String key) {
        if (m.getMetadata(key).size() == 0) {
            return false;
        }
        return true;
    }
 
    public Object get(Metadatable m, String key) {
    	if (hasKey(m, key)){
    		return m.getMetadata(key).get(0).value();
    	}
    	return 0;
    }
 
    public void removeAll(Metadatable m, String[] keys) {
        for (String k : keys) {
            remove(m, k);
        }
    }
    
    public int getInt(Metadatable m, String key) {
    	if (hasKey(m, key)){
    		return  NumberConversions.toInt(m.getMetadata(key).get(0).value());
    	}
    	return 0;
    }

    public Double getDouble(Metadatable m, String key) {
    	if (hasKey(m, key)){
    		return  NumberConversions.toDouble(m.getMetadata(key).get(0).value());
    	}
    	return 0d;
    }
 
    public float getFloat(Metadatable m, String key) {
    	if (hasKey(m, key)){
    		return  NumberConversions.toFloat(m.getMetadata(key).get(0).value());
    	}
    	return 0f;
    }

    public String getString(Metadatable m, String key) {
    	if (hasKey(m, key)){
        return  (m.getMetadata(key).get(0).value()).toString();
    	}
    	return "";
    }

    public boolean getBoolean(Metadatable m, String key) {
    	if (hasKey(m, key)){
    		return  Boolean.parseBoolean((m.getMetadata(key).get(0).value()).toString());
    	}
    	return false;
    }

   public void protect(Block block) {
       set(block, "protected", true);
    }
 
    public void unprotect(Block block) {
        remove(block, "protected");
   }
 
    public boolean isProtected(Block block) {
        return getBoolean(block, "protected");
    }

	public MetadataUtil getInstance() {
		return instance;
	}

}
