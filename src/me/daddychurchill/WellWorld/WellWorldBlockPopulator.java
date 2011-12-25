package me.daddychurchill.WellWorld;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class WellWorldBlockPopulator extends BlockPopulator {
	
	private WellWorld plugin;
	public String worldname;
	public String worldstyle;
	
	public WellWorldBlockPopulator(WellWorld instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {
		
		// figure out what everything looks like
		WellManager well = plugin.getWellManager(world, random, source.getX(), source.getZ());
		if (well != null)
			well.populateBlocks(world, random, source);
	}
}
