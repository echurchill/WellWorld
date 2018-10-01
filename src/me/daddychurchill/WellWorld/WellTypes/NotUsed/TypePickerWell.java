package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.InitialBlocks;

public class TypePickerWell extends WellArchetype {

	private SimplexNoiseGenerator generator;
	
	public TypePickerWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = generator.noise(chunkX * 16 + x, chunkZ * 16 + z);
				chunk.setBlock(x, 100, z, getWellType((noise + 1) / 2));
			}
		}
	}
	
	private Material getWellType(double noise) {
		switch (NoiseGenerator.floor(noise * 17)) {
		case 0:
			return Material.NETHER_BRICKS; 
		case 1:
			return Material.BEDROCK; 
		case 2:
			return Material.BOOKSHELF; 
		case 3:
			return Material.BRICKS; 
		case 4:
			return Material.IRON_BLOCK; 
		case 5:
			return Material.CLAY; 
		case 6:
			return Material.COAL_ORE; 
		case 7:
			return Material.COBBLESTONE; 
		case 8:
			return Material.DIAMOND_BLOCK; 
		case 9:
			return Material.DIAMOND_ORE; 
		case 10:
			return Material.END_STONE; 
		case 11:
			return Material.GLOWSTONE; 
		case 12:
			return Material.GOLD_BLOCK; 
		case 13:
			return Material.GOLD_ORE; 
		case 14:
			return Material.GRASS_BLOCK; 
		case 15:
			return Material.DIRT;
		case 16:
			return Material.LAPIS_ORE;
		default:
			return Material.GLASS;
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
