package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VeryEmptyWell extends WellArchetype {

	public VeryEmptyWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populateChunk(ByteChunk chunk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateBlocks(Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
