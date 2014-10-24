package DataBase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.gmail.Haggard_nl.GraveYard.GraveYardMain;
import com.gmail.Haggard_nl.GraveYard.Managers.DataBaseManager;
import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;
import com.gmail.Haggard_nl.GraveYard.Managers.YAMLManager;
import com.gmail.Haggard_nl.Util.UUIDLibrary;

import asyncsql.AsyncSQL;
import asyncsql.SQLConnectionInfo;
import asyncsql.SQLRunnable;
import asyncsql.SyncSQL;


public class SQLite implements DataBaseManager{
	private Object lock = new Object();
	private Connection con;
	private String database;
    private Plugin plugin;
	private String folder;
	private AsyncSQL sqlAsync;
	private SyncSQL sqlSync;
	private Boolean rc = false;
	private UUIDLibrary ulib = new UUIDLibrary();;

	
/**
 * 
 * @param file
 */
  public SQLite(){
	    plugin = GraveYardMain.getInstance();
	    this.database = YAMLManager.getConfig().getString("Config.Database");
	    String path = plugin.getDataFolder() + File.separator;
	    File dir = new File(path + File.separator  );
	    File file = new File(dir + File.separator + database);
	    this.folder = dir.toString();
	    dir.mkdir();
	    if (!file.exists()) {
	      try {
	    	  file.createNewFile();
	      } catch (IOException e) {
	    	  MessageManager.getInstance().consoleError("Failed to create SQLite database file/n" + e);
	      }
	    }
	    SQLConnectionInfo connectionInfo = new SQLConnectionInfo(path, database);
		try {
			sqlSync = new SyncSQL(plugin, plugin.getLogger(), connectionInfo);
			sqlAsync = new AsyncSQL(plugin, plugin.getLogger(), connectionInfo);
		} catch (SQLException e) {
			MessageManager.getInstance().consoleError("Failed to initialize SQLite database file!");
			MessageManager.getInstance().consoleInfoMsg(e.getMessage());
		}
		CreateTables(); 
	    MessageManager.getInstance().consoleGoodMsg("Initiated SQLite database");
  }
 
  /**
   * create all tables of the database	
   */
  		private void CreateTables(){
  			CreateTable("CREATE TABLE IF NOT EXISTS Yard (yardId INTEGER PRIMARY KEY, worldname VARCHAR(60) NOT NULL, spawn VARCHAR(120) NOT NULL, cuboidinner VARCHAR[160) NOT NULL, cuboidouter VARCHAR(160) NOT NULL, boundto VARCHAR(60), undofile VARCHAR(160) NOT NULL, UNIQUE (cuboidInner, cuboidouter, undofile));");
  			CreateTable("CREATE TABLE IF NOT EXISTS Grave (graveId INTEGER PRIMARY KEY, yardId INTEGER NOT NULL, coffinnr INTEGER NOT NULL, chest1 VARCHAR(60) NOT NULL, chest2 VARCHAR(60) NOT NULL, skull VARCHAR(60) NOT NULL, sign VARCHAR(60) NOT NULL, playeruuid VARCHAR(160) NOT NULL, UNIQUE (playeruuid, yardId, coffinnr));");
  		}
 /**
  * Create a table async.  		
  * @param tabelSQL
  */
  		private void CreateTable(final String tabelSQL) {
  			sqlAsync.run("Create table", new SQLRunnable(){

				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(tabelSQL);
					try{
						statement.execute();
					}finally{
						statement.close();
					}
				}
  				
  			});
   }
 /**
  * close the database
  */
  		
	@Override
	public void closeDB(){
	   try {
		   if (this.sqlAsync != null){
   	        this.sqlAsync.finishUp();
   	      }
		   if (this.sqlSync != null){
	   	        this.sqlSync.finishUp();
	   	      }
   	    } catch (Exception e){
   	    	MessageManager.getInstance().consoleError("Failed to close database connection! " + e.getMessage());
   	    }  		
	}
	
