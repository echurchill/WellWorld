package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VerySimpleHillyWell extends WellArchetype {

	private int mineralOdds; // 1/n chance that there is minerals on this level
	private int mineralsPerLayer; // number of minerals per layer
	private int height; // how thick is the bottom bit
	private int liquidLevel; // how thick is the water bit
	private final static Material stoneMaterial = Material.STONE; // what is the stone made of?
	private final static Material liquidMaterial = Material.STATIONARY_WATER; // what is the liquid made of?
	
	private int octives = 3;
	private double xFactor = 1.0;
	private double zFactor = 1.0;
	private double fequency = 0.5;
	private double amplitude = 0.5;
	private double hScale = 1.0 / 64.0;
	private double vScale = 16.0;
	private SimplexOctaveGenerator generator;
	
	public VerySimpleHillyWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		mineralOdds = random.nextInt(5) + 1;
		mineralsPerLayer = random.nextInt(10);
		height = random.nextInt(32) + 48;
		liquidLevel = random.nextInt(height / 2) + height / 2;
		
		generator = new SimplexOctaveGenerator(randseed, octives);
		generator.setScale(hScale);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, fequency, amplitude) * vScale;
				int y = height + (int)noise;
				chunk.setBlocks(x, 1, y, z, stoneMaterial);
				if (y < liquidLevel)
					chunk.setBlocks(x, y, liquidLevel, z, liquidMaterial);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {

		// sprinkle minerals for each y layer, one of millions of ways to do this!
		for (int y = 1; y < 127; y++) {
			if (random.nextInt(mineralOdds) == 0) {
				for (int i = 0; i < mineralsPerLayer; i++) {
					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
					if (block.getType() == stoneMaterial)
						block.setTypeId(pickRandomMineralAt(y).getId(), false);
				}
			}
		}
	}

}
