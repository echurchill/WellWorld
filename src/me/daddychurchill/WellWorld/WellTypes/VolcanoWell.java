package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.WellWorld;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VolcanoWell extends WellArchetype {

	//TODO need to add a chamber
	//TODO need to add a vent from the chamber to crater
	//TODO need to add partially filled crater
	private int mineralOdds; // 1/n chance that there is minerals on this level
	private int mineralsPerLayer; // number of minerals per layer
    private Material interiorMaterial;
    private Material surfaceMaterial;
    private Material floodMaterial;
    private Material liquidMaterial;
    private int baseAt;
    private int liquidAt;
    private int coneScale;
    private int craterAt;
    private int surfaceThickness;
    private double surfaceVariance;
    private NoiseGenerator generator;
    
    private final static double noiseTweak = 0.0625;
    
	public VolcanoWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		baseAt = calcRandomRange(10, 30);
		liquidAt = calcRandomRange(baseAt + 10, baseAt + 40);
		coneScale = calcRandomRange(baseAt + 50, 110) - baseAt;
		craterAt = baseAt + coneScale - calcRandomRange(2, 6);
		surfaceThickness = random.nextInt(3) + 1;
		surfaceVariance = calcRandomRange(4.0, 7.0);
		
		mineralOdds = random.nextInt(5) + 1;
		mineralsPerLayer = random.nextInt(10);
		
		interiorMaterial = Material.STONE;
		surfaceMaterial = Material.COBBLESTONE;
		floodMaterial = Material.LAVA;
		liquidMaterial = Material.STATIONARY_WATER;
		
        generator = new SimplexNoiseGenerator(randseed);
	}

    private int getSurfaceNoise(double x, double z) {
        return NoiseGenerator.floor(generator.noise(x, z) * surfaceVariance);
    }
    
    private final static double TwoPi = Math.PI * 2.0;
    private final static double HalfPi = Math.PI / 2.0;

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
            	double blockX = chunkX * 16 + x;
            	double blockZ = chunkZ * 16 + z;
            	double floatX = blockX / (WellWorld.wellWidthInChunks * chunk.width);
            	double floatZ = blockZ / (WellWorld.wellWidthInChunks * chunk.width);
            	double heightX = (Math.sin(floatX * TwoPi - HalfPi) + 1) / 2;
            	double heightZ = (Math.sin(floatZ * TwoPi - HalfPi) + 1) / 2;
            	int height = baseAt + (int) (heightX * heightZ * coneScale) + 
            			getSurfaceNoise((chunkX * 16 + x) * noiseTweak, (chunkZ * 16 + z) * noiseTweak);
            	if (height < liquidAt) {
                    chunk.setBlocks(x, 1, height, z, interiorMaterial);
                    chunk.setBlocks(x, height, liquidAt, z, liquidMaterial);
            	} else if (height > craterAt) {
            		int craterDepth = height - (int) (heightX * heightZ * coneScale);
                    chunk.setBlocks(x, 1, height - craterDepth, z, interiorMaterial);
                    chunk.setBlocks(x, height - craterDepth, craterAt + 1, z, floodMaterial);
            	} else {
            		chunk.setBlocks(x, 1, height - surfaceThickness, z, interiorMaterial);
            		chunk.setBlocks(x, height - surfaceThickness, height, z, surfaceMaterial);
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
					if (block.getType() == interiorMaterial)
						block.setTypeId(pickRandomMineralAt(y).getId(), false);
				}
			}
		}
	}

}
