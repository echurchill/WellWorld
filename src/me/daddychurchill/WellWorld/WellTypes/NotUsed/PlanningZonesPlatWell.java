package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class PlanningZonesPlatWell extends WellArchetype {

	private final static double xUrbanizationFactor = 30.0;
	private final static double zUrbanizationFactor = 30.0;
	private final static double threshholdCity = 0.60;
	private final static double threshholdResidence = 0.35;
//	private final static double xUrbanMaterialFactor = 5.0;
//	private final static double zUrbanMaterialFactor = 5.0;
	
	private final static double xGreenBeltFactor = 30.0;
	private final static double zGreenBeltFactor = 30.0;
	private final static double threshholdGreenBeltMin = 0.40;
	private final static double threshholdGreenBeltMax = 0.50;
	
	public final static int roadCellSize = 4;
//	private final static double xIntersectionFactor = 6;
//	private final static double zIntersectionFactor = 6;
//	private final static double threshholdRoad = 0.75;
//	private final static double threshholdBridge = 1.20;
//	private final static double threshholdBridgeLength = 0.10;
	
//	private final static double xHeightFactor = 20.0;
//	private final static double zHeightFactor = 20.0;
//	private final static double maxHeight = 5;
//	private final static double xUnfinishedFactor = xHeightFactor / 10;
//	private final static double zUnfinishedFactor = zHeightFactor / 10;
//	private final static double threshholdUnfinished = 0.15;
	
	private final static double xWaterFactor = 40.0;
	private final static double zWaterFactor = 40.0;
	private final static double threshholdWater = 0.3;
	private final static double xRiverFactor = 40.0;
	private final static double zRiverFactor = 40.0;
	private final static double threshholdMinRiver = 0.40;
	private final static double threshholdMaxRiver = 0.50;
	
	private SimplexNoiseGenerator noiseUrbanization;
	private SimplexNoiseGenerator noiseGreenBelt;
	private SimplexNoiseGenerator noiseRiver;
	private SimplexNoiseGenerator noiseWater;
//	private SimplexNoiseGenerator noiseIntersection;
//	private SimplexNoiseGenerator noiseUnfinished;
	
//	private SimplexNoiseGenerator noiseHeightDeviance; // add/subtract a little from the normal height for this building
//	private SimplexNoiseGenerator noiseUrbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseSuburbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseRuralMaterial; // which building/plat material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseBridgeStyle; // search to the previous (west or north) starting intersection and use it's location for generator
	
	// Sculpted terrain... gradual slopes, maybe key off of generatorWater but not stretch the value as much? 
	
	public PlanningZonesPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		//seed = -7311321900194778047L;
		noiseUrbanization = new SimplexNoiseGenerator(seed);
		noiseGreenBelt = new SimplexNoiseGenerator(seed + 1);
//		noiseUnfinished = new SimplexNoiseGenerator(seed + 2);
		noiseWater = new SimplexNoiseGenerator(seed + 3);
		noiseRiver = new SimplexNoiseGenerator(seed + 4);
//		noiseIntersection = new SimplexNoiseGenerator(seed + 5);
//		noiseHeightDeviance = new SimplexNoiseGenerator(seed + 6);
//		noiseUrbanMaterial = new SimplexNoiseGenerator(seed + 7);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				
				chunk.setBlocks(x, 0, 98, z, Material.GOLD_BLOCK);
				
				if (isLake(blockX, blockZ))
					chunk.setBlocks(x, 98, 100, z, Material.STATIONARY_WATER);
				else if (isRiver(blockX, blockZ))
					chunk.setBlocks(x, 98, 100, z, Material.STATIONARY_WATER);
				else 
					chunk.setBlocks(x, 98, 100, z, Material.DIRT);

				if (isRoad(blockX, blockZ))
					chunk.setBlocks(x, 100, 101, z, Material.STONE);
				else if (isBuildable(blockX, blockZ))
					if (isGreenBelt(blockX, blockZ)) {
						if (isUrban(blockX, blockZ))
							chunk.setBlocks(x, 100, 101, z, Material.MOSSY_COBBLESTONE);
						else if (isResidential(blockX, blockZ))
							chunk.setBlocks(x, 100, 101, z, Material.SANDSTONE);
						else if (isFarms(blockX, blockZ))
							chunk.setBlocks(x, 100, 101, z, Material.DIRT);
					} else {
						if (isUrban(blockX, blockZ))
							chunk.setBlocks(x, 100, 103, z, Material.IRON_BLOCK);
						else if (isResidential(blockX, blockZ))
							chunk.setBlocks(x, 100, 102, z, Material.WOOD);
						else if (isFarms(blockX, blockZ))
							chunk.setBlocks(x, 100, 101, z, Material.GRASS);
					}
//				else
//					chunk.setBlocks(x, 100, 101, z, Material.GLOWSTONE);
			}
		}
	}
	
	public boolean isLake(int x, int z) {
		double noiseLevel = (noiseWater.noise(x / xWaterFactor, z / zWaterFactor) + 1) / 2;
		return noiseLevel < threshholdWater;
	}

	public boolean isRiver(int x, int z) {
		double noiseLevel = (noiseRiver.noise(x / xRiverFactor, z / zRiverFactor) + 1) / 2;
		return noiseLevel > threshholdMinRiver && noiseLevel < threshholdMaxRiver && !isLake(x, z);
	}
	
	public boolean isWater(int x, int z) {
		return isLake(x, z) || isRiver(x, z);
	}
	
	public boolean isBuildable(int x, int z) {
		return !isWater(x, z) && !isRoad(x, z);
	}
	
	public boolean isRoad(int x, int z) {
		return false;
	}
	
	public boolean isGreenBelt(int x, int z) {
		double noiseLevel = (noiseGreenBelt.noise(x / xGreenBeltFactor, z / zGreenBeltFactor) + 1) / 2;
		return noiseLevel > threshholdGreenBeltMin && noiseLevel < threshholdGreenBeltMax;
	}

	public boolean isUrban(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel >= threshholdCity;
	}
	
	public boolean isResidential(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel >= threshholdResidence && noiseLevel < threshholdCity;
	}
	
	public boolean isFarms(int x, int z) {
		double noiseLevel = (noiseUrbanization.noise(x / xUrbanizationFactor, z / zUrbanizationFactor) + 1) / 2;
		return noiseLevel < threshholdResidence;
	}
	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
