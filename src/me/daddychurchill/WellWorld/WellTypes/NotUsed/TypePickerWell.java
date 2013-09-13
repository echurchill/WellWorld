package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class TypePickerWell extends WellArchetype {

	private SimplexNoiseGenerator generator;
	
	public TypePickerWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = generator.noise(chunkX * 16 + x, chunkZ * 16 + z);
				chunk.setBlock(x, 100, z, getWellType((noise + 1) / 2));
			}
		}
	}
	
	private byte getWellType(double noise) {
		switch (NoiseGenerator.floor(noise * 17)) {
		case 0:
			return (byte) Material.NETHER_BRICK.getId(); 
		case 1:
			return (byte) Material.BEDROCK.getId(); 
		case 2:
			return (byte) Material.BOOKSHELF.getId(); 
		case 3:
			return (byte) Material.BRICK.getId(); 
		case 4:
			return (byte) Material.IRON_BLOCK.getId(); 
		case 5:
			return (byte) Material.CLAY.getId(); 
		case 6:
			return (byte) Material.COAL_ORE.getId(); 
		case 7:
			return (byte) Material.COBBLESTONE.getId(); 
		case 8:
			return (byte) Material.DIAMOND_BLOCK.getId(); 
		case 9:
			return (byte) Material.DIAMOND_ORE.getId(); 
		case 10:
			return (byte) Material.ENDER_STONE.getId(); 
		case 11:
			return (byte) Material.GLOWSTONE.getId(); 
		case 12:
			return (byte) Material.GOLD_BLOCK.getId(); 
		case 13:
			return (byte) Material.GOLD_ORE.getId(); 
		case 14:
			return (byte) Material.GRASS.getId(); 
		case 15:
			return (byte) Material.DIRT.getId();
		case 16:
			return (byte) Material.LAPIS_ORE.getId();
		default:
			return (byte) Material.GLASS.getId();
		}
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}

}
