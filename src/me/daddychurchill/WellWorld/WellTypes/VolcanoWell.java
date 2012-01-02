package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class VolcanoWell extends WellArchetype {

    private Material interiorMaterial;
    private Material surfaceMaterial;
    private Material floodMaterial;
    private Material liquidMaterial;
    private int surfaceAt;
    private int surfaceThickness;
    private double surfaceVariance;
    private int peakX;
    private int peakZ;
    
	public VolcanoWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		surfaceAt = calcRandomRange(20, 60);
		surfaceThickness = random.nextInt(3) + 1;
		surfaceVariance = calcRandomRange(4.0, 7.0);
		interiorMaterial = Material.COBBLESTONE;
		surfaceMaterial = Material.STONE;
		floodMaterial = Material.LAVA;
		liquidMaterial = Material.WATER;
		
		peakX = (maxBlock.getBlockX() - minBlock.getBlockX()) / 2 + minBlock.getBlockX();
		peakZ = (maxBlock.getBlockZ() - minBlock.getBlockZ()) / 2 + minBlock.getBlockZ();
	}

    private NoiseGenerator generator;
    
    private int getHeight(double x, double z, double variance) {
        if (generator == null)
            generator = new SimplexNoiseGenerator(randseed);
        
        return NoiseGenerator.floor(generator.noise(x, z) * variance);
    }

	@Override
	public void populateChunk(ByteChunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();
		
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
            	
            	
            	
//                int height = getHeight(chunkX + x * 0.0625, chunkZ + z * 0.0625, surfaceVariance) + surfaceAt;
//                chunk.setBlocks(x, 1, height - surfaceThickness, z, interiorMaterial);
//                chunk.setBlocks(x, height - surfaceThickness, height, z, surfaceMaterial);
            }
        }
	}

	@Override
	public void populateBlocks(Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
