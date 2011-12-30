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
		int chunkX = source.getX();
		int chunkZ = source.getZ();
		
		// figure out what everything looks like
		WellArchetype well = plugin.getWellManager(world, random, source.getX(), source.getZ());
		if (well != null) {
			well.populateBlocks(world, source);
			
			// add walls and floors
			int testX = chunkX % WellWorld.wellWidthInChunks;
			int testZ = chunkZ % WellWorld.wellWidthInChunks;
			populateWalls(source, testX == 0, testX == WellWorld.wellWidthInChunks - 1 || chunkX == -1,
								  testZ == 0, testZ == WellWorld.wellWidthInChunks - 1 || chunkZ == -1);
			
		}
	}
	
	private void populateWalls(Chunk source, boolean wallNorth, boolean wallSouth, boolean wallWest, boolean wallEast) {
		//TODO teleports, railroads, rooms, doors?
	}
}
