package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class CityRoadsPlatWell extends WellArchetype {

	private double xUrbanFactor = 30.0;
	private double zUrbanFactor = 30.0;
	private double oddsUrbanFactor = 0.50;
	private double xRuralFactor = 30.0;
	private double zRuralFactor = 30.0;
	private double oddsRuralFactor = 0.50;
	
	private double xIntersectionFactor = 20;
	private double zIntersectionFactor = 20;
	
	private double xHeightFactor = 20.0;
	private double zHeightFactor = 20.0;
	private double maxHeight = 5;
	private double xUnfinishedFactor = xHeightFactor / 10;
	private double zUnfinishedFactor = zHeightFactor / 10;
	private double oddsUnfinished = 0.15;
	
	private double xWaterFactor = 40.0;
	private double zWaterFactor = 40.0;
	private double maxWaterFactor = 0.3;
	private double xRiverFactor = 40.0;
	private double zRiverFactor = 40.0;
	private double minRiverFactor = 0.40;
	private double maxRiverFactor = 0.50;
	
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
		return noiseWater < maxWaterFactor;
	}

	public boolean isRiver(int x, int z) {
		double noiseRiver = (generatorRiver.noise(x / xRiverFactor, z / zRiverFactor) + 1) / 2;
		return noiseRiver > minRiverFactor && noiseRiver < maxRiverFactor && !isLake(x, z);
	}
	
	public boolean isWater(int x, int z) {
		return isLake(x, z) || isRiver(x, z);
	}
	
	public boolean isBuildable(int x, int z) {
		return !isWater(x, z) && !isRoad(x, z);
	}
	
	public boolean isUrban(int x, int z) {
		double noiseUrban = (generatorUrban.noise(x / xUrbanFactor, z / zUrbanFactor) + 1) / 2;
		return noiseUrban > oddsUrbanFactor;
	}
	
	public boolean isSuburban(int x, int z) {
		return !isUrban(x, z) && !isRural(x, z);
	}
	
	public boolean isRural(int x, int z) {
		double noiseRural = (generatorRural.noise(x / xRuralFactor, z / zRuralFactor) + 1) / 2;
		return noiseRural > oddsRuralFactor;
	}

	public int getUrbanHeight(int x, int z) {
		return (int) ((generatorHeight.noise(x / xHeightFactor, z / zHeightFactor) + 1) / 2 * maxHeight);
	}
	
	public boolean isUrbanUnfinished(int x, int z) {
		double noiseUnfinished = (generatorUnfinished.noise(x / xUnfinishedFactor, z / zUnfinishedFactor) + 1) / 2;
		return noiseUnfinished < oddsUnfinished;
	}
	
	// connected buildings
	// wall material 
	// window material
	// floor material
	// roof treatment
	// room layout
	// furniture treatment
	// crop material
	
	public boolean isRoad(int x, int z) {
		
		// quick test
		boolean roadExists = x % 5 == 0 || z % 5 == 0;
		
		// not so quick test
		if (roadExists) {
			// is this an intersection
			if (x % 5 == 0 && z % 5 == 0) {
				// over land?
				if (!isWater(x, z))
					roadExists = true;
				else {
					roadExists = false;
					//   is it over water?
					//     does the intersection (keep looking until over land) to the
					//       north && south support southern && northern roads
					//       OR east && west support western && eastern roads
				}
			} else {
				roadExists = false;
			// else does the intersection to the
			//   north support a southern road
			//   south support a northern road
			//   east support a western road
			//   west support a eastern road
			}
		}
			
		return roadExists;
	}
	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
