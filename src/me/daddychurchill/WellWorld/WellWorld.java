package me.daddychurchill.WellWorld;

import java.util.logging.Logger;

import me.daddychurchill.WellWorld.Support.WellWorldCreateCMD;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

//DONE Global.BedrockWalls = obsidian or bedrock walls (false)
//DONE Global.WallDoorways = pathways through the walls (true)
//DONE Global.HexisWells = hexagonally laid out wells (true)
//TODO "worldname".<option> support for world specific options

//DONE Command.WellWorld
//TODO Command.WellWorld Leave
//TODO Command.WellWorld Regenerate
//TODO Command.WellWorld Regenerate "WellType"
//TODO player.hasPermission("WellWorld.WellWorldCommand") = WellWorld command enabled (true)
//TODO player.hasPermission("WellWorld.WellWorldCommandLeave") = WellWorld command leave option enabled (true)
//TODO player.hasPermission("WellWorld.WellWorldCommandRegeneration") = WellWorld command well regeneration option enabled (true)

//TODO Dynamically load wells "engines" from plugin/wellworld/*.well
//TODO Autoregister well "generators" from code
//TODO Predictable well types/seeds via noise instead of random
//TODO Move restriction to prevent wall climb overs

//TODO Use world generators (say like CityWorld) as well generators 
//TODO ..Captured farm, village/town and city block
//TODO Maze of rooms
//TODO Space with a few moons
//TODO Create a variant of BananaVoid with multiple vertical levels of pathways
//TODO Curved surface moon world with "moon" bases
//TODO Giant tree
//TODO Captured space station/ship
//TODO Captured futuristic town (domed city on RealisticMoon?)
//TODO A building with rooms/halls/fixtures/furniture/etc.
//TODO Toroidal space station
//TODO Four/Two castles facing each other
//TODO Single castle for storming purposes
//TODO Dungeon crawler
//TODO Ocean world with "floating islands"

//DONE Basalt field (http://www.flickr.com/photos/golfie88/3712377542/)
//DONE Desert with cactus
//DONE Silicon with crystal trees
//DONE Volcano with lava
//DONE Forested well
//DONE Hypersmooth snow world
//DONE Port BananaIce, BananaVoid and BananaForest
//DONE Included Khyperia's wells

public class WellWorld extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
	public static final int wellWidthInChunks = 8; 
	
	private Material wallMaterial;
	private Material negativeMaterial;
	private boolean wallDoorways;
	private boolean hexishWells;
	private boolean wellMarkers;
   	
	public WellWorld() {
		super();
		
		setBedrockWalls(false); // default to obsidian for prettiness
		setWallDoorways(true); // assume we want ways through
		setHexishWells(true); // assume we are hexagonally laying out the wells
		setWellMarkers(false);
		negativeMaterial = Material.AIR;
	}

	public void setBedrockWalls(boolean doit) {
		wallMaterial = doit ? Material.BEDROCK : Material.OBSIDIAN;
	}

	public Material getWallMaterial() {
		return wallMaterial;
	}
	
	public Material getNegativeWallMaterial() {
		return negativeMaterial;
	}
	
	public boolean isWallDoorways() {
		return wallDoorways;
	}
	
	public void setWallDoorways(boolean doit) {
		wallDoorways = doit;
	}
	
	public boolean isHexishWells() {
		return hexishWells;
	}
	
	public void setHexishWells(boolean doit) {
		hexishWells = doit;
	}
	
	public boolean isWellMarkers() {
		return wellMarkers;
	}
	
	public void setWellMarkers(boolean doit) {
		wellMarkers = doit;
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String name, String style){
		return new WellWorldChunkGenerator(this, name, style);
	}

	//TODO prevent people from climbing over the walls
	
	@Override
	public void onDisable() {
		// remember for the next time
		saveConfig();
		
		// goodbye cruel world
		log.info(getDescription().getFullName() + " has been disabled" );
	}

	@Override
	public void onEnable() {
		//PluginManager pm = getServer().getPluginManager();
		
		// add the commands
		addCommand("wellworld", new WellWorldCreateCMD(this));
		// wellworld:
		//    description: create/goto/leave WellWorld
		//    usage: /wellworld [leave]
		
//		addCommand("well", new WellWorldWellCMD(this));
		// well:
		//    description: modify the current well
		//    usage: /well regenerate

//		addCommand("wellcalc", new WellWorldWellCalcCMD(this));
		
		// add/get the configuration
		FileConfiguration config = getConfig();
		config.options().header("WellWorld Global Options");
		config.addDefault("Global.BedrockWalls", false);
		config.addDefault("Global.WallDoorways", true);
		config.addDefault("Global.HexishWells", true);
		config.addDefault("Global.WellMarkers", true);
		config.options().copyDefaults(true);
		saveConfig();
		
		// now read out the bits for real
		setBedrockWalls(config.getBoolean("Global.BedrockWalls"));
		setWallDoorways(config.getBoolean("Global.WallDoorways"));
		setHexishWells(config.getBoolean("Global.HexishWells"));
		setWellMarkers(config.getBoolean("Global.WellMarkers"));
		
		// announce our happiness
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
	private World primeWellWorld = null;
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
	
}
