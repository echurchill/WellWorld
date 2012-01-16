package me.daddychurchill.WellWorld;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class WellWorldBlockPopulator extends BlockPopulator {
	
	private WellWorldChunkGenerator chunkGen;
	
	public WellWorldBlockPopulator(WellWorldChunkGenerator chunkGen){
		this.chunkGen = chunkGen;
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {
		int chunkX = source.getX();
		int chunkZ = source.getZ();
		
		// figure out what everything looks like
		WellArchetype well = chunkGen.getWellManager(world, random, chunkX, chunkZ);
		if (well != null) {
			
			// well centric chunkX/Z
			int adjustedX = chunkX - well.getX();
			int adjustedZ = chunkZ - well.getZ();
			
			// populate the chunk
			well.populateBlocks(source, adjustedX, adjustedZ);
			
			// draw the well walls
			populateWalls(well, source, adjustedX, adjustedZ);
		}
	}
	
	private void populateWalls(WellArchetype well, Chunk source, int wellChunkX, int wellChunkZ) {
		//TODO teleports, railroads, rooms, doors?
	}
}
