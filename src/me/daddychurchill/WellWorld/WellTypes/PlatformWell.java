package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class PlatformWell extends WellArchetype {

	private final static int oddsFloor = 50; // % of the time
	private final static int oddsPlatform = 75; // % of the time
	private final static int oddsConnection = 75; // % of the time
	private byte byteFloor = (byte) Material.IRON_BLOCK.getId();
	private byte byteColumn = (byte) Material.IRON_BLOCK.getId();
	private byte bytePlatform = (byte) Material.IRON_BLOCK.getId();
	private boolean hasFloor;
	
	public PlatformWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		hasFloor = random.nextInt(100) < oddsFloor;
	}

	@Override
	public void populateChunk(ByteChunk chunk) {
		
		// hold's things up
		chunk.setBlocks(7, 9, 0, 127, 7, 9, byteColumn);
		for (int y = 0; y < 127; y += 16)
			if (random.nextInt(100) < oddsPlatform) {
				chunk.setBlocks(4, 12, y, y + 1, 4, 12, bytePlatform);
				if (random.nextInt(100) < oddsConnection)
					chunk.setBlocks(7, 9, y, y + 1, 0, 16, bytePlatform);
				if (random.nextInt(100) < oddsConnection)
					chunk.setBlocks(0, 16, y, y + 1, 7, 9, bytePlatform);
			}
		
		// draw the floor?
		if (hasFloor)
			chunk.setBlocksAt(0, byteFloor);
	}
	
	@Override
	public boolean includeBottom() {
		return false;
	}

	@Override
	public void populateBlocks(Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
