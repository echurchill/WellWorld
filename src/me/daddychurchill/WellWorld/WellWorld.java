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

/* TODO 
 * Well regenerate
 * Predictable Well types and seeds, use noise instead of random
 * Shuffle instead of Random.NextInt
 * Dynamically load wells from plugins/wellworld
 * YML
 *    BedrockWalls = obsidian or bedrock walls (false)
 *    WallDoorways = pathways through the walls (true)
 *    HexishWells = hexagonally layed out wells (true)
 */

public class WellWorld extends JavaPlugin {
    public static final Logger log = Logger.getLogger("Minecraft.CityWorld");
	public static final int wellWidthInChunks = 8; 
	
	private FileConfiguration config;
	
	private Material wallMaterial;
	private Material negativeMaterial;
	private boolean wallDoorways;
	private boolean hexishWells;
   	
	public WellWorld() {
		super();
		
		setBedrockWalls(false); // default to obsidian for prettiness
		setWallDoorways(true); // assume we want ways through
		setHexishWells(true); // assume we are hexagonally laying out the wells
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
	
	public void setWallDoorways(boolean doit) {
		wallDoorways = doit;
	}
	
	public boolean getWallDoorways() {
		return wallDoorways;
	}
	
	public void setHexishWells(boolean doit) {
		hexishWells = doit;
	}
	
	public boolean getHexishWells() {
		return hexishWells;
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
		config = getConfig();
		config.options().header("WellWorld Global Options");
		config.addDefault("Global.BedrockWalls", false);
		config.addDefault("Global.WallDoorways", true);
		config.addDefault("Global.HexishWells", true);
		config.options().copyDefaults(true);
		saveConfig();
		
		// now read out the bits for real
		setBedrockWalls(config.getBoolean("Global.BedrockWalls"));
		setWallDoorways(config.getBoolean("Global.WallDoorways"));
		setHexishWells(config.getBoolean("Global.HexishWells"));
		
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
	
}