/**
 * Check if the connecition is still alive	
 */
  		@Override
   		public boolean CheckConnection(){
   			return Boolean.valueOf(con != null);
   		}
   		
 /**
  *  		
  */
   		@Override
  		  public Boolean insert(final String tabelSQL){
  			rc = false;
  			sqlAsync.run("Async Insert in table", new SQLRunnable(){
				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(tabelSQL);
					try{
						rc = statement.execute();
					}finally{
						statement.close();
					}
				}
  				
  			});
  			return rc;
  		  }


   		@Override
  		  public Boolean delete(final String query){
   			rc = false;
 			sqlAsync.run("Delete data", new SQLRunnable(){
 				
				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(query);
					try{
						rc = statement.execute();
					}finally{
						statement.close();
					}
				}
  			});
 			return rc;
  		  }


   		@Override
  		  public Boolean execute(final String query){
   			rc = false;
			sqlSync.run("Execute sync", new SQLRunnable(){

				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(query);
					try{
						rc = statement.execute();
					}finally{
						statement.close();
					}
				}
  				
  			}); 
			return rc;
  		  }

   		@Override
		  public Boolean update(final String query){
 			rc = false;
			sqlAsync.run("Execute Async", new SQLRunnable(){

				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(query);
					try{
						rc = statement.execute();
					}finally{
						statement.close();
					}
				}
				
			}); 
			return rc;
		  }

   		
   		@Override
  		  public Boolean existsTable(final String table){
  			rc = false;
 			sqlSync.run("Table exists", new SQLRunnable(){

				@Override
				public void run(Connection connection) throws SQLException {
					String sqlString = "SELECT EXISTS(SELECT 1 FROM sqlite_master WHERE type='table' AND name='" + table + "');";
					PreparedStatement statement = connection.prepareStatement(sqlString);
					try{
						ResultSet rs = statement.executeQuery();
						while(rs.next()){
							rc = rs.getBoolean(1);
						}
						rs.close();
					}finally{
						statement.close();
					}
				}
  				
  			});  			
			return rc;
  		}


		  public Boolean valueExists(final String query){
			rc = false;
			sqlSync.run("Table exists", new SQLRunnable(){

				@Override
				public void run(Connection connection) throws SQLException {
					PreparedStatement statement = connection.prepareStatement(query);
					try{
						ResultSet rs = statement.executeQuery();
						while(rs.next()){
							rc = rs.getBoolean(1);
						}
						rs.close();
					}finally{
						statement.close();
					}
				}
				
			});  			
			return rc;
		}

		    ArrayList<HashMap<String, Object>> data = null;
		    ArrayList<Object> list = null;
			HashMap<String, Object> map = null;
		    Object value = null;
		 
		    public Object GetValue(String sub, final String sqlString) {
			    value = null;
				sqlSync.run("[Get a value] " + sub, new SQLRunnable(){

					@Override
					public void run(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(sqlString);
						try{
							ResultSet rs = statement.executeQuery();
						    while(rs.next()){
								value = (rs.getObject(1));
						    }
						    rs.close();
						}finally{
							statement.close();
						}
					}
				});  			
				return value;
			}
		 
		    
		    public List<Object> GetList(final String sqlString) {
			    list = new ArrayList<Object>();
				sqlSync.run("Get a list", new SQLRunnable(){

					@Override
					public void run(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(sqlString);
						try{
							ResultSet rs = statement.executeQuery();
						    while(rs.next()){
								list.add(rs.getObject(1));
						    }
						    rs.close();
						}finally{
							statement.close();
						}
					}
				});  			
				return list;
			}

			
			@Override
			public ArrayList<HashMap<String, Object>> GetMapList(final String sqlString) {
				data = null;
				sqlSync.run("Get a MapList", new SQLRunnable(){

					@Override
					public void run(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(sqlString);
						try{
							ResultSet rs = statement.executeQuery();
							int columnSize = rs.getMetaData().getColumnCount() + 1;
						    data = new ArrayList<HashMap<String,Object>>();
						    while(rs.next()){
								HashMap<String, Object> map = new HashMap<String, Object>(columnSize);
						    	for(int j=1; j < columnSize; j++){
									 map.put(rs.getMetaData().getColumnName(j), rs.getObject(j));
								}
								data.add(map);
						    }
						    rs.close();
						}finally{
							statement.close();
						}
					}
				});  			
				return data;
			}
			
			public HashMap<String, Object> GetMap(final String sqlString) {
				map = new HashMap<String, Object>();
				sqlSync.run("Get a HashMap", new SQLRunnable(){

					@Override
					public void run(Connection connection) throws SQLException {
						PreparedStatement statement = connection.prepareStatement(sqlString);
						try{
							ResultSet rs = statement.executeQuery();
						    while(rs.next()){
								 map.put(rs.getString(1), rs.getObject(2));
						    }
						    rs.close();
						}finally{
							statement.close();
						}
					}
				});  			
				return map;
			}
			
			  private float toFload(Object value){
				  if(value != null){
					  return (float) (((Double) value) + 0.0f);
				  }
				  return 0;
			  }
			  
			  private long toLong(Object value){
				  if(value != null){
					  return ((Long) value);
				  }
				  return 0;
			  }

			  private double toDouble(Object value){
				  if(value != null){
					  return ((Double) value);
				  }
				  return 0;
			  }

			  private Integer toInt(Object value){
				  if(value != null){
					  return ((Integer) value);
				  }
				  return 0;
			  }

			  private Byte toByte(Object value){
				  if(value != null){
					  return ((Byte) value);
				  }
				  return 0;
			  }
			  
			  private String toString(Object value){
				  if(value != null){
					  return ((String) value);
				  }
				  return null;
			  }
			  private Boolean toBoolean(Object value){
				  if(value != null){
					  return ((Boolean) value);
				  }
				  return false;
			  }
		  
