package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VeryEmptyWell extends WellArchetype {

	public VeryEmptyWell(long seed, int wellX, int wellZ) {
		super(seed, wellX, wellZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populateChunk(World world, ByteChunk chunk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
