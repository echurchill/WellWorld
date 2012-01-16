package me.daddychurchill.WellWorld.WellTypes.NotUsed;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class CityPlatWell extends WellArchetype {

	private double xFactor = 25.0;
	private double zFactor = 25.0;
	
	private SimplexNoiseGenerator generator;
	
	public CityPlatWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);

		generator = new SimplexNoiseGenerator(randseed);
	}

	@Override
	public void generateChunk(ByteChunk chunk, int chunkX, int chunkZ) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				double noise = (generator.noise((chunkX * 16 + x) / xFactor, (chunkZ * 16 + z) / zFactor) + 1) / 2;
				drawPlat(noise, chunk, x, z);
			}
		}
	}
	
	private void drawPlat(double noise, ByteChunk chunk, int x, int z) {
//		chunk.setBlocks(x, 100, 100 + (int) Math.round(noise * 10), z, (byte) Material.IRON_BLOCK.getId());
		switch ((int) Math.round(noise * 11)) {
		case 0:
			// city center
			chunk.setBlocks(x, 100, 102, z, (byte) Material.DIAMOND_BLOCK.getId());
			break;
		case 1:
			// highrise
			chunk.setBlocks(x, 100, 106, z, (byte) Material.IRON_BLOCK.getId());
			break;
		case 2:
			// unfinished
			chunk.setBlocks(x, 100, 105, z, (byte) Material.GLASS.getId());
			break;
		case 3:
			// midrise
			chunk.setBlocks(x, 100, 104, z, (byte) Material.IRON_BLOCK.getId());
			break;
		case 4:
			// unfinished
			chunk.setBlocks(x, 100, 103, z, (byte) Material.GLASS.getId());
			break;
		case 5:
			// lowrise
			chunk.setBlocks(x, 100, 102, z, (byte) Material.IRON_BLOCK.getId());
			break;
		case 6:
			// mall
			chunk.setBlocks(x, 100, 102, z, (byte) Material.GOLD_BLOCK.getId());
			break;
		case 7:
			// park
			chunk.setBlocks(x, 100, 101, z, (byte) Material.GRASS.getId());
			break;
		case 8:
			// residential
			chunk.setBlocks(x, 100, 102, z, (byte) Material.SANDSTONE.getId());
			break;
		case 9:
			// farm
			chunk.setBlocks(x, 100, 101, z, (byte) Material.DIRT.getId());
			break;
		default:
			// water
			chunk.setBlocks(x, 100, 101, z, (byte) Material.LAPIS_BLOCK.getId());
			break;
		}
		chunk.setBlock(x, 99, z, (byte) Material.STONE.getId());
	}

	@Override
	public void populateBlocks(Chunk chunk, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

	}
}
