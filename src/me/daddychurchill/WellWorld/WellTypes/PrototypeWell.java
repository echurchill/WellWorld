package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class PrototypeWell extends WellArchetype {

	private int height;
	public PrototypeWell(long seed) {
		super(seed);
		height = random.nextInt(64) + 32;
	}
	
	@Override
	public void populateChunk(World world, ByteChunk chunk) {
		chunk.setBlocksAt(0, height, Material.STONE);
	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
