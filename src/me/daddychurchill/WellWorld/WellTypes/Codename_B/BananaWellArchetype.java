package me.daddychurchill.WellWorld.WellTypes.Codename_B;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import me.daddychurchill.WellWorld.WellArchetype;

public abstract class BananaWellArchetype extends WellArchetype {

	// normal materials
	protected Material materialLiquid = Material.WATER;
	protected Material materialStone = Material.STONE;
	protected Material materialDirt = Material.DIRT;
	protected Material materialGrass = Material.GRASS;
	protected Material materialLog = Material.BIRCH_LOG;
	protected Material materialLeaves = Material.BIRCH_LEAVES;
	protected Material materialOre = materialStone;
	protected Material materialTreeBase = materialGrass;
	protected Material materialTreeTrunk = materialLog;
	protected Material materialTreeLeaves = materialLeaves;
	
//	protected byte byteTreeData = 0;
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
		materialOre = materialStone;
		materialTreeBase = materialGrass;
	}
	
	protected void randomizeMaterials() {
		switch (random.nextInt(5)) {
		case 0:
			// lava, sand/sandstone/stone, mushroom stalks
			materialLiquid = Material.LAVA;
			materialStone = Material.STONE;
			materialDirt = Material.SANDSTONE;
			materialGrass = Material.SAND;
			materialTreeTrunk = Material.BROWN_MUSHROOM_BLOCK;
			materialTreeLeaves = Material.AIR;
//			byteTreeData = (byte) (random.nextInt(2) == 0 ? 10 : 1);
			break;
		case 1:
			// air, sand/sandstone/stone, cactus
			materialLiquid = Material.AIR;
			materialStone = Material.STONE;
			materialDirt = Material.SANDSTONE;
			materialGrass = Material.SAND;
			materialTreeTrunk = Material.CACTUS;
			materialTreeLeaves = Material.AIR;
			minTreeHeight = 2;
			maxTreeHeight = 4;
			break;
		case 3:
			// water, glass/glowstone/endstone, crystals
			materialLiquid = Material.WATER;
			materialStone = Material.END_STONE;
			materialDirt = Material.SOUL_SAND;
			materialGrass = Material.MYCELIUM;
			materialTreeTrunk = Material.GLOWSTONE;
			materialTreeLeaves = Material.GLASS_PANE;
			break;
		default:
			// water, grass/dirt/stone, normal trees are the default
//			byteTreeData = (byte) random.nextInt(3);
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
	private static final Material[] material = new Material[] {Material.GRAVEL, Material.COAL_ORE,
		Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE,
		Material.DIAMOND_ORE, Material.LAPIS_ORE};
	private static final int[] maxHeight = new int[] {128, 128, 128, 128, 128, 64,
		32, 16, 16, 32};

	protected void populateOres(Chunk chunk) {
		// ores
		for (int i = 0; i < material.length; i++) {
			for (int j = 0; j < iterations[i]; j++) {
				placeOre(chunk, random.nextInt(16),
						 random.nextInt(maxHeight[i]), random.nextInt(16),
						 amount[i], material[i]);
			}
		}
	}
	
	private void placeOre(Chunk source, int originX,
			 int originY, int originZ, int amount, Material material) {
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
			if (block.getType() == materialOre) {
				block.setType(material, false);
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

		int index = random.nextInt(6);
		Material materialThisLeaves = materialTreeLeaves;
		Material materialThisTrunk = materialTreeTrunk;
		if (materialTreeLeaves == materialLeaves)
			materialThisLeaves = getMaterialLeaves(index);
		if (materialTreeTrunk == materialLog) 
			materialThisTrunk = getMaterialLog(index);
		
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
				if (sourceBlock.getType() == materialTreeBase) {
					
					// leaves or leave?
					if (materialThisLeaves != Material.AIR) {
						setBlock(centerX, centerY + height + 1, centerZ, materialThisLeaves);
						//world.getBlockAt(centerX, centerY + height + 1, centerZ).setTypeIdAndData(intLeaves, data, true);
						for (int j = 0; j < 4; j++) {
							setBlock(centerX, centerY + height + 1 - j, centerZ - 1, materialThisLeaves);
							setBlock(centerX, centerY + height + 1 - j, centerZ + 1, materialThisLeaves);
							setBlock(centerX - 1, centerY + height + 1 - j, centerZ, materialThisLeaves);
							setBlock(centerX + 1, centerY + height + 1 - j, centerZ, materialThisLeaves);
							//world.getBlockAt(centerX, centerY + height + 1 - j, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX, centerY + height + 1 - j, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX - 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(intLeaves, data, true);
							//world.getBlockAt(centerX + 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(intLeaves, data, true);
						}

						if (random.nextBoolean()) {
							setBlock(centerX + 1, centerY + height, centerZ + 1, materialThisLeaves);
							//world.getBlockAt(centerX + 1, centerY + height, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX + 1, centerY + height, centerZ - 1, materialThisLeaves);
							//world.getBlockAt(centerX + 1, centerY + height, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX - 1, centerY + height, centerZ + 1, materialThisLeaves);
							//world.getBlockAt(centerX - 1, centerY + height, centerZ + 1).setTypeIdAndData(intLeaves, data, true);
						}
						if (random.nextBoolean()) {
							setBlock(centerX - 1, centerY + height, centerZ - 1, materialThisLeaves);
							//world.getBlockAt(centerX - 1, centerY + height, centerZ - 1).setTypeIdAndData(intLeaves, data, true);
						}

						setBlock(centerX + 1, centerY + height - 1, centerZ + 1, materialThisLeaves);
						setBlock(centerX + 1, centerY + height - 1, centerZ - 1, materialThisLeaves);
						setBlock(centerX - 1, centerY + height - 1, centerZ + 1, materialThisLeaves);
						setBlock(centerX - 1, centerY + height - 1, centerZ - 1, materialThisLeaves);
						setBlock(centerX + 1, centerY + height - 2, centerZ + 1, materialThisLeaves);
						setBlock(centerX + 1, centerY + height - 2, centerZ - 1, materialThisLeaves);
						setBlock(centerX - 1, centerY + height - 2, centerZ + 1, materialThisLeaves);
						setBlock(centerX - 1, centerY + height - 2, centerZ - 1, materialThisLeaves);
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
									setBlock(centerX + k, centerY + height - 1 - j, centerZ + l, materialThisLeaves);
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
							//		- j, centerZ + 2).setTypeIdAndData(intAir, 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX + 2, centerY + height - 1
							//		- j, centerZ - 2).setTypeIdAndData(intAir, 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX - 2, centerY + height - 1
							//		- j, centerZ + 2).setTypeIdAndData(intAir, 0, true);
							//}
							//if (random.nextBoolean()) {
							//	world.getBlockAt(centerX - 2, centerY + height - 1
							//		- j, centerZ - 2).setTypeIdAndData(intAir, 0, true);
							//}
						}
					}

					// Trunk
					for (int y = 1; y <= height; y++) {
						setBlock(centerX, centerY + y, centerZ, materialThisTrunk);
					}
				}
			}
		}
	}
	
	private Material getMaterialLog(int index) {
		switch (index) {
		case 1:
			return Material.ACACIA_LOG;
		case 2:
			return Material.BIRCH_LOG;
		case 3:
			return Material.DARK_OAK_LOG;
		case 4:
			return Material.JUNGLE_LOG;
		case 5:
			return Material.OAK_LOG;
		default:
			return Material.SPRUCE_LOG;
		}
	}
	
	private Material getMaterialLeaves(int index) {
		switch (index) {
		case 1:
			return Material.ACACIA_LEAVES;
		case 2:
			return Material.BIRCH_LEAVES;
		case 3:
			return Material.DARK_OAK_LEAVES;
		case 4:
			return Material.JUNGLE_LEAVES;
		case 5:
			return Material.OAK_LEAVES;
		default:
			return Material.SPRUCE_LEAVES;
		}
	}
}
