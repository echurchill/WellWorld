package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class PlatformWell extends WellArchetype {

	private final static int oddsFloor = 95; // % of the time
	private final static int oddsLiquid = 95; // % of the time
	private final static int oddsLava = 5; // % of the time
	private final static int oddsPlatform = 75; // % of the time
	private final static int oddsConnection = 75; // % of the time
	
	private Material materialFloor = Material.IRON_BLOCK;
	private Material materialColumn = Material.IRON_BLOCK;
	private Material materialPlatform = Material.IRON_BLOCK;
	private Material materialLiquid = Material.STATIONARY_WATER;

	private boolean hasFloor;
	private boolean hasLiquid;
	private int liquidLevel;
	
	public PlatformWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		// ground?
		hasLiquid = calcOdds(oddsLiquid);
		hasFloor = hasLiquid || calcOdds(oddsFloor);
		liquidLevel = calcRandomRange(6, 12);
		
		// swizzle things around
		if (calcOdds(oddsLava))
			materialLiquid = Material.STATIONARY_LAVA;
		if (random.nextBoolean())
			materialColumn = Material.GLASS;
		if (random.nextBoolean())
			materialPlatform = Material.GLASS;
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		
		// add to the floor?
		if (hasFloor) {
			chunk.setBlocksAt(1, materialFloor);
			if (hasLiquid)
				chunk.setBlocksAt(2, liquidLevel, materialLiquid);
		}
		
		// hold's things up
		chunk.setBlocks(7, 9, 1, 127, 7, 9, materialColumn);
		for (int y = 0; y < 127; y += 16)
			if (calcOdds(oddsPlatform)) {
				chunk.setBlocks(4, 12, y, y + 1, 4, 12, materialPlatform);
				if (calcOdds(oddsConnection))
					chunk.setBlocks(7, 9, y, y + 1, 0, 16, materialPlatform);
				if (calcOdds(oddsConnection))
					chunk.setBlocks(0, 16, y, y + 1, 7, 9, materialPlatform);
			}
	}
	
	@Override
	public boolean includeBottom() {
		return hasFloor;
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
