package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class CityRoadsPlatWell extends WellArchetype {

	private double xUrbanFactor = 30.0;
	private double zUrbanFactor = 30.0;
	private double threshholdUrban = 0.50;
	private double xRuralFactor = 30.0;
	private double zRuralFactor = 30.0;
	private double threshholdRural = 0.50;
	
	private double xIntersectionFactor = 6;
	private double zIntersectionFactor = 6;
	private double threshholdRoad = 0.50;
	private double threshholdBridge = 1.25;
	
	private double xHeightFactor = 20.0;
	private double zHeightFactor = 20.0;
	private double maxHeight = 5;
	private double xUnfinishedFactor = xHeightFactor / 10;
	private double zUnfinishedFactor = zHeightFactor / 10;
	private double threshholdUnfinished = 0.15;
	
	private double xWaterFactor = 40.0;
	private double zWaterFactor = 40.0;
	private double threshholdWater = 0.3;
	private double xRiverFactor = 40.0;
	private double zRiverFactor = 40.0;
	private double threshholdMinRiver = 0.40;
	private double threshholdMaxRiver = 0.50;
	
	private SimplexNoiseGenerator generatorUrban;
	private SimplexNoiseGenerator generatorRural;
	private SimplexNoiseGenerator generatorHeight;
	private SimplexNoiseGenerator generatorUnfinished;
	private SimplexNoiseGenerator generatorRiver;
	private SimplexNoiseGenerator generatorWater;
	private SimplexNoiseGenerator generatorIntersection;
	
	public CityRoadsPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generatorUrban = new SimplexNoiseGenerator(randseed);
		generatorRural = new SimplexNoiseGenerator(randseed + 1);
		generatorHeight = new SimplexNoiseGenerator(randseed + 2);
		generatorUnfinished = new SimplexNoiseGenerator(randseed + 3);
		generatorWater = new SimplexNoiseGenerator(randseed + 4);
		generatorRiver = new SimplexNoiseGenerator(randseed + 5);
		generatorIntersection = new SimplexNoiseGenerator(randseed + 6);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int blockX = chunkX * 16 + x;
				int blockZ = chunkZ * 16 + z;
				
				chunk.setBlocks(x, 98, 100, z, Material.GOLD_BLOCK);
				
				if (isLake(blockX, blockZ))
					chunk.setBlocks(x, 99, 100, z, Material.STATIONARY_WATER);
				else if (isRiver(blockX, blockZ))
					chunk.setBlocks(x, 99, 100, z, Material.STATIONARY_WATER);

				if (isRoad(blockX, blockZ))
					chunk.setBlocks(x, 100, 101, z, Material.STONE);
				
				if (isBuildable(blockX, blockZ))
					if (isUrban(blockX, blockZ)) {
						if (isUrbanUnfinished(blockX, blockZ))
							chunk.setBlocks(x, 100, 101 + getUrbanHeight(blockX, blockZ), z, Material.GLASS);
						else
							chunk.setBlocks(x, 100, 101 + getUrbanHeight(blockX, blockZ), z, Material.IRON_BLOCK);
					} else if (isRural(blockX, blockZ))
						chunk.setBlocks(x, 100, 101, z, Material.GRASS);
					else //isSuburban
						chunk.setBlocks(x, 100, 101, z, Material.SANDSTONE);
			}
		}
	}
	
	public boolean isLake(int x, int z) {
		double noiseWater = (generatorWater.noise(x / xWaterFactor, z / zWaterFactor) + 1) / 2;
		return noiseWater < threshholdWater;
	}

	public boolean isRiver(int x, int z) {
		double noiseRiver = (generatorRiver.noise(x / xRiverFactor, z / zRiverFactor) + 1) / 2;
		return noiseRiver > threshholdMinRiver && noiseRiver < threshholdMaxRiver && !isLake(x, z);
	}
	
	public boolean isWater(int x, int z) {
		return isLake(x, z) || isRiver(x, z);
	}
	
	public boolean isBuildable(int x, int z) {
		return !isWater(x, z) && !isRoad(x, z);
	}
	
	public boolean isUrban(int x, int z) {
		double noiseUrban = (generatorUrban.noise(x / xUrbanFactor, z / zUrbanFactor) + 1) / 2;
		return noiseUrban > threshholdUrban;
	}
	
	public boolean isSuburban(int x, int z) {
		return !isUrban(x, z) && !isRural(x, z);
	}
	
	public boolean isRural(int x, int z) {
		double noiseRural = (generatorRural.noise(x / xRuralFactor, z / zRuralFactor) + 1) / 2;
		return noiseRural > threshholdRural;
	}

	public int getUrbanHeight(int x, int z) {
		return (int) ((generatorHeight.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * maxHeight);
	}
	
	public boolean isUrbanUnfinished(int x, int z) {
		double noiseUnfinished = (generatorUnfinished.noise(x / xUnfinishedFactor, z / zUnfinishedFactor) + 1) / 2;
		return noiseUnfinished < threshholdUnfinished;
	}
	
	// connected buildings
	// wall material 
	// window material
	// floor material
	// roof treatment
	// room layout
	// furniture treatment
	// crop material
	
	public static int roadCellSize = 5;
	
	// given a location
	// is it a NS location
	//   find the previous z+n non-water based intersection
	//   find the next z-n non-water based intersection
	
	// is it a EW location
	
	public boolean isRoad(int x, int z) {
		
		// quick test!
		boolean roadExists = x % roadCellSize == 0 || z % roadCellSize == 0;
		
		// not so quick test
		if (roadExists) {
			
			// is this an intersection?
			if (x % roadCellSize == 0 && z % roadCellSize == 0) {
				
				// if roads exists to any of the cardinal directions then we exist
				roadExists = !isWater(x, z);
				
			// north/south road?
			} else if (x % roadCellSize == 0) {
				
				// bridge?
				boolean isBridge = false;
				
				// find previous intersection not in water
				int previousX = x;
				int previousZ = (z / roadCellSize) * roadCellSize;
				while (isWater(previousX, previousZ)) {
					previousZ -= roadCellSize;
					isBridge = true;
				}
				
				// test for northward road
				double previousNoise = getIntersectionNoise(previousX, previousZ - 1);
				if (isWater(previousX, previousZ - 1))
					previousNoise = 0.0;
					
				// find next intersection not in water
				int nextX = x;
				int nextZ = ((z + roadCellSize) / roadCellSize) * roadCellSize;
				while (isWater(nextX, nextZ)) {
					nextZ += roadCellSize;
					isBridge = true;
				}
				
				// test for southward road
				double nextNoise = getIntersectionNoise(nextX, nextZ + 1);
				if (isWater(nextX, nextZ + 1))
					nextNoise = 0.0;
				
				// overwater?
				if (isBridge)
					roadExists = (previousNoise + nextNoise) > threshholdBridge;
				else
					roadExists = (previousNoise + nextNoise) > threshholdRoad;
				
			// east/west road?
			} else { // if (z % roadCellSize == 0)

				// bridge?
				boolean isBridge = false;
				
				// find previous intersection not in water
				int previousX = (x / roadCellSize) * roadCellSize;
				int previousZ = z;
				while (isWater(previousX, previousZ)) {
					previousX -= roadCellSize;
					isBridge = true;
				}
				
				// test for westward road
				double previousNoise = getIntersectionNoise(previousX - 1, previousZ);
				if (isWater(previousX - 1, previousZ))
					previousNoise = 0.0;
					
				// find next intersection not in water
				int nextX = ((x + roadCellSize) / roadCellSize) * roadCellSize;
				int nextZ = z;
				while (isWater(nextX, nextZ)) {
					nextX += roadCellSize;
					isBridge = true;
				}
				
				// test for eastward road
				double nextNoise = getIntersectionNoise(nextX + 1, nextZ);
				if (isWater(nextX + 1, nextZ))
					nextNoise = 0.0;
				
				// overwater?
				if (isBridge)
					roadExists = (previousNoise + nextNoise) > threshholdBridge;
				else
					roadExists = (previousNoise + nextNoise) > threshholdRoad;
			}
		}
		
		// tell the world
		return roadExists;
	}
	
	private double getIntersectionNoise(int x, int z) {
		return (generatorIntersection.noise(x / xIntersectionFactor, z / zIntersectionFactor) + 1) / 2;
	}
	
	
//	public boolean isRoad(int x, int z) {
//		
//		// quick test
//		boolean roadExists = x % roadCellSize == 0 || z % roadCellSize == 0;
//		
//		// not so quick test
//		if (roadExists) {
//			
//			// is this an intersection
//			if (x % roadCellSize == 0 && z % roadCellSize == 0) {
//				
//				// over land?
//				if (!isWater(x, z))
//					roadExists = true;
//				else {
//					roadExists = false;
//					//   is it over water?
//					//     does the intersection (keep looking until over land) to the
//					//       north && south support southern && northern roads
//					//       OR east && west support western && eastern roads
//				}
//			
//			// else is it a bridge?
//			} else if (isWater(x, z)) {
//				if (x % roadCellSize == 0) { // North/South road
////					double prevIntersection = getIntersection(x, z / roadCellSize);
////					double nextIntersection = getIntersection(x, (z + roadCellSize) / roadCellSize);
////					return isDirection(nextIntersection, roadDirection.SOUTH) &&
////							isDirection(prevIntersection, roadDirection.NORTH);
////					
//				} else { // (z % roadCellSize == 0) { // West/East road
////					double prevIntersection = getIntersection(x / roadCellSize, z);
////					double nextIntersection = getIntersection((x + roadCellSize) / roadCellSize, z);
////					return isDirection(nextIntersection, roadDirection.WEST) &&
////							isDirection(prevIntersection, roadDirection.EAST);
//				}
//				roadExists = false;
//				
//			// okay, then it is just a plain road then
//			} else {
//				// else does the intersection to the
//				//   north support a southern road
//				//   south support a northern road
//				//   east support a western road
//				//   west support a eastern road
//				if (x % roadCellSize == 0) { // North/South road
//					double prevIntersection = getIntersection(x, z, 0, -roadCellSize);
//					double nextIntersection = getIntersection(x, z, 0,  roadCellSize);
//					roadExists = isDirection(nextIntersection, roadDirection.SOUTH) &&
//							isDirection(prevIntersection, roadDirection.NORTH);
//					
//				} else { // (z % roadCellSize == 0) { // West/East road
//					double prevIntersection = getIntersection(x, z, -roadCellSize, 0);
//					double nextIntersection = getIntersection(x, z,  roadCellSize, 0);
//					roadExists = isDirection(nextIntersection, roadDirection.WEST) &&
//							isDirection(prevIntersection, roadDirection.EAST);
//				}
//			}
//		}
//			
//		return roadExists;
//	}
//	
//	private enum roadDirection {NORTH, SOUTH, EAST, WEST};
//	
//	private double getIntersection(int x, int z, int deltaX, int deltaZ) {
//		int testX = getFixedIntersectionCoordinate(x + deltaX);
//		int testZ = getFixedIntersectionCoordinate(z + deltaZ);
//		while (isWater(testX, testZ)) {
//			testX = getFixedIntersectionCoordinate(testX + deltaX);
//			testZ = getFixedIntersectionCoordinate(testZ + deltaZ);
//		}
//		return getIntersectionNoise(testX, testZ);
//	}
//	
//	private int getFixedIntersectionCoordinate(int i) {
//		return (i / roadCellSize) * roadCellSize;
//	}
//	
//	
//	private boolean isDirection(double intersectionNoise, roadDirection direction) {
//		// reference: notes/notes.xlsx
//		int intersection = NoiseGenerator.floor(intersectionNoise * 6);
//		switch (direction) {
//		case NORTH:
//			switch (intersection) {
//			case 1:
////			case 5:
////			case 8:
//				return false;
//			default:
//				return true;
//			}
//		case SOUTH:
//			switch (intersection) {
//			case 3:
////			case 6:
////			case 7:
//				return false;
//			default:
//				return true;
//			}
//		case EAST:
//			switch (intersection) {
//			case 2:
////			case 5:
////			case 6:
//				return false;
//			default:
//				return true;
//			}
//		case WEST:
//			switch (intersection) {
//			case 4:
////			case 7:
////			case 8:
//				return false;
//			default:
//				return true;
//			}
//		default:
//			// this will never happen but we need to keep "LINT" happy
//			return false;
//		}
//	}
//	
////	private boolean isDirection(double intersectionNoise, roadDirection direction) {
////		// reference: notes/notes.xlsx
////		int intersection = NoiseGenerator.floor(intersectionNoise * 9);
////		switch (direction) {
////		case NORTH:
////			switch (intersection) {
////			case 0:
////			case 2:
////			case 3:
////			case 4:
////			case 6:
////			case 7:
////				return true;
////			default:
////				return false;
////			}
////		case SOUTH:
////			switch (intersection) {
////			case 0:
////			case 1:
////			case 2:
////			case 4:
////			case 5:
////			case 8:
////				return true;
////			default:
////				return false;
////			}
////		case EAST:
////			switch (intersection) {
////			case 0:
////			case 1:
////			case 3:
////			case 4:
////			case 7:
////			case 8:
////				return true;
////			default:
////				return false;
////			}
////		case WEST:
////			switch (intersection) {
////			case 0:
////			case 1:
////			case 2:
////			case 3:
////			case 5:
////			case 6:
////				return true;
////			default:
////				return false;
////			}
////		default:
////			return false;
////		}
////	}
//	
//	private boolean isRoadToThe(int x, int z, int deltaX, int deltaZ, roadDirection direction) {
////		double intersectionA = 
//		return false;
//	}
//	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
