package com.gmail.Haggard_nl.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {

	public static Boolean isSign(Material material){
		return (material.equals(Material.SIGN) || material.equals(Material.SIGN_POST) || material.equals(Material.WALL_SIGN));
	}

	public static Location string2Location(String str){
       String[] args = str.split(";");
       String world = args[0];

       double x = Double.parseDouble(args[1]);
       double y = Double.parseDouble(args[2]);
       double z = Double.parseDouble(args[3]);    
       Location loc = new Location(Bukkit.getWorld(world), x, y, z);
       return loc;
   }

   public static String location2String(Location loc){
       String world = loc.getWorld().getName();
       int x = loc.getBlockX();
       int y = loc.getBlockY();
       int z = loc.getBlockZ();
       return new String(world + ";" + x + ";" + y + ";" + z);
   }
   
   public static Location EntityString2Location(String str){
       String[] args = str.split(";");
       String world = args[0];
       double x = Double.parseDouble(args[1]);
       double y = Double.parseDouble(args[2]);
       double z = Double.parseDouble(args[3]);    
       float pitch = Float.parseFloat(args[4]);
       float yaw = Float.parseFloat(args[5]);
       Location loc = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
       return loc;
   }

   public static String EntityLocation2String(Location loc){
       String world = loc.getWorld().getName();
       int x = loc.getBlockX();
       int y = loc.getBlockY();
       int z = loc.getBlockZ();
       int pitch = (int) loc.getPitch();
       int yaw = (int) loc.getYaw();
       return new String(world + ";" + x + ";" + y + ";" + z + ";" + pitch + ";" + yaw);
   }
   
   public static Boolean sameLocation(Location l1, Location l2){
	   int x1 = l1.getBlockX();
	   int y1 = l1.getBlockY();
	   int z1 = l1.getBlockZ();
	   int x2 = l2.getBlockX();
	   int y2 = l2.getBlockY();
	   int z2 = l2.getBlockZ();
	   return (x1 == x2 && y1 == y2 && z1 == z2);
   }
   
   public static String getDateTime(){
     Calendar cal = Calendar.getInstance();
     SimpleDateFormat sdf = new SimpleDateFormat("MMM d ''yy HH:mm");
     return sdf.format(cal.getTime());
   }

   public static String convertTicks(int ticks){
     int seconds = ticks / 20;
     String time;
     if (seconds < 60) {
       time = seconds + "s";
     } else {
       int minutes = seconds / 60;
       if (minutes < 60) {
         time = minutes + "m " + (seconds - minutes * 60) + "s";
       } else {
         int hours = minutes / 60;
         time = hours + "h " + (minutes - hours * 60) + "m " + (seconds - minutes * 60) + "s";
       }
     }
     return time;
   }
   
   public static Vector getMaximum(Vector v1, Vector v2){
	   return Vector.getMaximum(v1, v2);
   }
   
   public static Vector getMinimum(Vector v1, Vector v2){
	   return Vector.getMinimum(v1, v2);
   }
   
   /**
    * Get the cardinal compass direction of a player.
    * Math.round(yaw / 90f);
    * @param player
    * @return
    */
   public static String getDirection(Player player) {
 /*      double rot = Math.round((player.getLocation().getYaw() / 90f));
       if (rot < 0) {
           rot += 360.0;
       }
       return getDirection(rot);
*/
	   		Float yaw = player.getLocation().getYaw();
	       yaw = yaw / 90;
	       yaw = (float)Math.round(yaw);
	    
	       if (yaw == -4 || yaw == 0 || yaw == 4) {return "SOUTH";}
	       if (yaw == -1 || yaw == 3) {return "EAST";}
	       if (yaw == -2 || yaw == 2) {return "NORTH";}
	       if (yaw == -3 || yaw == 1) {return "WEST";}
	       return "";  
   }
   /**
    * Get the cardinal compass direction of a player.
    * 
    * @param player
    * @return
    */
   public static String getCardinalDirection(Player player) {
       double rot = (player.getLocation().getYaw() - 90) % 360;
       if (rot < 0) {
           rot += 360.0;
       }
       return getDirection(rot);
   }

   /**
    * Converts a rotation to a cardinal direction name.
    * 
    * @param rot
    * @return
    */
   private static String getDirection(double rot) {
       if (0 <= rot && rot < 22.5) {
           return "North";
       } else if (22.5 <= rot && rot < 67.5) {
           return "Northeast";
       } else if (67.5 <= rot && rot < 112.5) {
           return "East";
       } else if (112.5 <= rot && rot < 157.5) {
           return "Southeast";
       } else if (157.5 <= rot && rot < 202.5) {
           return "South";
       } else if (202.5 <= rot && rot < 247.5) {
           return "Southwest";
       } else if (247.5 <= rot && rot < 292.5) {
           return "West";
       } else if (292.5 <= rot && rot < 337.5) {
           return "Northwest";
       } else if (337.5 <= rot && rot < 360.0) {
           return "North";
       } else {
           return null;
       }
   }
}
