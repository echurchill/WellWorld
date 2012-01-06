package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import me.daddychurchill.WellWorld.WellArchetype;

public abstract class BananaWellArchetype extends WellArchetype {

	// normal materials
	protected byte byteLiquid = (byte) Material.STATIONARY_WATER.getId();
	protected byte byteStone = (byte) Material.STONE.getId();
	protected byte byteDirt = (byte) Material.DIRT.getId();
	protected byte byteGrass = (byte) Material.GRASS.getId();
	protected byte byteLog = (byte) Material.LOG.getId();
	protected byte byteLeaves = (byte) Material.LEAVES.getId();
	protected byte byteAir = (byte) Material.AIR.getId();
	protected int intOre = byteStone;
	protected int intTreeBase = byteGrass;
	protected int intTreeTrunk = byteLog;
	protected int intTreeLeaves = byteLeaves;
	
	protected byte byteTreeData = 0;
	protected int intAir = Material.AIR.getId();
	protected int minTreeHeight = 5;
	protected int maxTreeHeight = 5;
	
	public BananaWellArchetype(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		// figure out materials
		calculateMaterials();
	}
	
	// override this if you want something special
	protected void calculateMaterials() {
		randomizeMaterials();
		
		//  copy over the "seed" materials used by populateOres and populateFoliage
		intOre = byteStone;
		intTreeBase = byteGrass;
	}
	
	protected void randomizeMaterials() {
		switch (random.nextInt(5)) {
		case 0:
			// lava, sand/sandstone/stone, mushroom stalks
			byteLiquid = (byte) Material.STATIONARY_LAVA.getId();
			byteStone = (byte) Material.STONE.getId();
			byteDirt = (byte) Material.SANDSTONE.getId();
			byteGrass = (byte) Material.SAND.getId();
			intTreeTrunk = Material.HUGE_MUSHROOM_1.getId();
			intTreeLeaves = intAir;
			byteTreeData = (byte) (random.nextInt(2) == 0 ? 10 : 1);
			break;
		case 1:
			// air, sand/sandstone/stone, cactus
			byteLiquid = (byte) Material.AIR.getId();
			byteStone = (byte) Material.STONE.getId();
			byteDirt = (byte) Material.SANDSTONE.getId();
			byteGrass = (byte) Material.SAND.getId();
			intTreeTrunk = Material.CACTUS.getId();
			intTreeLeaves = intAir;
			minTreeHeight = 2;
			maxTreeHeight = 4;
			break;
		case 3:
			// water, glass/glowstone/endstone, crystals
			byteLiquid = (byte) Material.STATIONARY_WATER.getId();
			byteStone = (byte) Material.ENDER_STONE.getId();
			byteDirt = (byte) Material.SOUL_SAND.getId();
			byteGrass = (byte) Material.MYCEL.getId();
			intTreeTrunk = Material.GLOWSTONE.getId();
			intTreeLeaves = Material.THIN_GLASS.getId();
			break;
		default:
			// water, grass/dirt/stone, normal trees are the default
			byteTreeData = (byte) random.nextInt(3);
			break;
		}
	}
	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// ores
		populateOres(chunk);
		
