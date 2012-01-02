package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class KnollsWell extends WellArchetype {


	private int specialBlockOdds; // 1/n chance that there is minerals on this level
	private int specialsPerLayer; // number of minerals per layer
	private int stoneLevel; // how thick is the bottom bit
	private int liquidLevel; // how thick is the water bit
	private int dirtThickness; // how thick is the middle bit
	private int treeOdds = 8; // 1/n chance that there is a tree
	private int flowerOdds = 6; // 1/n chance that there is a flower on the grass, if there isn't a tree else make some tall grass
	
	private Material stoneMaterial; // what is the stone made of?
	private Material dirtMaterial; // what is dirt made of?
	private Material grassMaterial; // what is grass made of?
	private Material sandMaterial; // what is sand made of?
	private Material liquidMaterial; // what is the liquid made of?
	private Material bladesMaterial; // what is a blade of grass made of?
	private Material flowerMaterial; // what is a flower made of?
	
	private int stoneId; // for later use in the populator
	private int grassId;
	
	private int octives = 3;
	private double xFactor = 1.0;
	private double zFactor = 1.0;
	private double fequency = 0.5;
	private double amplitude = 0.5;
	private double hScale = 1.0 / 64.0;
	private double vScale = 16.0;
	private SimplexOctaveGenerator generator;
	
	public KnollsWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		specialBlockOdds = random.nextInt(3) + 1;
		specialsPerLayer = random.nextInt(20) + 10;
		stoneLevel = random.nextInt(32) + 48;
		liquidLevel = stoneLevel + random.nextInt(8) - 4;
		dirtThickness = random.nextInt(5) + 1;
		
		xFactor = calcRandomRange(0.75, 1.25);
		zFactor = calcRandomRange(0.75, 1.25);
		fequency = calcRandomRange(0.40, 0.60);
		amplitude = calcRandomRange(0.40, 0.60);
		vScale = calcRandomRange(13.0, 19.0);
		
		stoneMaterial = Material.STONE;
		dirtMaterial = Material.DIRT;
		grassMaterial = Material.GRASS;
		sandMaterial = Material.SAND;
		liquidMaterial = Material.STATIONARY_WATER;
		bladesMaterial = Material.LONG_GRASS;
		flowerMaterial = random.nextBoolean() ? Material.RED_ROSE : Material.YELLOW_FLOWER;
		
		stoneId = stoneMaterial.getId();
		grassId = grassMaterial.getId();
		
		generator = new SimplexOctaveGenerator(randseed, octives);
		generator.setScale(hScale);
	}

	@Override
	public void populateChunk(ByteChunk chunk) {
		int chunkX = chunk.getX();
		int chunkZ = chunk.getZ();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor, fequency, amplitude) * vScale;
				int y = stoneLevel + (int)noise;
				chunk.setBlocks(x, 1, y - dirtThickness, z, stoneMaterial);
				chunk.setBlocks(x, y - dirtThickness, y, z, dirtMaterial);
				if (y < liquidLevel) {
					chunk.setBlock(x, y, z, sandMaterial);
					chunk.setBlocks(x, y + 1, liquidLevel, z, liquidMaterial);
				} else
					chunk.setBlock(x, y, z, grassMaterial);
			}
		}
	}

	@Override
	public void populateBlocks(Chunk chunk) {

		// sprinkle minerals for each y layer, one of millions of ways to do this!
		for (int y = 1; y < 127; y++) {
			if (random.nextInt(specialBlockOdds) == 0) {
				for (int i = 0; i < specialsPerLayer; i++) {
					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
					int id = block.getTypeId();
					
					// Transmutation?
					if (id == stoneId)
						block.setTypeId(pickRandomMineralAt(y).getId(), false);
					
					// If the block supports foliage, then what type will go here?
					else if (id == grassId) {
						Location foliageAt = block.getLocation().add(0, 1, 0);
						if (random.nextInt(treeOdds) == 0) {
							TreeType tree = random.nextBoolean() ? TreeType.BIRCH : TreeType.TREE;
							world.generateTree(foliageAt, tree);
						}
						else if (random.nextInt(flowerOdds) == 0)
							world.getBlockAt(foliageAt).setType(flowerMaterial);
						else {
							Block grass = world.getBlockAt(foliageAt);
							grass.setType(bladesMaterial);
							grass.setData((byte)1);
						}
					}
				}
			}
		}
	}

}
