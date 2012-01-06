package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VerySimpleWaterWell extends WellArchetype {

	private int mineralCount; // how many random minerals to pepper the chunk with for each y-layer
	private int height; // how thick is the bottom bit
	private int waterLevel; // how thick is the water bit
	
	public VerySimpleWaterWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		mineralCount = random.nextInt(4) + 4;
		height = random.nextInt(48) + 16;
		waterLevel = random.nextInt(8) + height;
	}
	
	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		// fill up with stone (don't need to do 0 as that will be handled for me
		chunk.setBlocksAt(1, height, Material.STONE);
		
		// sprinkle minerals for each y layer
		for (int y = 1; y < height; y++) {
			for (int m = 0; m < mineralCount; m++) {
				chunk.setBlock(random.nextInt(16), y, random.nextInt(16), pickRandomMineralAt(y));
			}
		}
		
		// add water on top
		chunk.setBlocksAt(height, waterLevel, Material.WATER);
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