//=================================================================================================
/**
 * 
 * Table Yards
 * 
 * yardId INTEGER 
 * worldname VARCHAR(60)
 * spawn VARCHAR(120)
 * cuboidinner VARCHAR[160)
 * cuboidouter VARCHAR(160)
 * boundto VARCHAR(60)
 * undofile VARCHAR(160)
 * 		  
 */
			  
			  
	@Override
	public boolean setYard(String worldName, String Spawn,String cuboidInner, 
			String cuboidOuter, String boundTo, String undoFile) 
		{
		String insert = "INSERT OR IGNORE INTO Yard (worldname,spawn,cuboidinner,cuboidouter,boundto,undofile)";
		String values = "VALUES('"+worldName+"','"+Spawn+"','"+cuboidInner+"',";
		values = values + "'"+cuboidOuter+"','"+boundTo+"','"+undoFile+"');";
		this.insert(insert+" "+values);
		
		return false;
	}
	@Override			  
	public boolean updateYard(String worldName, String Spawn,String cuboidInner, 
			String cuboidOuter, String boundTo, String undoFile)
		{
		String insert = "UPDATE Yard (worldName,spawn,cuboidinner,cuboidouter,boundto,undofile)";
		String values = "VALUES('"+worldName+"','"+Spawn+"','"+cuboidInner+"',";
		values = values + "'"+cuboidOuter+"','"+boundTo+"','"+undoFile+"');";
		this.insert(insert+" "+values);
		
		return false;
	}
	@Override			  
	public boolean deleteYard(String worldName, String cuboidInner) 
		{
		String delete = "Delete * FROM Yard";
		String values = "WHERE worldname='"+worldName+"' AND '"+cuboidInner+"');";
		this.delete(delete+" "+values);
		
		return false;
	}
	@Override
	public Integer getYardId(String worldName, String cuboidInner) {
		String delete = "SELECT yardId FROM Yard";
		String values = "WHERE worldname='"+worldName+"' AND '"+cuboidInner+"');";
		return this.toInt(this.GetValue("", delete+" "+values));
	}

	/**
	 * 
	 * Graves
	 * 		  
	 */

	@Override
	public boolean setGrave(Integer yardId, Integer garveNumber, String playerUUID,
			String chest1Loc, String chest2Loc, String skullLoc, String signLoc) 
		{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean updatetGrave(Integer yardId, Integer garveNumber, String playerUUID,
			String chest1Loc, String chest2Loc, String skullLoc, String signLoc) 
		{
		// TODO Auto-generated method stub
		return false;
	}
	
/**
 * 
 * Coffins
 * 		  
 */

	@Override
	public boolean clearCoffin(Integer yardId, String worldName, String playerUUID) {
		// TODO Auto-generated method stub
		return false;
	}

}
