package me.daddychurchill.WellWorld.Support;

import java.util.Random;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.WellWorld;

import org.bukkit.Chunk;
import org.bukkit.Material;

public class WellWall {
	public static final int wallThicknessInBlocks = 8;
	public static final int wallHeightInBlocks = 128;
	public static final int floorThicknessInBlocks = 1;
	public static final int ceilingThicknessInBlocks = 1;
	public static final Material wallMaterial = Material.OBSIDIAN;
	public static final Material negativeMaterial = Material.AIR;
	public static final int portalLevels = 3; // how many levels of portals are there
	
	public static final void generateWalls(WellArchetype well, Random random, ByteChunk source, int wellChunkX, int wellChunkZ) {
		// top
		if (well.includeTop())
			source.setBlocksAt(wallHeightInBlocks - ceilingThicknessInBlocks, wallHeightInBlocks, wallMaterial);
		
		// sides?
		boolean wallNorth = wellChunkX == 0;
		boolean wallSouth = wellChunkX == WellWorld.wellWidthInChunks - 1;
		boolean wallWest = wellChunkZ == 0;
		boolean wallEast = wellChunkZ == WellWorld.wellWidthInChunks - 1;
		if (wallNorth || wallSouth || wallEast || wallWest) {
			
			// place the walls themselves
			int step = wallHeightInBlocks / wallThicknessInBlocks;
			for (int i = 0; i < wallThicknessInBlocks; i++) {
				int top = wallHeightInBlocks - i * step;
				if (wallNorth)
					source.setBlocks(i, i + 1, 0, top, 0, 16, wallMaterial);
				if (wallSouth)
					source.setBlocks(16 - i - 1, 16 - i, 0, top, 0, 16, wallMaterial);
				if (wallWest)
					source.setBlocks(0, 16, 0, top, i, i + 1, wallMaterial);
				if (wallEast)
					source.setBlocks(0, 16, 0, top, 16 - i - 1, 16 - i, wallMaterial);
			}
			
			// punch some random holes in the walls... for now
			int y1 = wallHeightInBlocks - 2 * (wallHeightInBlocks / wallThicknessInBlocks) + random.nextInt(portalLevels) * 3;
			int y2 = y1 + 2;

			// punch the door
			if (wallNorth) {
				source.setBlocks(0, 2, y1, y2, 9, 10, negativeMaterial); // door
				source.setBlocks(0, 1, y1, y2, 7, 9, negativeMaterial); // hall
			}
			if (wallSouth) {
				source.setBlocks(14, 16, y1, y2, 6, 7, negativeMaterial); // door
				source.setBlocks(15, 16, y1, y2, 7, 9, negativeMaterial); // hall
			}
			if (wallWest) {
				source.setBlocks(9, 10, y1, y2, 0, 2, negativeMaterial); // door
				source.setBlocks(7, 9, y1, y2, 0, 1, negativeMaterial); // hall
			}
			if (wallEast) {
				source.setBlocks(6, 7, y1, y2, 14, 16, negativeMaterial); // door
				source.setBlocks(7, 9, y1, y2, 15, 16, negativeMaterial); // hall
			}
		}
		
		// bottom
		if (well.includeBottom())
			source.setBlocksAt(0, floorThicknessInBlocks, wallMaterial);
	}
	
	public static final void populateWalls(WellArchetype well, Random random, Chunk source, int wellChunkX, int wellChunkZ) {
		//TODO teleports, railroads, rooms, doors?
	}
}
