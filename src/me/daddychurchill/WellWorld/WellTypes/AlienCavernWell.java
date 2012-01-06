package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class AlienCavernWell extends WellArchetype {

	private int mineralOdds; // 1/n chance that there is minerals on this level
	private int mineralsPerLayer; // number of minerals per layer
	private int liquidLevel; // how thick is the water bit
	private Material solidMaterial; // what is the stone made of?
	private Material liquidMaterial; // what is the liquid made of?
	//private int leakOdds; // 1/n chance, how often does liquid leak down
	
	private double xFactor;
	private double yFactor;
	private double zFactor;
	private SimplexNoiseGenerator generator;
	
	public AlienCavernWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		mineralOdds = random.nextInt(5) + 1;
		mineralsPerLayer = random.nextInt(10);
		liquidLevel = random.nextInt(32) + 48;
		
		// spice it up
		xFactor = calcRandomRange(35, 55);
		yFactor = calcRandomRange(15, 35);
		zFactor = calcRandomRange(35, 55);
		
		// pick some materials
		switch (random.nextInt(7)) {
		case 1:
			solidMaterial = Material.CLAY;
			liquidMaterial = Material.STATIONARY_LAVA;
			break;
		case 2:
			solidMaterial = Material.SPONGE;
			liquidMaterial = Material.STATIONARY_WATER;
			break;
		case 3:
			solidMaterial = Material.ICE;
			liquidMaterial = Material.STATIONARY_WATER;
			break;
		case 4:
			solidMaterial = Material.NETHERRACK;
			liquidMaterial = Material.STATIONARY_LAVA;
			break;
		case 5:
			solidMaterial = Material.OBSIDIAN;
			liquidMaterial = Material.STATIONARY_LAVA;
			break;
		case 6:
			solidMaterial = Material.LAPIS_BLOCK;
			liquidMaterial = Material.STATIONARY_WATER;
			break;
		case 7:
			solidMaterial = Material.ENDER_STONE;
			liquidMaterial = Material.ICE;
			break;
		default:
			solidMaterial = Material.STONE;
			liquidMaterial = Material.STATIONARY_WATER;
			break;
		}
		
		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 1; y < 128; y++) {
					double noise = generator.noise((chunkX * 16 + x) / xFactor, y / yFactor, (chunkZ * 16 + z) / zFactor);
					
					if (noise >= 0)
						chunk.setBlock(x, y, z, solidMaterial);
					else if (y < liquidLevel)
						chunk.setBlock(x, y, z, liquidMaterial);
				}
			}
		}
	}
	
	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		
		// sprinkle minerals for each y layer, one of millions of ways to do this!
		for (int y = 1; y < 127; y++) {
			if (random.nextInt(mineralOdds) == 0) {
				for (int i = 0; i < mineralsPerLayer; i++) {
					Block block = chunk.getBlock(random.nextInt(16), y, random.nextInt(16));
					if (block.getType() == solidMaterial)
						block.setTypeId(pickRandomMineralAt(y).getId(), false);
				}
			}
		}
	}

	@Override
	public boolean includeTop() {
		return true;
	}
}
