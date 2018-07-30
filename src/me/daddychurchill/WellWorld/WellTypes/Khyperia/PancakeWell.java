package me.daddychurchill.WellWorld.WellTypes.Khyperia;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.Support.InitialBlocks;
import me.daddychurchill.WellWorld.WellTypes.StandardWellArchetype;

public class PancakeWell extends StandardWellArchetype {

	// Port of Khyperia's TrippyTerrain/Pancake 
	// EC: converted the Material array and added a few stock populators
//	private byte byteStone = (byte) Material.STONE.getId();
//	private byte byteDirt = (byte) Material.DIRT.getId();
//	private byte byteGrass = (byte) Material.GRASS_BLOCK.getId();
//	private byte byteAir = (byte) Material.AIR.getId();
	private SimplexNoiseGenerator simplex;
	
	public PancakeWell(World world, long seed, int wellX, int wellZ) {
		super(world, seed, wellX, wellZ);
		
		simplex = new SimplexNoiseGenerator(randseed);	
	}

	@Override
	public void generateChunk(InitialBlocks chunk, int chunkX, int chunkZ) {

		//EC: Moved this up to the initialization section
		//SimplexNoiseGenerator simplex = new SimplexNoiseGenerator(randseed);
		
		//EC: use the one passed in instead
		//int chunkX = chunk.getX();
		//int chunkZ = chunk.getZ();
		
		//EC: directly using the byte array passed in
		//Material[][][] blocks = new Material[16][128][16];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 128; y++) {
					double noise = simplex.noise((chunkX * 16 + x) / 50.0f, y / 5.0f, (chunkZ * 16 + z) / 50.0f);
					noise += (64 - y) * (1 / 32f);
					chunk.setBlock(x, y, z, (noise > 0) ? Material.STONE : Material.AIR);
					//blocks[x][y][z] = (noise > 0) ? Material.STONE : Material.AIR;
				}
			}
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 5; y < 127; y++) {
					if (chunk.isBlock(x, y, z, Material.STONE) && chunk.isBlock(x, y + 1, z, Material.AIR)) {
						chunk.setBlock(x, y, z, Material.GRASS_BLOCK);
						for (int height = 1; height < 3; height++)
							if (!chunk.isBlock(x, y - height, z, Material.AIR))
								chunk.setBlock(x, y - height, z, Material.DIRT);
					}
					//if (blocks[x][y][z].equals(Material.STONE) && blocks[x][y + 1][z].equals(Material.AIR)) {
					//	blocks[x][y][z] = Material.GRASS_BLOCK;
					//	for (int height = 1; height < 3; height++)
					//		if (blocks[x][y - height][z].equals(Material.AIR) == false)
					//			blocks[x][y - height][z] = Material.DIRT;
					//}
				}
			}
		}

		//for (int x = 0; x < 16; x++) {
		//	for (int z = 0; z < 16; z++) {
		//		int xz = (x * 16 + z) * 128;
		//		for (int y = 0; y < 128; y++) {
		//			chunk.blocks[xz + y] = (byte) blocks[x][y][z].getId();
		//		}
		//	}
		//}
	}
}
