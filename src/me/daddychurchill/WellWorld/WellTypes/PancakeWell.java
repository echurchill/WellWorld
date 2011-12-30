package me.daddychurchill.WellWorld.WellTypes;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import me.daddychurchill.WellWorld.WellArchetype;
import me.daddychurchill.WellWorld.Support.ByteChunk;

public class PancakeWell extends WellArchetype {

	// Port of Khyperia's TrippyTerrain/Pancake for testing purposes
	
	public PancakeWell(long seed, int wellX, int wellZ) {
		super(seed, wellX, wellZ);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populateChunk(World world, ByteChunk chunk) {

		SimplexNoiseGenerator simplex = new SimplexNoiseGenerator(randseed);
		int cx = chunk.getX();
		int cz = chunk.getZ();
		Material[][][] blocks = new Material[16][128][16];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 128; y++) {
					double noise = simplex.noise((cx * 16 + x) / 50.0f, y / 5.0f, (cz * 16 + z) / 50.0f);
					noise += (64 - y) * (1 / 32f);
					blocks[x][y][z] = (noise > 0) ? Material.STONE : Material.AIR;
				}
			}
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 5; y < 127; y++) {
					if (blocks[x][y][z].equals(Material.STONE) && blocks[x][y + 1][z].equals(Material.AIR)) {
						blocks[x][y][z] = Material.GRASS;
						for (int height = 1; height < 3; height++)
							if (blocks[x][y - height][z].equals(Material.AIR) == false)
								blocks[x][y - height][z] = Material.DIRT;
					}
				}
			}
		}

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int xz = (x * 16 + z) * 128;
				for (int y = 0; y < 128; y++) {
					chunk.blocks[xz + y] = (byte) blocks[x][y][z].getId();
				}
			}
		}
	}

	@Override
	public void populateBlocks(World world, Chunk chunk) {
		// TODO Auto-generated method stub

	}

}
