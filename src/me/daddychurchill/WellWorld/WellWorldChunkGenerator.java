package me.daddychurchill.WellWorld;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import me.daddychurchill.WellWorld.Support.InitialBlocks;
import me.daddychurchill.WellWorld.WellTypes.AlienCavernWell;
import me.daddychurchill.WellWorld.WellTypes.AlienWorldWell;
import me.daddychurchill.WellWorld.WellTypes.BasaltFieldWell;
import me.daddychurchill.WellWorld.WellTypes.ForestWell;
import me.daddychurchill.WellWorld.WellTypes.KnollsWell;
import me.daddychurchill.WellWorld.WellTypes.PlatformWell;
import me.daddychurchill.WellWorld.WellTypes.RealisticMoonWell;
import me.daddychurchill.WellWorld.WellTypes.SmoothSnowWell;
import me.daddychurchill.WellWorld.WellTypes.VolcanoWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaForestWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaIceWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaOctaveWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaSkyWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaTrigWell;
import me.daddychurchill.WellWorld.WellTypes.Codename_B.BananaVoidWell;
import me.daddychurchill.WellWorld.WellTypes.Khyperia.KhylandWell;
import me.daddychurchill.WellWorld.WellTypes.Khyperia.PancakeWell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class WellWorldChunkGenerator extends ChunkGenerator {
	public static final int wallThicknessInBlocks = 8;
	public static final int wallHeightInBlocks = 128;
	public static final int floorThicknessInBlocks = 1;
	public static final int ceilingThicknessInBlocks = 1;
	public static final int portalLevels = 3; // how many levels of portals are there
	public static final int lowestDoors = wallHeightInBlocks - 2 * (wallHeightInBlocks / wallThicknessInBlocks);

	private WellWorld plugin;
	private String worldname;
	private String worldstyle;
	
	Material wallFloorMaterial;
	Material wallMaterial;
	Material wallNegativeMaterial;
	boolean wallDoorways;
	boolean hexishWells;
//	private int[] sums;
	
	private SimplexNoiseGenerator generator;
	
	public WellWorldChunkGenerator(WellWorld instance, String name, String style){
		this.plugin = instance;
		this.worldname = name;
		this.worldstyle = style;
		
		wallFloorMaterial = Material.BEDROCK;
		wallMaterial = plugin.getWallMaterial();
		wallNegativeMaterial = plugin.getNegativeWallMaterial();
		wallDoorways = plugin.isWallDoorways();
		hexishWells = plugin.isHexishWells();
//		sums = new int[wellTypeCount];
	}
	
	public WellWorld getPlugin() {
		return plugin;
	}

	public String getWorldname() {
		return worldname;
	}

	public String getWorldstyle() {
		return worldstyle;
	}
	
	@Override
	public Location getFixedSpawnLocation(World world, Random random) {
		// see if this works any better (loosely based on ExpansiveTerrain)
		int x = random.nextInt(100) + 16;
		int z = random.nextInt(100) + 16;
		int y = world.getHighestBlockYAt(x, z);
		return new Location(world, x, y, z);
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new WellWorldBlockPopulator(this));
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
		
		// figure out what everything looks like
		WellArchetype well = getWellManager(world, random, chunkX, chunkZ);
		if (well != null) {
			
			// well centric chunkX/Z
			int adjustedX = chunkX - well.getX();
			int adjustedZ = chunkZ - well.getZ();
			
			// generate the chunk
			InitialBlocks initialBlocks = new InitialBlocks(world, createChunkData(world), adjustedX, adjustedZ);
			well.generateChunk(initialBlocks, adjustedX, adjustedZ);
			
			// draw the well walls
			generateWalls(well, initialBlocks, adjustedX, adjustedZ);
			
			return initialBlocks.chunkData;
		} else
			return null;
	}
	
	public void generateWalls(WellArchetype well, InitialBlocks source, int wellChunkX, int wellChunkZ) {
		// top
		if (well.includeTop())
			source.setBlocksAt(wallHeightInBlocks - ceilingThicknessInBlocks, wallHeightInBlocks, wallMaterial);
		
		// sides?
		boolean wallNorth = wellChunkX == 0;
		boolean wallSouth = wellChunkX == WellWorld.wellWidthInChunks - 1;
		boolean wallWest = wellChunkZ == 0;
		boolean wallEast = wellChunkZ == WellWorld.wellWidthInChunks - 1;
		if (wallNorth || wallSouth || wallEast || wallWest) {
			
			// place the walls themselves
			int step = wallHeightInBlocks / wallThicknessInBlocks;
			for (int i = 0; i < wallThicknessInBlocks; i++) {
				int top = wallHeightInBlocks - i * step;
				if (wallNorth)
					source.setBlocks(i, i + 1, 0, top, 0, 16, wallMaterial);
				if (wallSouth)
					source.setBlocks(16 - i - 1, 16 - i, 0, top, 0, 16, wallMaterial);
				if (wallWest)
					source.setBlocks(0, 16, 0, top, i, i + 1, wallMaterial);
				if (wallEast)
					source.setBlocks(0, 16, 0, top, 16 - i - 1, 16 - i, wallMaterial);
			}
			
			// doorways anyone?
			if (wallDoorways) {
				
				// punch some random holes in the walls... for now
				int y1 = lowestDoors + (well.random.nextInt(portalLevels) + 1) * 3;
				int y2 = y1 + 2;
	
				// punch the door
				if (wallNorth) {
					source.setBlocks(0, 2, y1, y2, 9, 10, wallNegativeMaterial); // door
					source.setBlocks(0, 1, y1, y2, 7, 9, wallNegativeMaterial); // hall
					source.setBlocks(2, 3, y1 - 1, y1, 8, 11, wallMaterial); // balcony
				}
				if (wallSouth) {
					source.setBlocks(14, 16, y1, y2, 6, 7, wallNegativeMaterial); // door
					source.setBlocks(15, 16, y1, y2, 7, 9, wallNegativeMaterial); // hall
					source.setBlocks(13, 14, y1 - 1, y1, 5, 8, wallMaterial); // balcony
				}
				if (wallWest) {
					source.setBlocks(9, 10, y1, y2, 0, 2, wallNegativeMaterial); // door
					source.setBlocks(7, 9, y1, y2, 0, 1, wallNegativeMaterial); // hall
					source.setBlocks(8, 11, y1 - 1, y1, 2, 3, wallMaterial); // balcony
				}
				if (wallEast) {
					source.setBlocks(6, 7, y1, y2, 14, 16, wallNegativeMaterial); // door
					source.setBlocks(7, 9, y1, y2, 15, 16, wallNegativeMaterial); // hall
					source.setBlocks(5, 8, y1 - 1, y1, 13, 14, wallMaterial); // balcony
				}
			}
		}
		
		// bottom
		if (well.includeBottom())
			source.setBlocksAt(0, floorThicknessInBlocks, wallFloorMaterial);
	}
	
	// Class instance data
	private Hashtable<Long, WellArchetype> wells;
	private static final int wellWidthInChunksHalf = WellWorld.wellWidthInChunks / 2;

	public WellArchetype getWellManager(World world, Random random, int chunkX, int chunkZ) {
		// get the list of wells
		if (wells == null)
			wells = new Hashtable<Long, WellArchetype>();
		
		// find the origin for the well
		int wellX = calcOrigin(chunkX);
		int wellZ = calcOrigin(chunkZ);
		
		// hex offset... finally!
		if (hexishWells && (Math.abs(wellX) / WellWorld.wellWidthInChunks) % 2 != 0)
			if (wellZ + wellWidthInChunksHalf > chunkZ)
				wellZ -= wellWidthInChunksHalf;
			else
				wellZ += wellWidthInChunksHalf;

		// calculate the plat's key
		long wellpos = (long) wellX * (long) Integer.MAX_VALUE + (long) wellZ;
		Long wellkey = Long.valueOf(wellpos);

		// see if the well is already out there
		WellArchetype wellmanager = wells.get(wellkey);
		
		// doesn't exist? then make it!
		if (wellmanager == null) {
			
			// calculate the well's random seed
			long wellseed = wellpos ^ world.getSeed();
			
			// make sure the initial spawn location is "safe-ish"
			if (wellX == 0 && wellZ == 0)
				wellmanager = new KnollsWell(world, wellseed, wellX, wellZ);
			else {
				
				// noise please
				if (generator == null)
					generator = new SimplexNoiseGenerator(world.getSeed());
//				Random wellrand = new Random(wellseed);
//				double noise = (generator.noise(wellrand.nextDouble() * wellX, 
//												wellrand.nextDouble() * wellZ, 
//												wellrand.nextDouble() * 23.0,
//												wellrand.nextGaussian() * 71.0) + 1) / 2;
//				double noise = (generator.noise(wellX / ByteChunk.Width, wellZ / ByteChunk.Width) + 1) / 2;
//				double noise = ((generator.noise(wellX, wellZ) + 1) / 2 + 
//								 getWellFactor(wellX) + 
//								 getWellFactor(wellZ) + 
//								 new Random(wellseed).nextDouble()) / 4;
//				double noise = new Random(wellseed).nextDouble();
//				double doubleNoise = wellrand.nextDouble();
//				double gaussianNoise = wellrand.nextGaussian();
//				double noise = (doubleNoise + gaussianNoise) / 2.0;
//				double noise = (wellrand.nextGaussian() + 1.0) / 2.0;
				double noise = new HighQualityRandom(wellseed).nextDouble();
				
//				WellWorld.log.info("Well: " + wellX + ", " + wellZ + " Noise = " + noise);
				
				// pick one from the list
				wellmanager = chooseWellManager(noise, world, wellseed, wellX, wellZ);
			}
			
			// remember it for the next time
			wells.put(wellkey, wellmanager);
		}
		
		// return it
		return wellmanager;
	}
	