		// foliage
		populateFoliage(chunk);
	}

	/**
	 * Populates the world with ores.
	 *
	 * @author Nightgunner5
	 * @author Markus Persson
	 */
	private static final int[] iterations = new int[] {10, 20, 20, 2, 8, 1, 1, 1};
	private static final int[] amount = new int[] {32, 16, 8, 8, 7, 7, 6};
	private static final int[] type = new int[] {Material.GRAVEL.getId(), Material.COAL_ORE.getId(),
		Material.IRON_ORE.getId(), Material.GOLD_ORE.getId(), Material.REDSTONE_ORE.getId(),
		Material.DIAMOND_ORE.getId(), Material.LAPIS_ORE.getId()};
	private static final int[] maxHeight = new int[] {128, 128, 128, 128, 128, 64,
		32, 16, 16, 32};

	protected void populateOres(Chunk chunk) {
		// ores
		for (int i = 0; i < type.length; i++) {
			for (int j = 0; j < iterations[i]; j++) {
				placeOre(chunk, random.nextInt(16),
						 random.nextInt(maxHeight[i]), random.nextInt(16),
						 amount[i], type[i]);
			}
		}
	}
	
	private void placeOre(Chunk source, int originX,
			 int originY, int originZ, int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int x = originX + random.nextInt(amount / 2) - amount / 4;
			int y = originY + random.nextInt(amount / 4) - amount / 8;
			int z = originZ + random.nextInt(amount / 2) - amount / 4;
			x &= 0xf;
			z &= 0xf;
			if (y > 127 || y < 0) {
				continue;
			}
			Block block = source.getBlock(x, y, z);
			if (block.getTypeId() == intOre) {
				block.setTypeId(type, false);
			}
		}
	}

	private static final int chanceRange = 150;
	
	protected void populateFoliage(Chunk chunk) {
		
//		int centerX = (chunk.getX() << 4) + random.nextInt(16);
//		int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
//		if (random.nextBoolean()) {
//			data = 2;
//			height = 5 + random.nextInt(3);
//		}
//
//		//EC: tweaked the biome logic a bit
//		switch (getBiome()) {
//		case FOREST:
//			chance = 160;
//			multiplier = 10;
//			break;
//		case PLAINS:
//			chance = 40;
//			break;
//		case RAINFOREST:
//			chance = 160;
//			multiplier = 10;
//			break;
//		case SAVANNA:
//			chance = 20;
//			break;
//		case SEASONAL_FOREST:
//			chance = 140;
//			multiplier = 8;
//			break;
//		case SHRUBLAND:
//			chance = 60;
//			break;
//		case SWAMPLAND:
//			chance = 120;
//			break;
//		case TAIGA:
//			chance = 120;
//			data = 1;
//			height = 8 + random.nextInt(3);
//			multiplier = 3;
//			break;
//		case TUNDRA:
//			chance = 10;
//			data = 1;
//			height = 7 + random.nextInt(3);
//			break;
//		case SKY:
//		case DESERT:
//		case HELL:
//		case ICE_DESERT:
//			chance = 5;
//			return;
//		}

		byte data = (byte) random.nextInt(3);
		int chance = chanceRange / 2;
		int height = minTreeHeight + random.nextInt(maxTreeHeight);
		int multiplier = 3 + random.nextInt(7);

		for (int i = 0; i < multiplier; i++) {
			int centerX = (chunk.getX() << 4) + random.nextInt(16);
			int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
			if (random.nextInt(chanceRange) < chance) {
				int centerY = world.getHighestBlockYAt(centerX, centerZ) - 1;
				Block sourceBlock = world.getBlockAt(centerX, centerY, centerZ);

				// found a place to put it?
				if (sourceBlock.getTypeId() == intTreeBase) {
					
					// leaves or leave?
					if (intTreeLeaves != intAir) {
						setBlock(centerX, centerY + height + 1, centerZ, intTreeLeaves, data);
						//world.getBlockAt(centerX, centerY + height + 1, centerZ).setTypeIdAndData(intLeaves, data, true);
						for (int j = 0; j < 4; j++) {
							setBlock(centerX, centerY + height + 1 - j, centerZ - 1, intTreeLeaves, data);
							setBlock(centerX, centerY + height + 1 - j, centerZ + 1, intTreeLeaves, data);
							setBlock(centerX - 1, centerY + height + 1 - j, centerZ, intTreeLeaves, data);
							setBlock(centerX + 1, centerY + height + 1 - j, centerZ, intTreeLeaves, data);
							//world.getBlockAt(centerX, centerY + height + 1 - j, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX, centerY + height + 1 - j, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX - 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX + 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(intLeaves, data, true);
						}

						if (random.nextBoolean()) {
							setBlock(centerX + 1, centerY + height, centerZ + 1, intTreeLeaves, data);
							//world.getBlockAt(centerX + 1, centerY + height, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX + 1, centerY + height, centerZ - 1, intTreeLeaves, data);
							//world.getBlockAt(centerX + 1, centerY + height, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX - 1, centerY + height, centerZ + 1, intTreeLeaves, data);
							//world.getBlockAt(centerX - 1, centerY + height, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX - 1, centerY + height, centerZ - 1, intTreeLeaves, data);
							//world.getBlockAt(centerX - 1, centerY + height, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						}

						setBlock(centerX + 1, centerY + height - 1, centerZ + 1, intTreeLeaves, data);
						setBlock(centerX + 1, centerY + height - 1, centerZ - 1, intTreeLeaves, data);
						setBlock(centerX - 1, centerY + height - 1, centerZ + 1, intTreeLeaves, data);
						setBlock(centerX - 1, centerY + height - 1, centerZ - 1, intTreeLeaves, data);
						setBlock(centerX + 1, centerY + height - 2, centerZ + 1, intTreeLeaves, data);
						setBlock(centerX + 1, centerY + height - 2, centerZ - 1, intTreeLeaves, data);
						setBlock(centerX - 1, centerY + height - 2, centerZ + 1, intTreeLeaves, data);
						setBlock(centerX - 1, centerY + height - 2, centerZ - 1, intTreeLeaves, data);
						//world.getBlockAt(centerX + 1, centerY + height - 1, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX + 1, centerY + height - 1, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX - 1, centerY + height - 1, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX - 1, centerY + height - 1, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX + 1, centerY + height - 2, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX + 1, centerY + height - 2, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX - 1, centerY + height - 2, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						//world.getBlockAt(centerX - 1, centerY + height - 2, centerZ - 1).setTypeIdAndData(intLeaves, data, true);

						for (int j = 0; j < 2; j++) {
							for (int k = -2; k <= 2; k++) {
								for (int l = -2; l <= 2; l++) {
									setBlock(centerX + k, centerY + height - 1 - j, centerZ + l, intTreeLeaves, data);
									//world.getBlockAt(centerX + k, centerY + height
									//	- 1 - j, centerZ + l).setTypeIdAndData(intLeaves, data, true);
								}
							}
						}

						for (int j = 0; j < 2; j++) {
							if (random.nextBoolean()) 
								clearBlock(centerX + 2, centerY + height - 1 - j, centerZ + 2);
							if (random.nextBoolean()) 
								clearBlock(centerX + 2, centerY + height - 1 - j, centerZ - 2);
							if (random.nextBoolean()) 
								clearBlock(centerX - 2, centerY + height - 1 - j, centerZ + 2);
							if (random.nextBoolean()) 
								clearBlock(centerX - 2, centerY + height - 1 - j, centerZ - 2);
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX + 2, centerY + height - 1
							//		- j, centerZ + 2).setTypeIdAndData(intAir, (byte) 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX + 2, centerY + height - 1
							//		- j, centerZ - 2).setTypeIdAndData(intAir, (byte) 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX - 2, centerY + height - 1
							//		- j, centerZ + 2).setTypeIdAndData(intAir, (byte) 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX - 2, centerY + height - 1
							//		- j, centerZ - 2).setTypeIdAndData(intAir, (byte) 0, true);
							//}
						}
					}

					// Trunk
					for (int y = 1; y <= height; y++) {
						world.getBlockAt(centerX, centerY + y, centerZ).setTypeIdAndData(intTreeTrunk, data, false);
					}
				}
			}
		}
	}
	
	protected void setBlock(int x, int y, int z, int blockId, byte blockData) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, blockData, false);
	}
	
	protected void setBlock(int x, int y, int z, int blockId) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, (byte) 0, false);
	}
	
	protected void setBlock(int x, int y, int z, int blockId, byte blockData, boolean blockPhysics) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, blockData, blockPhysics);
	}
	
	protected void setBlock(int x, int y, int z, int blockId, boolean blockPhysics) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() == intAir)
			block.setTypeIdAndData(blockId, (byte) 0, blockPhysics);
	}
	
	protected void clearBlock(int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		if (block.getTypeId() != intAir)
			block.setTypeIdAndData(intAir, (byte) 0, false);
	}
}
