package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class CityRoadsPlatWell extends WellArchetype {

	private final static double xUrbanFactor = 30.0;
	private final static double zUrbanFactor = 30.0;
	private final static double threshholdUrban = 0.50;
	private final static double xUrbanMaterialFactor = 5.0;
	private final static double zUrbanMaterialFactor = 5.0;
	
	private final static double xRuralFactor = 30.0;
	private final static double zRuralFactor = 30.0;
	private final static double threshholdRural = 0.50;
	
	public final static int roadCellSize = 4;
	private final static double xIntersectionFactor = 6;
	private final static double zIntersectionFactor = 6;
	private final static double threshholdRoad = 0.75;
	private final static double threshholdBridge = 1.20;
	private final static double threshholdBridgeLength = 0.10;
	
	private final static double xHeightFactor = 20.0;
	private final static double zHeightFactor = 20.0;
	private final static double maxHeight = 5;
	private final static double xUnfinishedFactor = xHeightFactor / 10;
	private final static double zUnfinishedFactor = zHeightFactor / 10;
	private final static double threshholdUnfinished = 0.15;
	
	private final static double xWaterFactor = 40.0;
	private final static double zWaterFactor = 40.0;
	private final static double threshholdWater = 0.3;
	private final static double xRiverFactor = 40.0;
	private final static double zRiverFactor = 40.0;
	private final static double threshholdMinRiver = 0.40;
	private final static double threshholdMaxRiver = 0.50;
	
	private SimplexNoiseGenerator noiseUrban;
	private SimplexNoiseGenerator noiseRural;
	private SimplexNoiseGenerator noiseUnfinished;
	private SimplexNoiseGenerator noiseRiver;
	private SimplexNoiseGenerator noiseWater;
	private SimplexNoiseGenerator noiseIntersection;
	
	private SimplexNoiseGenerator noiseHeightDeviance; // add/subtract a little from the normal height for this building
	private SimplexNoiseGenerator noiseUrbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseSuburbanMaterial; // which building material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseRuralMaterial; // which building/plat material set to use (if an adjacent chunk is similar then join them)
//	private SimplexNoiseGenerator noiseBridgeStyle; // search to the previous (west or north) starting intersection and use it's location for generator
	
	// Sculpted terrain... gradual slopes, maybe key off of generatorWater but not stretch the value as much? 
	
	public CityRoadsPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		//seed = -7311321900194778047L;
		noiseUrban = new SimplexNoiseGenerator(seed);
		noiseRural = new SimplexNoiseGenerator(seed + 1);
		noiseUnfinished = new SimplexNoiseGenerator(seed + 2);
		noiseWater = new SimplexNoiseGenerator(seed + 3);
		noiseRiver = new SimplexNoiseGenerator(seed + 4);
		noiseIntersection = new SimplexNoiseGenerator(seed + 5);
		noiseHeightDeviance = new SimplexNoiseGenerator(seed + 6);
		noiseUrbanMaterial = new SimplexNoiseGenerator(seed + 7);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				
				chunk.setBlocks(x, 0, 98, z, Material.GOLD_BLOCK);
				
				if (isLake(blockX, blockZ))
					chunk.setBlocks(x, 98, 100, z, Material.WATER);
				else if (isRiver(blockX, blockZ))
					chunk.setBlocks(x, 98, 100, z, Material.WATER);
				else 
					chunk.setBlocks(x, 98, 100, z, Material.DIRT);

				if (isRoad(blockX, blockZ))
					chunk.setBlocks(x, 100, 101, z, Material.STONE);
				else if (isBuildable(blockX, blockZ))
					if (isUrban(blockX, blockZ)) {
						int height = getUrbanHeight(blockX, blockZ);
						Material wallMaterial = getUrbanWallMaterial(blockX, blockZ);
						Material floorMaterial = getUrbanFloorMaterial(blockX, blockZ);
						for (int i = 0; i < height; i++) {
							int y = i * 2;
							chunk.setBlock(x, 100 + y, z, floorMaterial);
							chunk.setBlock(x, 100 + y + 1, z, wallMaterial);
						}
//						chunk.setBlocks(x, 100, 101 + getUrbanHeight(blockX, blockZ), z, Material.IRON_BLOCK);
					} else if (isRural(blockX, blockZ))
						chunk.setBlocks(x, 100, 101, z, Material.GRASS_BLOCK);
					else //isSuburban
						chunk.setBlocks(x, 100, 101, z, Material.SANDSTONE);
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
	
	public boolean isUrban(int x, int z) {
		double noiseLevel = (noiseUrban.noise(x / xUrbanFactor, z / zUrbanFactor) + 1) / 2;
		return noiseLevel > threshholdUrban;
	}
	
	public boolean isSuburban(int x, int z) {
		return !isUrban(x, z) && !isRural(x, z);
	}
	
	public boolean isRural(int x, int z) {
		double noiseLevel = (noiseRural.noise(x / xRuralFactor, z / zRuralFactor) + 1) / 2;
		return noiseLevel > threshholdRural;
	}

	public int getUrbanHeight(int x, int z) {
		return (int) ((noiseHeightDeviance.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * maxHeight);
	}
	
	public Material getUrbanWallMaterial(int x, int z) {
		double noiseLevel = (noiseUrbanMaterial.noise(x / xUrbanMaterialFactor, 0, z / zUrbanMaterialFactor) + 1) / 2;
		switch (NoiseGenerator.floor(noiseLevel * 5)) {
		case 1:
			return Material.BRICKS;
		case 2:
			return Material.COBBLESTONE;
		case 3:
			return Material.NETHER_BRICKS;
		case 4:
			return Material.NETHERRACK;
		default:
			return Material.STONE_BRICKS;
		}
	}
	
	public Material getUrbanFloorMaterial(int x, int z) {
		double noiseLevel = (noiseUrbanMaterial.noise(x / xUrbanMaterialFactor, 1, z / zUrbanMaterialFactor) + 1) / 2;
		switch (NoiseGenerator.floor(noiseLevel * 3)) {
		case 1:
			return Material.BRICKS;
		case 2:
			return Material.COBBLESTONE;
		default:
			return Material.STONE_BRICKS;
		}
	}
	
	public boolean isUrbanUnfinished(int x, int z) {
		double noiseLevel = (noiseUnfinished.noise(x / xUnfinishedFactor, z / zUnfinishedFactor) + 1) / 2;
		return noiseLevel < threshholdUnfinished;
	}
	
	private boolean checkRoadCoordinate(int i) {
		return i % roadCellSize == 0;
	}
	
	private int fixRoadCoordinate(int i) {
		if (i < 0) {
			return -((Math.abs(i + 1) / roadCellSize) * roadCellSize + roadCellSize);
		} else
			return (i / roadCellSize) * roadCellSize;
	}
	
	public boolean isRoad(int x, int z) {

		// quick test!
		boolean roadExists = checkRoadCoordinate(x) || checkRoadCoordinate(z);
		
		// not so quick test
		if (roadExists) {
			
			// is this an intersection?
			if (checkRoadCoordinate(x) && checkRoadCoordinate(z)) {
				
				// if roads exists to any of the cardinal directions then we exist
				roadExists = isRoad(x - 1, z) ||
							 isRoad(x + 1, z) ||
							 isRoad(x, z - 1) ||
							 isRoad(x, z + 1);
				
			} else {
				
				// bridge?
				boolean isBridge = false;
				int previousX, previousZ, nextX, nextZ;
				double previousNoise, nextNoise, lengthNoise = 0.0;
				
				// north/south road?
				if (checkRoadCoordinate(x)) {
					
					// find previous intersection not in water
					previousX = x;
					previousZ = fixRoadCoordinate(z);
					while (isWater(previousX, previousZ)) {
						previousZ -= roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for northward road
					if (isWater(previousX, previousZ + 1))
						previousNoise = 0.0;
					else
						previousNoise = getIntersectionNoise(previousX, previousZ + 1);
						
					// find next intersection not in water
					nextX = x;
					nextZ = fixRoadCoordinate(z + roadCellSize);
					while (isWater(nextX, nextZ)) {
						nextZ += roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for southward road
					if (isWater(nextX, nextZ - 1))
						nextNoise = 0.0;
					else
						nextNoise = getIntersectionNoise(nextX, nextZ - 1);
					
				// east/west road?
				} else { // if (checkRoadCoordinate(z))
	
					// find previous intersection not in water
					previousX = fixRoadCoordinate(x);
					previousZ = z;
					while (isWater(previousX, previousZ)) {
						previousX -= roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for westward road
					if (isWater(previousX + 1, previousZ))
						previousNoise = 0.0;
					else
						previousNoise = getIntersectionNoise(previousX + 1, previousZ);
						
					// find next intersection not in water
					nextX = fixRoadCoordinate(x + roadCellSize);
					nextZ = z;
					while (isWater(nextX, nextZ)) {
						nextX += roadCellSize;
						isBridge = true;
						lengthNoise = lengthNoise + threshholdBridgeLength;
					}
					
					// test for eastward road
					if (isWater(nextX - 1, nextZ))
						nextNoise = 0.0;
					else
						nextNoise = getIntersectionNoise(nextX - 1, nextZ);
				}
				
				// overwater?
				if (isBridge)
					roadExists = (previousNoise + nextNoise) > (threshholdBridge + lengthNoise); // longer bridges are "harder"
				else
					roadExists = (previousNoise + nextNoise) > threshholdRoad;
			}
		}
		
		// tell the world
		return roadExists;
	}
	
	private double getIntersectionNoise(int x, int z) {
		return (noiseIntersection.noise(x / xIntersectionFactor, z / zIntersectionFactor) + 1) / 2;
	}
	

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
