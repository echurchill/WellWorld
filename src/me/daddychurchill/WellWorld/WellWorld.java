package me.daddychurchill.WellWorld;

import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Logger;

import me.daddychurchill.WellWorld.Support.WellWorldCreateCMD;
import me.daddychurchill.WellWorld.WellTypes.*;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class WellWorld extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
	public static final int wellWidthInChunks = 9; 
   	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String name, String style){
		return new WellWorldChunkGenerator(this, name, style);
	}

	//TODO prevent people from climbing over the walls
	
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
	private Hashtable<Long, WellArchetype> wells;

	public WellArchetype getWellManager(World world, Random random, int chunkX, int chunkZ) {
		// get the list of wells
		if (wells == null)
			wells = new Hashtable<Long, WellArchetype>();
		
		// find the origin for the well
		int wellX = calcOrigin(chunkX);
		int wellZ = calcOrigin(chunkZ);

		// calculate the plat's key
		long wellpos = (long) wellX * (long) Integer.MAX_VALUE + (long) wellZ;
		Long wellkey = Long.valueOf(wellpos);
		long wellseed = wellpos ^ world.getSeed();

		// see if the well is already out there
		WellArchetype wellmanager = wells.get(wellkey);
		
		// doesn't exist? then make it!
		if (wellmanager == null) {
			
			//TODO add other possible well types
			// bottom noise
			// cave noise
			// water flat
			// lava flat
			// crystal world
			if (wellX == 0 && wellZ == 0)
				wellmanager = new KnollsWell(wellseed, wellX, wellZ);
			else
				wellmanager = randomWellManager(random, wellseed, wellX, wellZ);
			
			// remember it for the next time
			wells.put(wellkey, wellmanager);
		}
		
		// return it
		return wellmanager;
	}
	
	// Supporting code used by getWellManager
	private int calcOrigin(int i) {
		if (i >= 0) {
			return i / wellWidthInChunks * wellWidthInChunks;
		} else {
			return -((Math.abs(i + 1) / wellWidthInChunks * wellWidthInChunks) + wellWidthInChunks);
		}
	}
	
	//TODO Maze of rooms
	//TODO Space with a few moons
	//TODO Basalt field (http://www.flickr.com/photos/golfie88/3712377542/)
	//TODO Volcano with lava
	//TODO Desert with cactus
	//TODO Silicon with crystal trees
	//TODO Captured farm
	//TODO Captured village/town
	//TODO Captured city block
	//TODO Captured space station/ship
	//TODO Captured futuristic town (domed city on RealisticMoon?)
	
	private WellArchetype randomWellManager(Random random, long seed, int wellX, int wellZ) {
		switch (random.nextInt(4)) {
		case 1:
			return new AlienWorldWell(seed, wellX, wellZ);
		case 2:
			return new AlienCavernWell(seed, wellX, wellZ);
		case 3:
			return new RealisticMoonWell(seed, wellX, wellZ);

// not enabled as they are kind of boring :-)
//		case 4:
//			return new VeryEmptyWell(seed, wellX, wellZ);
//		case 5:
//			return new VerySimpleFlatWell(seed, wellX, wellZ);
//		case 6:
//			return new VerySimpleWaterWell(seed, wellX, wellZ);
//		case 7:
//			return new VerySimpleHillyWell(seed, wellX, wellZ);
//		case 9:
//			return new SimplexNoiseWell(seed, wellX, wellZ);
//		case 10:
//			return new SimplexOctaveWell(seed, wellX, wellZ);

// not enabled as I don't have permission to do so
//		case 8:
//			return new CodenameBWell(seed, wellX, wellZ);
//		case 11:
//			return new KhylandWell(seed, wellX, wellZ);
//		case 12:
//			return new PancakeWell(seed, wellX, wellZ);
//		case 13:
//			return new DinnerboneMoonWell(seed, wellX, wellZ);
		default:
			return new KnollsWell(seed, wellX, wellZ);
		}
	}
}
