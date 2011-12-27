package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class WaterWell extends WellArchetype {

	private int waterLevel;
	public WaterWell(long seed) {
		super(seed);
		waterLevel = random.nextInt(64) + 32;
	}
	
	@Override
	public void populateChunk(World world, ByteChunk chunk) {
		chunk.setBlocksAt(0, Material.STONE);
		chunk.setBlocksAt(1, waterLevel, Material.WATER);
	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
