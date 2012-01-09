package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class PlatformWell extends WellArchetype {

	private final static int oddsFloor = 95; // % of the time
	private final static int oddsLiquid = 95; // % of the time
	private final static int oddsLava = 5; // % of the time
	private final static int oddsPlatform = 75; // % of the time
	private final static int oddsConnection = 75; // % of the time
	
	private byte byteFloor = (byte) Material.IRON_BLOCK.getId();
	private byte byteColumn = (byte) Material.IRON_BLOCK.getId();
	private byte bytePlatform = (byte) Material.IRON_BLOCK.getId();
	private byte byteLiquid = (byte) Material.STATIONARY_WATER.getId();

	private boolean hasFloor;
	private boolean hasLiquid;
	private int liquidLevel;
	
	private byte byteGlass = (byte) Material.GLASS.getId();
	private byte byteLava = (byte) Material.STATIONARY_LAVA.getId();

	public PlatformWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		// ground?
		hasLiquid = calcOdds(oddsLiquid);
		hasFloor = hasLiquid || calcOdds(oddsFloor);
		liquidLevel = calcRandomRange(6, 12);
		
		// swizzle things around
		if (calcOdds(oddsLava))
			byteLiquid = byteLava;
		if (random.nextBoolean())
			byteColumn = byteGlass;
		if (random.nextBoolean())
			bytePlatform = byteGlass;
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		
		// add to the floor?
		if (hasFloor) {
			chunk.setBlocksAt(1, byteFloor);
			if (hasLiquid)
				chunk.setBlocksAt(2, liquidLevel, byteLiquid);
		}
		
		// hold's things up
		chunk.setBlocks(7, 9, 1, 127, 7, 9, byteColumn);
		for (int y = 0; y < 127; y += 16)
			if (calcOdds(oddsPlatform)) {
				chunk.setBlocks(4, 12, y, y + 1, 4, 12, bytePlatform);
				if (calcOdds(oddsConnection))
					chunk.setBlocks(7, 9, y, y + 1, 0, 16, bytePlatform);
				if (calcOdds(oddsConnection))
					chunk.setBlocks(0, 16, y, y + 1, 7, 9, bytePlatform);
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
