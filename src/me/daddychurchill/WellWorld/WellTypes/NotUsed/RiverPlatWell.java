package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class RiverPlatWell extends WellArchetype {
	private double xUrbanFactor = 30.0;
	private double zUrbanFactor = 30.0;
	private double xHeightFactor = 20.0;
	private double zHeightFactor = 20.0;
//	private double xUnfinishedFactor = xHeightFactor / 10;
//	private double zUnfinishedFactor = zHeightFactor / 10;
	private double xWaterFactor = 40.0;
	private double zWaterFactor = 40.0;
	private double maxWaterFactor = 0.3;
	private double xRiverFactor = 40.0;
	private double zRiverFactor = 40.0;
	private double minRiverFactor = 0.40;
	private double maxRiverFactor = 0.50;
	
	private SimplexNoiseGenerator generatorUrban;
	private SimplexNoiseGenerator generatorHeight;
//	private SimplexNoiseGenerator generatorUnfinished;
	private SimplexNoiseGenerator generatorRiver;
	private SimplexNoiseGenerator generatorWater;
	
	public RiverPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generatorUrban = new SimplexNoiseGenerator(randseed);
		generatorHeight = new SimplexNoiseGenerator(randseed + 1);
//		generatorUnfinished = new SimplexNoiseGenerator(randseed + 5);
//		generatorFarm = new SimplexNoiseGenerator(randseed + 2);
		generatorWater = new SimplexNoiseGenerator(randseed + 3);
		generatorRiver = new SimplexNoiseGenerator(randseed + 4);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				
				double noiseUrban = generatorUrban.noise(blockX / xUrbanFactor, blockZ / zUrbanFactor);
				double noiseHeight = (generatorHeight.noise(blockX / xHeightFactor, blockZ / zHeightFactor) + 1) / 2;
//				double noiseUnfinished = generatorUnfinished.noise(blockX / xUnfinishedFactor, blockZ / zUnfinishedFactor);
				double noiseWater = (generatorWater.noise(blockX / xWaterFactor, blockZ / zWaterFactor) + 1) / 2;
				double noiseRiver = (generatorRiver.noise(blockX / xRiverFactor, blockZ / zRiverFactor) + 1) / 2;
//				double noiseSelect = noiseWater > maxWaterFactor ? noiseUrban : 1.0; 
//				double noiseSelect = noiseWater > maxWaterFactor ? 0.0 : 1.0; 
				
				chunk.setBlocks(x, 98, 100, z, Material.STONE);
				
//				if (blockX % 5 == 0 || blockZ % 5 == 0)
//					chunk.setBlocks(x, 100, 101, z, Material.SMOOTH_BRICK);
//				
//				else 
				if (noiseWater < maxWaterFactor)
					chunk.setBlocks(x, 100, 101, z, Material.STATIONARY_WATER);
				
				else if (noiseRiver > minRiverFactor && noiseRiver < maxRiverFactor)
					chunk.setBlocks(x, 100, 101, z, Material.STATIONARY_WATER);

				else if (noiseUrban > 0)
//					if (noiseUnfinished > )
					chunk.setBlocks(x, 100, 101 + (int)(noiseHeight * 5), z, Material.IRON_BLOCK);
				else
					chunk.setBlocks(x, 100, 101, z, Material.GRASS);
				
//				if (noiseSelect < 1.0 && noiseRiver > minRiverFactor && noiseRiver < maxRiverFactor)
//					noiseSelect = 1.0;
//				drawPlat(noiseSelect, true, chunk, x, z);
//				drawPlat(noiseSelect, noiseUnfinished > vsUrbanWaterFactor, chunk, x, z);
			}
		}
	}
	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