//	private String getSums() {
//		String result = "Spread:";
//		for (int i = 0; i < wellTypeCount; i++)
//			result = result + " " + sums[i];
//		return result;
//	}
	
	private final int wellTypeCount = 17;
	private WellArchetype chooseWellManager(double noise, World world, long seed, int wellX, int wellZ) {
		
		int index = NoiseGenerator.floor(noise * wellTypeCount);
//		sums[index]++;
//		WellWorld.log.info(getSums());
		
		switch (index) {
		case 1:
			return new AlienWorldWell(world, seed, wellX, wellZ);
		case 2:
			return new AlienCavernWell(world, seed, wellX, wellZ);
		case 3:
			return new RealisticMoonWell(world, seed, wellX, wellZ);
		case 4:
			return new BasaltFieldWell(world, seed, wellX, wellZ);
		case 5:
			return new PlatformWell(world, seed, wellX, wellZ);
		case 6:
			return new VolcanoWell(world, seed, wellX, wellZ);
		case 7:
			return new ForestWell(world, seed, wellX, wellZ);
		case 8:
			return new SmoothSnowWell(world, seed, wellX, wellZ);
			
		// Codename_B's Banana wells
		case 9:
			return new BananaOctaveWell(world, seed, wellX, wellZ);
		case 10:
			return new BananaSkyWell(world, seed, wellX, wellZ);
		case 11:
			return new BananaTrigWell(world, seed, wellX, wellZ);
		case 12:
			return new BananaIceWell(world, seed, wellX, wellZ);
		case 13:
			return new BananaVoidWell(world, seed, wellX, wellZ);
		case 14:
			return new BananaForestWell(world, seed, wellX, wellZ);

		// Khyperia's TrippyTerrain based wells
		case 15:
			return new KhylandWell(world, seed, wellX, wellZ);
		case 16:
			return new PancakeWell(world, seed, wellX, wellZ);
			
//      debug wells
//		case 17:
//			return new TypePickerWell(world, seed, wellX, wellZ);
//	 	case 18:
//			return new CityPlatWell(world, seed, wellX, wellZ);
//	 	case 19:
//			return new RoadPlatWell(world, seed, wellX, wellZ);
//	 	case 20:
//			return new RiverPlatWell(world, seed, wellX, wellZ);
//	 	case 21:
//			return new CityRoadsPlatWell(world, seed, wellX, wellZ);
//	 	case 22:
//			return new MineralPlatWell(world, seed, wellX, wellZ);
//	 	case 23:
//			return new PlanningZonesPlatWell(world, seed, wellX, wellZ);

//      not enabled as I don't have permission to do so
//		case 17:
//			return new MicroNordicWell(world, seed, wellX, wellZ);
//		case 18:
//			return new DinnerboneMoonWell(seed, wellX, wellZ);
			
		// The generic world
		default:
			return new KnollsWell(world, seed, wellX, wellZ);
			
// not enabled as they are kind of boring :-)
//		case 4:
//			return new VeryEmptyWell(world, seed, wellX, wellZ);
//		case 5:
//			return new VerySimpleFlatWell(world, seed, wellX, wellZ);
//		case 6:
//			return new VerySimpleWaterWell(world, seed, wellX, wellZ);
//		case 7:
//			return new VerySimpleHillyWell(world, seed, wellX, wellZ);
//		case 9:
//			return new SimplexNoiseWell(world, seed, wellX, wellZ);
//		case 10:
//			return new SimplexOctaveWell(world, seed, wellX, wellZ);

		}
	}
	
	// Supporting code used by getWellManager
	private int calcOrigin(int i) {
		if (i >= 0) {
			return i / WellWorld.wellWidthInChunks * WellWorld.wellWidthInChunks;
		} else {
			return -((Math.abs(i + 1) / WellWorld.wellWidthInChunks * WellWorld.wellWidthInChunks) + WellWorld.wellWidthInChunks);
		}
	}
	
	private class HighQualityRandom extends Random {
		
		private static final long serialVersionUID = 1L;
		// private Lock l = new ReentrantLock();
		private long u;
		private long v = 4101842887655102017L;
		private long w = 1;

//		public HighQualityRandom() {
//			this(System.nanoTime());
//		}
//
		public HighQualityRandom(long seed) {
			// l.lock();
			u = seed ^ v;
			nextLong();
			v = u;
			nextLong();
			w = v;
			nextLong();
			// l.unlock();
		}

		public long nextLong() {
			// l.lock();
			// try {
			u = u * 2862933555777941757L + 7046029254386353087L;
			v ^= v >>> 17;
			v ^= v << 31;
			v ^= v >>> 8;
			w = 4294957665L * (w & 0xffffffff) + (w >>> 32);
			long x = u ^ (u << 21);
			x ^= x >>> 35;
			x ^= x << 4;
			long ret = (x + v) ^ w;
			return ret;
			// } finally {
			// l.unlock();
			// }
		}
		
		public double nextDouble() {
			return (double) Math.abs(nextLong()) / (double) Long.MAX_VALUE;
		}

		protected int next(int bits) {
			return (int) (nextLong() >>> (64 - bits));
		}

	}
	
}
