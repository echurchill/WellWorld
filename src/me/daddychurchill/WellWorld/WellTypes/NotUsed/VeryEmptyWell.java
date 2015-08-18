package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class VeryEmptyWell extends WellArchetype {

	public VeryEmptyWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
