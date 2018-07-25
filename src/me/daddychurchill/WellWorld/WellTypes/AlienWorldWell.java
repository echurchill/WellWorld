package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class AlienWorldWell extends WellArchetype {

	private int mineralOdds; // 1/n chance that there is minerals on this level
	private int mineralsPerLayer; // number of minerals per layer
	private int height; // how thick is the bottom bit
	private int liquidLevel; // how thick is the water bit
	private Material stoneMaterial; // what is the stone made of?
	private Material liquidMaterial; // what is the liquid made of?
	
	private double xFactor = 50.0;
	private double yFactor = 25.0;
	private double zFactor = 50.0;
	private SimplexNoiseGenerator generator;
	
	public AlienWorldWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		mineralOdds = random.nextInt(5) + 1;
		mineralsPerLayer = random.nextInt(10);
		height = random.nextInt(32) + 48;
		liquidLevel = random.nextInt(height / 2) + height / 2;
		
		switch (random.nextInt(7)) {
		case 1:
			stoneMaterial = Material.CLAY;
			liquidMaterial = Material.LAVA;
			break;
		case 2:
			stoneMaterial = Material.SPONGE;
			liquidMaterial = Material.WATER;
			break;
		case 3:
			stoneMaterial = Material.ICE;
			liquidMaterial = Material.WATER;
			break;
		case 4:
			stoneMaterial = Material.NETHERRACK;
			liquidMaterial = Material.LAVA;
			break;
		case 5:
			stoneMaterial = Material.OBSIDIAN;
			liquidMaterial = Material.LAVA;
			break;
		case 6:
			stoneMaterial = Material.LAPIS_BLOCK;
			liquidMaterial = Material.WATER;
			break;
		case 7:
			stoneMaterial = Material.END_STONE;
			liquidMaterial = Material.ICE;
			break;
		default:
			stoneMaterial = Material.STONE;
			liquidMaterial = Material.WATER;
			break;
		}
		
		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 128; y++) {
					double noise = generator.noise((chunkX * 16 + x) / xFactor, y / yFactor, (chunkZ * 16 + z) / zFactor);
					noise += (height - y) * (1.0 / 32.0);
					
					if (noise >= 0)
						chunk.setBlock(x, y, z, stoneMaterial);
					else if (y < liquidLevel)
						chunk.setBlock(x, y, z, liquidMaterial);
				}
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
						block.setType(pickRandomMineralAt(y), false);
				}
			}
		}
	}
}
