package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.WellWall;

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
		int chunkX = source.getX();
		int chunkZ = source.getZ();
		
		// figure out what everything looks like
		WellArchetype well = plugin.getWellManager(world, random, chunkX, chunkZ);
		if (well != null) {
			
			// well centric chunkX/Z
			int adjustedX = chunkX - well.getX();
			int adjustedZ = chunkZ - well.getZ();
			
			// populate the chunk
			well.populateBlocks(source, adjustedX, adjustedZ);
			
			// draw the well walls
			WellWall.populateWalls(well, source, adjustedX, adjustedZ);
		}
	}
}
