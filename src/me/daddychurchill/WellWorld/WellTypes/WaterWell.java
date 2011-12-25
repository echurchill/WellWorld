package me.daddychurchill.WellWorld.WellTypes;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellManager;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class WaterWell extends WellManager {

	private int waterLevel;
	public WaterWell(Random random) {
		super(random);
		waterLevel = random.nextInt(64) + 32;
	}
	
	@Override
	public void populateChunk(World world, Random random, ByteChunk chunk) {
		chunk.setBlocksAt(0, Material.STONE);
		chunk.setBlocksAt(1, waterLevel, Material.WATER);
	}

	@Override
	public void populateBlocks(World world, Random random, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
