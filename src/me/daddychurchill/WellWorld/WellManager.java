package me.daddychurchill.WellWorld;

import java.util.Random;

import me.daddychurchill.WellWorld.Support.ByteChunk;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

public abstract class WellManager {
	
	public WellManager(Random random) {
		//TODO Anything here?
	}
	
	public abstract void populateChunk(World world, Random random, ByteChunk chunk);
	public abstract void populateBlocks(World world, Random random, Chunk chunk);
	
	public boolean populateWalls(World world, Random random, ByteChunk chunk) {
		
		// do something?
		int chunkX = chunk.getX() % WellWorld.Width;
		int chunkZ = chunk.getZ() % WellWorld.Width;
		boolean buildWalls = chunkX == 0 || chunkZ == 0;
		
		// then do it!
		if (buildWalls) {
			// make a door-ish-thing
			int doorAt = WellWorld.Width / 2;
			if ((chunkX == doorAt && chunkZ == 0) ||
				(chunkZ == doorAt && chunkX == 0)) {
				
				// how big is the door and where is it?
				int doorLevel = random.nextInt(64) + 32;
				int doorHeight = random.nextInt(6) + 1;
				
				// put things above and blow 
				chunk.setBlocksAt(0, doorLevel, Material.BEDROCK);
				chunk.setBlocksAt(doorLevel + doorHeight, ByteChunk.Height, Material.BEDROCK);
			} else
				chunk.setAllBlocks(Material.BEDROCK);
			
//			
//			// bottom out
//			chunk.setBlocksAt(0, Material.BEDROCK);
//			
//			// wall it in
//			int y1 = 1;
//			int y2 = ByteChunk.Height - 1;
//			for (int i = 0; i < ByteChunk.Width - 1; i++) {
//				chunk.setBlocks(i, y1, y2, 0, Material.BEDROCK);
//				chunk.setBlocks(ByteChunk.Width - 1, y1, y2, i, Material.BEDROCK);
//				chunk.setBlocks(i + 1, y1, y2, ByteChunk.Width - 1, Material.BEDROCK);
//				chunk.setBlocks(0, y1, y2, i + 1, Material.BEDROCK);
//			}
//			
//			// top it out
//			chunk.setBlocksAt(ByteChunk.Height - 1, Material.BEDROCK);
		}
		
		// inform the caller
		return buildWalls;
	}
}
