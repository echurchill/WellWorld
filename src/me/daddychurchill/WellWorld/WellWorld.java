package me.daddychurchill.WellWorld;

import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Logger;

import me.daddychurchill.WellWorld.WellTypes.PrototypeWell;
import me.daddychurchill.WellWorld.WellTypes.KhylandWell;
import me.daddychurchill.WellWorld.WellTypes.PancakeWell;
import me.daddychurchill.WellWorld.WellTypes.WaterWell;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class WellWorld extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
   	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String name, String style){
		return new WellWorldChunkGenerator(this, name, style);
	}
	
	@Override
	public void onDisable() {
		log.info(getDescription().getFullName() + " has been disabled" );
	}

	@Override
	public void onEnable() {
		//PluginManager pm = getServer().getPluginManager();
		
		addCommand("wellworld", new WellWorldCreateCMD(this));

		// configFile can be retrieved via getConfig()
		log.info(getDescription().getFullName() + " is enabled" );
	}
	
	private void addCommand(String keyword, CommandExecutor exec) {
		PluginCommand cmd = getCommand(keyword);
		if (cmd == null || exec == null) {
			log.info("Cannot create command for " + keyword);
		} else {
			cmd.setExecutor(exec);
		}
	}
	
    // prime world support (loosely based on CityWorld which in turn was loosely based on ExpansiveTerrain)
	public final static String WORLD_NAME = "WellWorld";
	private static World primeWellWorld = null;
	public World getWellWorld() {
		
		// created yet?
		if (primeWellWorld == null) {
			
			// built yet?
			primeWellWorld = Bukkit.getServer().getWorld(WORLD_NAME);
			if (primeWellWorld == null) {
				
				// if neither then create/build it!
				WorldCreator worldcreator = new WorldCreator(WORLD_NAME);
				worldcreator.environment(World.Environment.NORMAL);
				worldcreator.generator(new WellWorldChunkGenerator(this, WORLD_NAME, ""));
				primeWellWorld = Bukkit.getServer().createWorld(worldcreator);
			}
		}
		return primeWellWorld;
	}
	
	
	// Class instance data
	private Hashtable<Long, WellManager> wells;

	public WellManager getWellManager(World world, Random random, int chunkX, int chunkZ) {
		// get the list of wells
		if (wells == null)
			wells = new Hashtable<Long, WellManager>();
		
		// find the origin for the plat
		int platX = calcOrigin(chunkX);
		int platZ = calcOrigin(chunkZ);

		// calculate the plat's key
		Long wellkey = Long.valueOf(((long) platX * (long) Integer.MAX_VALUE + (long) platZ));

		// see if the well is already out there
		WellManager wellmanager = wells.get(wellkey);
		
		// doesn't exist? then make it!
		if (wellmanager == null) {
			
			//TODO add other possible well types
			// bottom noise
			// cave noise
			// water flat
			// lava flat
			// crystal world
			wellmanager = randomWellManager(random);
			
			// remember it for the next time
			wells.put(wellkey, wellmanager);
		}
		
		// return it
		return wellmanager;
	}
	
	// Supporting code used by getWellManager
	static final public int Width = 9; 
	private int calcOrigin(int i) {
		if (i >= 0) {
			return i / Width * Width;
		} else {
			return -((Math.abs(i + 1) / Width * Width) + Width);
		}
	}
	
	private WellManager randomWellManager(Random random) {
		switch (random.nextInt(4)) {
		case 1:
			return new KhylandWell(random);
		case 2:
			return new PancakeWell(random);
		case 3:
			return new WaterWell(random);
		default:
			return new PrototypeWell(random);
		}
	}
}
