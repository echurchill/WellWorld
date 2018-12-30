package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public abstract class StandardWellArchetype extends WellArchetype {

	protected int specialBlockOdds; // 1/n chance that there is minerals on this level
	protected int specialsPerLayer; // number of minerals per layer
	protected int bottomLevel; // how thick is the bottom bit
	protected int middleThickness; // how thick is the middle bit
	protected int liquidLevel; // how thick is the water bit
	protected int flowerOdds = 6; // 1/n chance that there is a flower on the grass, if there isn't a tree else
									// make some tall grass
	protected TreeType treeType;
	protected int treesPerChunk;

	protected Material materialBottom; // what is the stone made of?
	protected Material materialMiddle; // what is dirt made of?
	protected Material materialTop; // what is grass made of?
	protected Material materialLiquidBase; // what is sand made of?
	protected Material materialLiquid; // what is the liquid made of?
	protected Material materialBlades; // what is a blade of grass made of?
	protected Material materialFlower; // what is a flower made of?

	protected Material materialMineral; // for later use in the populator
	protected Material materialFertile;
//	protected int airId; 

	protected int octives = 3;
	protected double xFactor = 1.0;
	protected double zFactor = 1.0;
	protected double frequency = 0.5;
	protected double amplitude = 0.5;
	protected double hScale = 1.0 / 64.0;
	protected double vScale = 16.0;

	private SimplexOctaveGenerator generator;

	public StandardWellArchetype(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		specialBlockOdds = random.nextInt(3) + 1;
		specialsPerLayer = random.nextInt(20) + 10;
		bottomLevel = random.nextInt(32) + 48;
		liquidLevel = bottomLevel + random.nextInt(8) - 4;
		middleThickness = random.nextInt(5) + 1;
		treeType = random.nextBoolean() ? TreeType.BIRCH : TreeType.TREE;
		treesPerChunk = 2;

		xFactor = calcRandomRange(0.75, 1.25);
		zFactor = calcRandomRange(0.75, 1.25);
		frequency = calcRandomRange(0.40, 0.60);
		amplitude = calcRandomRange(0.40, 0.60);
		vScale = calcRandomRange(13.0, 19.0);

		materialBottom = Material.STONE;
		materialMiddle = Material.DIRT;
		materialTop = Material.GRASS_BLOCK;
		materialLiquidBase = Material.SAND;
		materialLiquid = Material.WATER;
		materialBlades = Material.GRASS;
		materialFlower = random.nextBoolean() ? Material.POPPY : Material.DANDELION;

		materialMineral = materialBottom;
		materialFertile = materialTop;
	}

	protected SimplexOctaveGenerator getGenerator() {
		if (generator == null) {
			generator = new SimplexOctaveGenerator(randseed, octives);
			generator.setScale(hScale);
		}
		return generator;
	}

	protected int getHeight(double x, double z) {
		return NoiseGenerator.floor(getGenerator().noise(x, z, frequency, amplitude) * vScale) + bottomLevel;
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = getHeight((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor);
				chunk.setBlocks(x, 1, y - middleThickness, z, materialBottom);
				chunk.setBlocks(x, y - middleThickness, y, z, materialMiddle);
				if (y < liquidLevel) {
					chunk.setBlock(x, y, z, materialLiquidBase);
					chunk.setBlocks(x, y + 1, liquidLevel, z, materialLiquid);
				} else
					chunk.setBlock(x, y, z, materialTop);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		populateSpecials(chunk, chunkX, chunkZ);
		populateTrees(chunk, chunkX, chunkZ);
	}

	protected void populateSpecials(Chunk chunk, int chunkX, int chunkZ) {

		// sprinkle minerals/foliage for each y layer, one of millions of ways to do
		// this!
		for (int y = 1; y < 127; y++) {
			if (random.nextInt(specialBlockOdds) == 0) {
				for (int i = 0; i < specialsPerLayer; i++) {
					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
					Material material = block.getType();
					if (material != Material.AIR) {

						// Transmutation?
						if (material == materialMineral)
							populateMineral(block, y);
						else if (material == materialFertile)
							populateFoliage(block, y);
					}
				}
			}
		}
	}

	protected void populateMineral(Block block, int atY) {
		block.setType(pickRandomMineralAt(atY), false);
	}

	protected void populateFoliage(Block block, int atY) {
		Location foliageAt = block.getLocation().add(0, 1, 0);
		if (random.nextInt(flowerOdds) == 0)
			world.getBlockAt(foliageAt).setType(materialFlower);
		else {
			world.getBlockAt(foliageAt).setType(materialBlades);
		}
	}

	protected void populateTrees(Chunk chunk, int chunkX, int chunkZ) {

		// plant lots of trees
		int worldX = chunk.getX() * 16; // these use the chunk's world relative location instead..
		int worldZ = chunk.getZ() * 16; // ..of well's as generateTree needs world coordinates
		for (int t = 0; t < treesPerChunk; t++) {
			int centerX = worldX + random.nextInt(16);
			int centerZ = worldZ + random.nextInt(16);
			int centerY = world.getHighestBlockYAt(centerX, centerZ);
			Block rootBlock = world.getBlockAt(centerX, centerY - 1, centerZ);
			if (rootBlock.getType() == materialFertile) {
				Location treeAt = rootBlock.getLocation().add(0, 1, 0);
				world.generateTree(treeAt, treeType);
			}
		}
	}
}
