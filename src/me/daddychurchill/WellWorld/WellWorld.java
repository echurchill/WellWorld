package me.daddychurchill.WellWorld;

import java.util.logging.Logger;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class WellWorld extends JavaPlugin {
	
	protected Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onDisable() {
		log.info("WellWorld has been disabled");
		
	}

	@Override
	public void onEnable() {
		log.info("WellWorld has been enabled");
		
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id){
		return new WellWorldGenerator(this);
	}

}
